package com.cdhz.cdhz_1.fragments;

import android.os.Bundle;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdhz.cdhz_1.APPConfig;
import com.cdhz.cdhz_1.APPHelper;
import com.cdhz.cdhz_1.R;
import com.cdhz.cdhz_1.views.AdWidget;
import com.cdhz.cdhz_1.views.AdWidget.OnItemClickListener;
import com.hyc.androidcore.BaseFragment;
import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.annotation.HFFindView;
import com.hyc.androidcore.annotation.HFSetListener;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.util.IntentHelper;

public class HomeFragment extends BaseFragment implements OnClickListener,
		OnItemClickListener, android.widget.AdapterView.OnItemClickListener {

	@HFFindView(Id = R.id.left)
	private ImageView mBack;
	// 标题
	@HFFindView(Id = R.id.title)
	private TextView mTitle;
	// 菜单切换按钮
	@HFSetListener(Id = R.id.right, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private ImageView mUser;

	// 近期会展
	@HFSetListener(Id = R.id.home_huizhan, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mHuiZhan;

	// 新闻资讯
	@HFSetListener(Id = R.id.home_zixun, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mZiXun;

	// 会展服务
	@HFSetListener(Id = R.id.home_huizhanfuwu, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mHuiZhanFuwu;

	// 展馆
	@HFSetListener(Id = R.id.home_zhanguan, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mZhanguan;

	// 会展报告
	@HFSetListener(Id = R.id.home_baogao, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mBaogao;

	// 年展计划
	@HFSetListener(Id = R.id.home_nianzhan, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mNianZhan;

	// 个人登录
	@HFSetListener(Id = R.id.home_login, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mLogIn;

	// 个人登录
	@HFFindView(Id = R.id.home_lgoin_text)
	private TextView mLoginText;

	@HFFindView(Id = R.id.home_ad_bulletin)
	private AdWidget mAd;

	private JSONArray adData;

	public HomeFragment() {

	}

	public void initView(JSONArray data, JSONArray adData, JSONArray nianzhan) {
		HandlerHelper.getInstance().sendMessage(true, 100,
				APPConfig.H_HOME_REFRESH);
		this.adData = adData;
		JSONArray dat = new JSONArray();
		for (int i = 0; i < adData.length(); i++) {
			dat.put(adData.optJSONObject(i).optString("picUrl"));
		}
		mAd.setData(dat, R.drawable.dot_1, R.drawable.dot_2, R.drawable.ad_bg);
		mAd.setOnItemClickListener(this);
		HandlerHelper.getInstance().sendMessage(true, 8000, APPConfig.H_MAIN_AD);
	}

	public void initLoginView() {
		if (APPHelper.islogin()) {
			mLoginText.setText("我");
		} else {
			mLoginText.setText("个人登录");
		}
	}

	@Override
	public View getView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.fragment_home, null);
	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
		if (msg.what == APPConfig.H_MAIN_AD) {
			int id = mAd.getSelectId();
			if (id < mAd.getViewPagerChildNum()-1) {
				id++;
				mAd.setSelectId(id);
			}else{
				mAd.setSelectId(0);
			}
			HandlerHelper.getInstance().sendMessage(true, 8000, APPConfig.H_MAIN_AD);
		}
	}

	@Override
	public void init() {
		mBack.setVisibility(View.GONE);
		mUser.setVisibility(View.GONE);
		mTitle.setText("成都会展");
		Display v_display = mActivity.getWindowManager().getDefaultDisplay();
		int v_adW = v_display.getWidth();
		int v_adH = v_display.getHeight()-CommHelper.dip2px(mActivity, 456);
		if(v_adH<200){
			v_adH=200;
		}
		mAd.setLayoutParams(new LinearLayout.LayoutParams(v_adW, v_adH));

		JSONArray dat = new JSONArray();
		mAd.setData(dat, R.drawable.dot_1, R.drawable.dot_2, R.drawable.ad_bg);
		mAd.setOnItemClickListener(this);
	}
    final Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f,0.1f,1.0f,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,0.5f);
	@Override
	public void onClick(View v) {
		int id = v.getId();
        v.startAnimation(scaleAnimation);
		switch (id) {
		case R.id.home_huizhan:
			// 近期会展
			HandlerHelper.getInstance().sendMessage(true, 0,
					APPConfig.H_goto_HuiZhan);
			break;
		case R.id.home_zixun:
			// 新闻资讯
			Bundle bb = new Bundle();
			bb.putInt("type", 2);
			bb.putString("id", "");
			bb.putString("title", "新闻资讯");
			IntentHelper.startIntent2Activity(mActivity, APPConfig.A_zixun, bb);
			break;
		case R.id.home_huizhanfuwu:
			// 会展服务
			Bundle cc = new Bundle();
			cc.putInt("type", 3);
			cc.putString("id", "");
			cc.putString("title", "会展服务");
			IntentHelper.startIntent2Activity(mActivity, APPConfig.A_zixun, cc);

			break;
		case R.id.home_zhanguan:
			// 展馆
			HandlerHelper.getInstance().sendMessage(true, 0,
					APPConfig.H_GOTO_ALL_ZHANGUAN);
			break;
		case R.id.home_baogao:
			CommHelper.showToastLong(mActivity, "业务开发中，尽请期待！");
			break;
		case R.id.home_nianzhan:
			// 年展
			HandlerHelper.getInstance().sendMessage(true, 10,
					APPConfig.H_goto_YearPlan);
			break;
		case R.id.home_login:
			// 登录
			if (!APPHelper.islogin()) {
				IntentHelper.startIntent2Activity(mActivity, APPConfig.A_login);
			} else {
				IntentHelper.startIntent2Activity(mActivity,
						APPConfig.A_userinfo);
			}
			break;
		}

		// if (v.getId() == R.id.home_zhanguan) {
		// HandlerHelper.getInstance().sendMessage(true, 10,
		// APPConfig.H_GOTO_ALL_ZHANGUAN);
		// } else
		// if (v.getId() == R.id.home_plan) {
		// HandlerHelper.getInstance().sendMessage(true, 10,
		// APPConfig.H_goto_YearPlan);
		// }
	}

	@Override
	public void onItemClick(int id, String obj) {
		JSONObject o = this.adData.optJSONObject(id);
		Bundle bundler = new Bundle();
		bundler.putString("data", o.toString());
		IntentHelper.startIntent2Activity(mActivity, APPConfig.A_huizhandetail,
				bundler);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}
}
