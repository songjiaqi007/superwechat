package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by liuning on 2017/3/29.
 */

public class GuideActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
    }

    private static final String TAG = "GuideActivity";

    @OnClick({R.id.btnLogin, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Log.i(TAG, "dianjilogin");
                MFGT.gotoLogin(GuideActivity.this);
                break;
            case R.id.btnRegister:
                MFGT.gotoRegister(GuideActivity.this);
                break;
        }
    }
}
