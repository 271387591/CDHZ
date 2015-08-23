package com.cdhz.cdhz_1.fragments;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdhz.cdhz_1.APPConfig;
import com.cdhz.cdhz_1.APPHelper;
import com.cdhz.cdhz_1.R;
import com.hyc.androidcore.BaseFragment;
import com.hyc.androidcore.annotation.HFFindView;
import com.hyc.androidcore.annotation.HFSetListener;
import com.hyc.androidcore.net.NetHelper;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.util.IntentHelper;

public class WoDeFragment extends BaseFragment implements OnClickListener {

	@HFFindView(Id = R.id.left)
	private ImageView mBack;
	// 标题
	@HFFindView(Id = R.id.title)
	private TextView mTitle;
	// 菜单切换按钮
	@HFSetListener(Id = R.id.right, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private ImageView mUser;

	@HFSetListener(Id = R.id.wode_userInfo, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mUserInfo;

	@HFSetListener(Id = R.id.wode_about, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mAbout;

	public WoDeFragment() {

	}

	@Override
	public View getView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.fragment_wode, null);
	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
	}

	@Override
	public void init() {
		mBack.setVisibility(View.GONE);
		mTitle.setText("我的");
		mUser.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.wode_about:
			IntentHelper.startIntent2Activity(mActivity, APPConfig.A_about);
			break;

		case R.id.wode_userInfo:
			if (!APPHelper.islogin()) {
				IntentHelper.startIntent2Activity(mActivity, APPConfig.A_login);
			} else {
				IntentHelper.startIntent2Activity(mActivity,
						APPConfig.A_userinfo);

			}
			break;
		}

	}
}
