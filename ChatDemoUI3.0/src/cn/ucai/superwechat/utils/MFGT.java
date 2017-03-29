package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Intent;

import cn.ucai.superwechat.R;
import cn.ucai.superwechat.ui.GuideActivity;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.MainActivity;
import cn.ucai.superwechat.ui.RegisterActivity;

/**
 * Created by liuning on 2017/3/16.
 */

public class MFGT {
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void starActivity(Activity activity, Class cls) {
        activity.startActivity(new Intent(activity,cls));
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    public static void statActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    public static void gotoMain(Activity activity) {
        starActivity(activity, MainActivity.class);
    }

    public static void gotoGuide(Activity activity) {
       starActivity(activity, GuideActivity.class);
    }

    public static void gotoLogin(Activity activity) {
        starActivity(activity, LoginActivity.class);
    }

    public static void gotoRegister(Activity activity) {
        starActivity(activity, RegisterActivity.class);

    }
}