package com.cdhz.cdhz_1;

import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.cdhz.cdhz_1.fragments.HomeFragment;
import com.cdhz.cdhz_1.fragments.HuiZhanFragment;
import com.cdhz.cdhz_1.fragments.WoDeFragment;
import com.cdhz.cdhz_1.fragments.ZhanGuanFragment;
import com.hyc.androidcore.BaseFragmentActivity;
import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.annotation.BundleType;
import com.hyc.androidcore.annotation.HABundle;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HAInstanceState;
import com.hyc.androidcore.annotation.HASetListener;
import com.hyc.androidcore.cache.GlobalDataHelper;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.util.IntentHelper;

public class Main extends BaseFragmentActivity implements OnClickListener,
		AMapLocationListener {

	private HomeFragment mHome;
	private HuiZhanFragment mHuiZhan;
	private ZhanGuanFragment mZhanGuan;
	private WoDeFragment mWode;

	@HASetListener(Id = R.id.main_bottom_0, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mHomeFlag;
	@HASetListener(Id = R.id.main_bottom_1, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mHuiZhanFlag;
	@HASetListener(Id = R.id.main_bottom_2, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mZhanGuanFlag;
	@HASetListener(Id = R.id.main_bottom_3, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mWodeFlag;

	@HAFindView(Id = R.id.type_0)
	private ImageView mType0;

	@HAFindView(Id = R.id.type_1)
	private ImageView mType1;

	@HAFindView(Id = R.id.type_2)
	private ImageView mType2;

	@HAFindView(Id = R.id.type_3)
	private ImageView mType3;
	// 热门展馆数据
	@HAInstanceState(name = "remenZhanGuan", type = BundleType.JSONARRAY)
	@HABundle(name = "remenZhanGuan", type = BundleType.JSONARRAY)
	private JSONArray remenZhanGuan;

	@HAInstanceState(name = "nianzhan", type = BundleType.JSONARRAY)
	@HABundle(name = "nianzhan", type = BundleType.JSONARRAY)
	private JSONArray nianzhan;

	@HAInstanceState(name = "ad", type = BundleType.JSONARRAY)
	@HABundle(name = "ad", type = BundleType.JSONARRAY)
	private JSONArray ad;
	// 定位相关
	private LocationManagerProxy aMapLocManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mHome = new HomeFragment();
		mHuiZhan = new HuiZhanFragment();
		mZhanGuan = new ZhanGuanFragment();
		mWode = new WoDeFragment();
		addFragment(mHome, mHuiZhan, mZhanGuan, mWode);
		HandlerHelper.getInstance().sendMessage(true, 100,
				APPConfig.H_MAIN_REFRESH, 0, 0);

	}

	private void addFragment(HomeFragment home, HuiZhanFragment huizhan,
			ZhanGuanFragment zhanguan, WoDeFragment wode) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		if (fragmentManager.getBackStackEntryCount() > 0) {
			return;
		}
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.addToBackStack("home");
		fragmentTransaction.add(R.id.main_fragmentcontainer, home, "home");

		fragmentTransaction.addToBackStack("huizhan");
		fragmentTransaction
				.add(R.id.main_fragmentcontainer, huizhan, "huizhan");
		fragmentTransaction.addToBackStack("zhanguan");
		fragmentTransaction.add(R.id.main_fragmentcontainer, zhanguan,
				"zhanguan");
		fragmentTransaction.addToBackStack("wode");
		fragmentTransaction.add(R.id.main_fragmentcontainer, wode, "wode");
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.main_bottom_0:
			setBottomFocus(0);
			break;
		case R.id.main_bottom_1:
			setBottomFocus(1);
			break;
		case R.id.main_bottom_2:
			setBottomFocus(2);
			break;
		case R.id.main_bottom_3:
			setBottomFocus(3);
		}

	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
		if (msg.what == APPConfig.H_MAIN_REFRESH) {
			setBottomFocus(0);
			this.mHome.initView(remenZhanGuan, ad, nianzhan);
		} else if (msg.what == APPConfig.H_GOTO_ALL_ZHANGUAN) {
			setBottomFocus(2);
		} else if (msg.what == APPConfig.H_goto_HuiZhan) {
			setBottomFocus(1);
		}

		else if (msg.what == APPConfig.H_goto_YearPlan) {
			// 进入年展计划
			IntentHelper.startIntent2Activity(this, APPConfig.A_nianzhanplan);
		}
	}

	private void setVisible(String fragmentString, boolean show) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		Fragment fragment = fragmentManager.findFragmentByTag(fragmentString);
		if (fragment == null) {
			return;
		}
		if (show) {
			fragmentTransaction.show(fragment);
		} else {
			fragmentTransaction.hide(fragment);
		}
		fragmentTransaction.commitAllowingStateLoss();
	}

	private void setBottomFocus(int id) {
		if (id == 0) {
			mType0.setImageResource(R.drawable.main_shouye_);
			mType1.setImageResource(R.drawable.main_huizhan);
			mType2.setImageResource(R.drawable.main_zhanguang);
			mType3.setImageResource(R.drawable.main_wode);
			setVisible("home", true);
			setVisible("huizhan", false);
			setVisible("zhanguan", false);
			setVisible("wode", false);
		} else if (id == 1) {
			mType0.setImageResource(R.drawable.main_shouye);
			mType1.setImageResource(R.drawable.main_huizhan_);
			mType2.setImageResource(R.drawable.main_zhanguang);
			mType3.setImageResource(R.drawable.main_wode);
			setVisible("home", false);
			setVisible("huizhan", true);
			setVisible("zhanguan", false);
			setVisible("wode", false);
		} else if (id == 2) {
			mType0.setImageResource(R.drawable.main_shouye);
			mType1.setImageResource(R.drawable.main_huizhan);
			mType2.setImageResource(R.drawable.main_zhanguang_);
			mType3.setImageResource(R.drawable.main_wode);
			setVisible("home", false);
			setVisible("huizhan", false);
			setVisible("zhanguan", true);
			setVisible("wode", false);
		} else if (id == 3) {
			mType0.setImageResource(R.drawable.main_shouye);
			mType1.setImageResource(R.drawable.main_huizhan);
			mType2.setImageResource(R.drawable.main_zhanguang);
			mType3.setImageResource(R.drawable.main_wode_);
			setVisible("home", false);
			setVisible("huizhan", false);
			setVisible("zhanguan", false);
			setVisible("wode", true);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			DialogHelper.showShutDownAPpDialog(this);
			stopLocation();
			return false;
		}
		return true;
	}

	@Override
	protected void onPause() {
		stopLocation();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		startLocation();
		if (this.mHome != null) {
			this.mHome.initLoginView();
		}
	}

	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destroy();
		}
		aMapLocManager = null;
	}

	@Override
	protected void onDestroy() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.remove(mHome);
		fragmentTransaction.remove(mHuiZhan);
		fragmentTransaction.remove(mZhanGuan);
		fragmentTransaction.remove(mWode);
		fragmentTransaction.commit();
		super.onDestroy();
	}

	/***
	 * 开始定位
	 */
	private void startLocation() {
		if (GlobalDataHelper.getInstance().containKey(
				APPConfig.USER_POSITION_geoLat)) {
			return;
		}
		aMapLocManager = LocationManagerProxy.getInstance(this);
		aMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork,
				2000, 10, this);
		HandlerHelper.getInstance().getMainThreadHandler()
				.postDelayed(new Runnable() {

					@Override
					public void run() {
						stopLocation();

					}
				}, 20000);
	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			GlobalDataHelper.getInstance().put(APPConfig.USER_POSITION_geoLat,
					(long) (geoLat * 100000000));
			GlobalDataHelper.getInstance().put(APPConfig.USER_POSITION_geoLng,
					(long) (geoLng * 100000000));
			stopLocation();
		}
	}
}
