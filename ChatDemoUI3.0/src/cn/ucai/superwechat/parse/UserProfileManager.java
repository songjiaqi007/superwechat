package cn.ucai.superwechat.parse;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.SuperWeChatHelper.DataSyncListener;
import cn.ucai.superwechat.db.IUserModel;
import cn.ucai.superwechat.db.OnCompleteListener;
import cn.ucai.superwechat.db.UserModel;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.PreferenceManager;
import cn.ucai.superwechat.utils.Result;
import cn.ucai.superwechat.utils.ResultUtils;

public class UserProfileManager {
    private static final String TAG = UserProfileManager.class.getSimpleName();

    /**
     * application context
     */
    protected Context appContext = null;

    /**
     * init flag: test if the sdk has been inited before, we don't need to init
     * again
     */
    private boolean sdkInited = false;

    /**
     * HuanXin sync contact nick and avatar listener
     */
    private List<DataSyncListener> syncContactInfosListeners;

    private boolean isSyncingContactInfosWithServer = false;

    private EaseUser currentUser;
    private User currentAppUser;
    IUserModel userModel;

    public UserProfileManager() {
    }

    public synchronized boolean init(Context context) {
        if (sdkInited) {
            return true;
        }
        appContext = context;
        ParseManager.getInstance().onInit(context);
        syncContactInfosListeners = new ArrayList<DataSyncListener>();
        sdkInited = true;
        userModel = new UserModel();
        return true;
    }

    public void addSyncContactInfoListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncContactInfosListeners.contains(listener)) {
            syncContactInfosListeners.add(listener);
        }
    }

    public void removeSyncContactInfoListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncContactInfosListeners.contains(listener)) {
            syncContactInfosListeners.remove(listener);
        }
    }

    public void asyncFetchContactInfosFromServer(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {
        if (isSyncingContactInfosWithServer) {
            return;
        }
        isSyncingContactInfosWithServer = true;
        ParseManager.getInstance().getContactInfos(usernames, new EMValueCallBack<List<EaseUser>>() {

            @Override
            public void onSuccess(List<EaseUser> value) {
                isSyncingContactInfosWithServer = false;
                // in case that logout already before server returns,we should
                // return immediately
                if (!SuperWeChatHelper.getInstance().isLoggedIn()) {
                    return;
                }
                if (callback != null) {
                    callback.onSuccess(value);
                }
            }

            @Override
            public void onError(int error, String errorMsg) {
                isSyncingContactInfosWithServer = false;
                if (callback != null) {
                    callback.onError(error, errorMsg);
                }
            }

        });

    }

    public void notifyContactInfosSyncListener(boolean success) {
        for (DataSyncListener listener : syncContactInfosListeners) {
            listener.onSyncComplete(success);
        }
    }

    public boolean isSyncingContactInfoWithServer() {
        return isSyncingContactInfosWithServer;
    }

    public synchronized void reset() {
        isSyncingContactInfosWithServer = false;
        currentUser = null;
        currentAppUser = null;
        PreferenceManager.getInstance().removeCurrentUserInfo();
    }

    public synchronized EaseUser getCurrentUserInfo() {
        if (currentUser == null) {
            String username = EMClient.getInstance().getCurrentUser();
            currentUser = new EaseUser(username);
            String nick = getCurrentUserNick();
            currentUser.setNick((nick != null) ? nick : username);
            currentUser.setAvatar(getCurrentUserAvatar());
        }
        return currentUser;
    }

    public synchronized User getCurrentAppUserInfo(){
        L.e(TAG,"getCurrentAppUserInfo,currentAppUser="+currentAppUser);
        if (currentAppUser == null || currentAppUser.getMUserName()==null){
            String username = EMClient.getInstance().getCurrentUser();
            currentAppUser = new User(username);
            String nick = getCurrentUserNick();
            currentAppUser.setMUserNick((nick != null) ? nick : username);

        }
        return currentAppUser;
    }

    public boolean updateCurrentUserNickName(final String nickname) {
        userModel.updateUserNick(appContext, EMClient.getInstance().getCurrentUser(), nickname,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        boolean updatenick = false;
                        if (s!=null){
                            Result result = ResultUtils.getResultFromJson(s, User.class);
                            if (result!=null && result.isRetMsg()){
                                User user = (User) result.getRetData();
                                if (user!=null){
                                    updatenick = true;
                                    setCurrentAppUserNick(user.getMUserNick());
                                    SuperWeChatHelper.getInstance().saveAppContact(user);
                                }
                            }
                        }
                        appContext.sendBroadcast(new Intent(I.REQUEST_UPDATE_USER_NICK)
                                .putExtra(I.User.NICK,updatenick));
                    }

                    @Override
                    public void onError(String error) {
                        appContext.sendBroadcast(new Intent(I.REQUEST_UPDATE_USER_NICK)
                                .putExtra(I.User.NICK,false));
                    }
                });
        return false;
    }

    public void uploadUserAvatar(File file) {
        userModel.uploadAvatar(appContext, EMClient.getInstance().getCurrentUser(), file,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        boolean success = false;
                        if (s!=null){
                            Result result = ResultUtils.getResultFromJson(s, User.class);
                            if (result!=null && result.isRetMsg()){
                                User user = (User) result.getRetData();
                                if (user!=null){
                                    success = true;
                                    setCurrentAppUserAvatar(user.getAvatar());
                                    SuperWeChatHelper.getInstance().saveAppContact(user);
                                }
                            }
                        }
                        appContext.sendBroadcast(new Intent(I.REQUEST_UPDATE_AVATAR)
                                .putExtra(I.Avatar.UPDATE_TIME,success));
                    }

                    @Override
                    public void onError(String error) {
                        appContext.sendBroadcast(new Intent(I.REQUEST_UPDATE_AVATAR)
                                .putExtra(I.Avatar.UPDATE_TIME,false));
                    }
                });
//		String avatarUrl = ParseManager.getInstance().uploadParseAvatar(data);
//		if (avatarUrl != null) {
//			setCurrentUserAvatar(avatarUrl);
//		}
//		return avatarUrl;
    }

    public void updateCurrentAppUserInfo(User user){
        currentAppUser = user;
        setCurrentAppUserNick(user.getMUserNick());
        setCurrentAppUserAvatar(user.getAvatar());
        SuperWeChatHelper.getInstance().saveAppContact(user);
    }

    public void asyncGetCurrentAppUserInfo() {
        userModel.loadUserInfo(appContext, EMClient.getInstance().getCurrentUser(),
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (s!=null){
                            Result result = ResultUtils.getResultFromJson(s, User.class);
                            if (result!=null && result.isRetMsg()){
                                User user = (User) result.getRetData();
//								L.e(TAG,"asyncGetCurrentAppUserInfo,user="+user);
                                if (user!=null){
                                    updateCurrentAppUserInfo(user);
//									currentAppUser = user;
//									setCurrentAppUserNick(user.getMUserNick());
//									setCurrentAppUserAvatar(user.getAvatar());
//									SuperWeChatHelper.getInstance().saveAppContact(user);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    public void asyncGetCurrentUserInfo() {
        ParseManager.getInstance().asyncGetCurrentUserInfo(new EMValueCallBack<EaseUser>() {

            @Override
            public void onSuccess(EaseUser value) {
                if(value != null){
                    setCurrentUserNick(value.getNick());
                    setCurrentUserAvatar(value.getAvatar());
                }
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });

    }
    public void asyncGetUserInfo(final String username,final EMValueCallBack<EaseUser> callback){
        ParseManager.getInstance().asyncGetUserInfo(username, callback);
    }
    private void setCurrentUserNick(String nickname) {
        getCurrentUserInfo().setNick(nickname);
        PreferenceManager.getInstance().setCurrentUserNick(nickname);
    }

    private void setCurrentUserAvatar(String avatar) {
        getCurrentUserInfo().setAvatar(avatar);
        PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
    }

    private void setCurrentAppUserNick(String nickname){
        getCurrentAppUserInfo().setMUserNick(nickname);
        PreferenceManager.getInstance().setCurrentUserNick(nickname);
    }

    private void setCurrentAppUserAvatar(String avatar){
        L.e(TAG,"setCurrentAppUserAvatar,avatar="+avatar);
        getCurrentAppUserInfo().setAvatar(avatar);
        PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
    }

    private String getCurrentUserNick() {
        return PreferenceManager.getInstance().getCurrentUserNick();
    }

    private String getCurrentUserAvatar() {
        return PreferenceManager.getInstance().getCurrentUserAvatar();
    }

}