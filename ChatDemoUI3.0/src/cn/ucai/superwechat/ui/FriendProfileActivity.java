package cn.ucai.superwechat.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.db.IUserModel;
import cn.ucai.superwechat.db.InviteMessgeDao;
import cn.ucai.superwechat.db.OnCompleteListener;
import cn.ucai.superwechat.db.UserModel;
import cn.ucai.superwechat.domain.InviteMessage;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.Result;
import cn.ucai.superwechat.utils.ResultUtils;

/**
 * Created by clawpo on 2017/4/5.
 */
public class FriendProfileActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.tv_userinfo_nick)
    TextView mTvUserinfoNick;
    @BindView(R.id.tv_userinfo_name)
    TextView mTvUserinfoName;
    @BindView(R.id.btn_add_contact)
    Button mBtnAddContact;
    @BindView(R.id.btn_send_msg)
    Button mBtnSendMsg;
    @BindView(R.id.btn_send_video)
    Button mBtnSendVideo;
    User user = null;

    IUserModel model;
    InviteMessage msg;
    boolean isFriend = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_firend_profile);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFGT.finish(FriendProfileActivity.this);
            }
        });
    }

    private void initData() {
        model = new UserModel();
        user = (User) getIntent().getSerializableExtra(I.User.TABLE_NAME);
        if (user!=null){
            showUserInfo();
        }else{
            msg  = (InviteMessage) getIntent().getSerializableExtra(I.User.NICK);
            if (msg!=null){
                user = new User(msg.getFrom());
                user.setMUserNick(msg.getNickname());
                user.setAvatar(msg.getAvatar());
                showUserInfo();
            }else {
                MFGT.finish(FriendProfileActivity.this);
            }
        }
    }

    private void showUserInfo() {
        isFriend = SuperWeChatHelper.getInstance().getAppContactList().containsKey(user.getMUserName());
        if (isFriend){
            SuperWeChatHelper.getInstance().saveAppContact(user);
        }
        mTvUserinfoName.setText(user.getMUserName());
        EaseUserUtils.setAppUserAvatar(FriendProfileActivity.this,user,mProfileImage);
        EaseUserUtils.setAppUserNick(user,mTvUserinfoNick);
        showFirend(isFriend);
        syncUserInfo();
    }

    private void showFirend(boolean isFirend){
        mBtnAddContact.setVisibility(isFirend?View.GONE:View.VISIBLE);
        mBtnSendMsg.setVisibility(isFirend?View.VISIBLE:View.GONE);
        mBtnSendVideo.setVisibility(isFirend?View.VISIBLE:View.GONE);
    }

    @OnClick(R.id.btn_add_contact)
    public void addContact(){
        boolean isConfirm = true;
        if (isConfirm){
            //发送验证消息
            MFGT.gotoSendAddFirend(FriendProfileActivity.this,user.getMUserName());
        }else{
            //直接添加为好友
        }
    }

    @OnClick(R.id.btn_send_msg)
    public void sendMsg() {
        finish();
        MFGT.gotoChat(FriendProfileActivity.this,user.getMUserName());
    }

    private void syncUserInfo() {
        //从服务器异步加载用户的最新信息,填充到好友列表或者新的朋友列表
        model.loadUserInfo(FriendProfileActivity.this, user.getMUserName(),
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (s != null) {
                            Result result = ResultUtils.getResultFromJson(s, User.class);
                            if (result != null && result.isRetMsg()) {
                                User u = (User) result.getRetData();
                                if (u != null) {
                                    if (msg != null) {
                                        //update msg
                                        ContentValues values = new ContentValues();
                                        values.put(InviteMessgeDao.COLUMN_NAME_NICK, u.getMUserNick());
                                        values.put(InviteMessgeDao.COLUMN_NAME_AVATAR, u.getAvatar());
                                        InviteMessgeDao dao = new InviteMessgeDao(FriendProfileActivity.this);
                                        dao.updateMessage(msg.getId(), values);
                                    } else if (isFriend) {
                                        //update user
                                        SuperWeChatHelper.getInstance().saveAppContact(u);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
}