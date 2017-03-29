package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.ui.activity.BoutiqueChileActivity;
import cn.ucai.fulicenter.ui.activity.CategoryChileActivity;
import cn.ucai.fulicenter.ui.activity.CollectsActivity;
import cn.ucai.fulicenter.ui.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.ui.activity.LoginActivity;
import cn.ucai.fulicenter.ui.activity.MainActivity;
import cn.ucai.fulicenter.ui.activity.OrderActivity;
import cn.ucai.fulicenter.ui.activity.RegisterActivity;
import cn.ucai.fulicenter.ui.activity.SettingActivity;
import cn.ucai.fulicenter.ui.activity.UpdateNickActivity;

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

    public static void gotoBoutiqueChild(Context activity, BoutiqueBean bean) {
         activity.startActivity(new Intent(activity, BoutiqueChileActivity.class)
        .putExtra(I.NewAndBoutiqueGoods.CAT_ID,bean.getId())
        .putExtra(I.Boutique.TITLE,bean.getTitle()));

    }



    public static void gotoDetails(Context activity, int goodsId) {
       startActivityForResult((Activity) activity,new Intent(activity, GoodsDetailsActivity.class)
        .putExtra(I.Goods.KEY_GOODS_ID,goodsId),I.REQUEST_CODE_COLLECT);
    }

    public static void gotoCategoryChild(Context activity, int catId, String groupName,
                                         ArrayList<CategoryChildBean> list) {
        activity.startActivity(new Intent(activity, CategoryChileActivity.class)
        .putExtra(I.NewAndBoutiqueGoods.CAT_ID,catId)
        .putExtra(I.CategoryGroup.NAME,groupName)
        .putExtra(I.CategoryChild.DATA,list));
    }

    public static void gotoLogin(Activity activity,int requestCode) {
        startActivityForResult(activity, new Intent(activity,LoginActivity.class),requestCode);
    }

    public static void gotoRegister(Activity activity) {
        startActivityForResult(activity,new Intent(activity, RegisterActivity.class),
                I.REQUEST_CODE_REGISTER);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent,requestCode);
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }


    public static void gotoSettings(Activity activity) {
        starActivity(activity,SettingActivity.class);

    }

    public static void gotoUpdateNick(Activity activity) {
        starActivity(activity, UpdateNickActivity.class);
    }

    public static void gotoCollectsList(Activity activity) {
        starActivity(activity, CollectsActivity.class);
    }


    public static void gotoOrder(Activity activity, int price) {
       startActivityForResult(activity,new Intent(activity,OrderActivity.class)
       .putExtra(I.ORDER_BUY_PRICE,price),I.REQUEST_CODE_ORDER);
    }
}
