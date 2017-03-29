package cn.ucai.superwechat.db;

import android.content.Context;

import cn.ucai.superwechat.utils.OkHttpUtils;

/**
 * Created by liuning on 2017/3/29.
 */

public interface IUserModel {
    void register(Context context, String username, String nickname, String password,
                  OkHttpUtils.OnCompleteListener<String> listener);

    void login(Context context, String username, String password,
               OkHttpUtils.OnCompleteListener<String> listener);

    void unregister(Context context, String username,
                    OkHttpUtils.OnCompleteListener<String> listener);

    void loadUserInfo(Context context,String username,OnCompleteListener<String> listener);

}
