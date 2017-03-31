package cn.ucai.superwechat.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.redpacketui.utils.RedPacketUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    @BindView(R.id.iv_profile_avatar)
    ImageView mIvProfileAvatar;
    @BindView(R.id.tv_profile_nickname)
    TextView mTvProfileNickname;
    @BindView(R.id.tv_profile_username)
    TextView mTvProfileUsername;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prifile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        String username = EMClient.getInstance().getCurrentUser();
        mTvProfileUsername.setText(username);
        EaseUserUtils.setAppUserNick(username,mTvProfileNickname);
        EaseUserUtils.setAppUserAvatar(getContext(),username,mIvProfileAvatar);

    }

    @OnClick(R.id.tv_profile_money)
    public void money() {
        RedPacketUtil.startChangeActivity(getActivity());
    }

    @OnClick(R.id.iv_profile_avatar)
    public void avatar() {
        startActivity(new Intent(getActivity(), UserProfileActivity.class).putExtra("setting", true)
                .putExtra("username", EMClient.getInstance().getCurrentUser()));

    }

    @OnClick(R.id.tv_profile_settings)
    public void settings() {

    }



}
