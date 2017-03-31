package cn.ucai.superwechat.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    }
}
