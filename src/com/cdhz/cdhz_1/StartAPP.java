package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;

import com.hyc.androidcore.BaseActivity;
import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.db.DBHelper;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.log.DefalutLogger;
import com.hyc.androidcore.net.NetHelper;
import com.hyc.androidcore.net.NetRequestListener;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.util.IntentHelper;

@HALayout(layout = R.layout.activity_start)
public class StartAPP extends BaseActivity implements NetRequestListener {

	private JSONArray adData;
	private JSONArray zhanguanData;
	private JSONArray nianzhanData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		HandlerHelper.getInstance().start(false);
		DefalutLogger.getInstance().setLoggerLevel(DefalutLogger.ALL);
		NetHelper.getInstance().init(this, 0, null, APPConfig.BASE_URL);
		NetHelper.getInstance().setInitSession(false);

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init() {
		super.init();
		DBHelper.getInstance().openDB();
		updateInfo();
	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
		if (msg.what == APPConfig.H_CHECK_DATA) {
			checkData();
		}
	}

	private String userName;
	private String pass;

	private void checkData() {
		if (adData == null) {
			HandlerHelper.getInstance().sendMessage(true, 2000,
					APPConfig.H_CHECK_DATA);
			return;
		}
		if (zhanguanData == null) {
			HandlerHelper.getInstance().sendMessage(true, 2000,
					APPConfig.H_CHECK_DATA);
			return;
		}
		if (nianzhanData == null) {
			HandlerHelper.getInstance().sendMessage(true, 2000,
					APPConfig.H_CHECK_DATA);
			return;
		}
		// 处理自动登陆
		JSONObject userInfo = APPHelper.getSaveUserInfo();
		if (userInfo != null) {
			userName = userInfo.optString("userName");
			pass = userInfo.optString("pass");
			if (!CommHelper.checkNull(userName) && !CommHelper.checkNull(pass)) {
				APPNet.login(userName, pass, this);
				return;
			}
		}
		gotoMain();
	}

	private void gotoMain() {
		disMiss();
		DBHelper.getInstance().closeDB();
		Bundle bundle = new Bundle();
		bundle.putString("nianzhan", nianzhanData.toString());
		bundle.putString("ad", adData.toString());
		bundle.putString("remenZhanGuan", zhanguanData.toString());
		IntentHelper.startIntent2Activity(this, APPConfig.A_main, bundle);
		this.finish();
	}

	public void updateInfo() {
		// 获取热门展馆
		showNetDialog();
		DBHelper.getInstance().openDB();
		APPNet.getHalls(true, 0, 10, this);
		APPNet.getExhs("DESC", null, null, null, 0, 5, this);
		APPNet.getExhPlans(0, 5, this);
		checkData();
	}

	@Override
	public void onSuccess(String url, JSONArray data, int total) {
		if (url.equals(APPConfig.I_getHalls)) {
			// 获取热门展馆成功
			zhanguanData = data;
			DBHelper.getInstance().insertOrUpdateById(
					new Propertity("zhanguan", data.toString()), "zhanguan");
		} else if (url.startsWith(APPConfig.I_getExhs)) {
			if (data.length() > 5) {
				adData = new JSONArray();
				for (int i = 0; i < 5; i++) {
					adData.put(data.optJSONObject(i));
				}
			} else {
				adData = data;
			}

			DBHelper.getInstance().insertOrUpdateById(
					new Propertity("ad", data.toString()), "ad");
		} else if (url.equals(APPConfig.I_LOGIN)) {
			JSONObject o = data.optJSONObject(0);
			APPHelper.saveUserInfo(userName, pass, o.optString("email"),
					o.optString("nickName"), o.optString("portraitUrl"), "1");
			gotoMain();
		} else if (url.equals(APPConfig.I_getExhPlans)) {
			if (data.length() > 5) {
				nianzhanData = new JSONArray();
				for (int i = 0; i < 5; i++) {
					nianzhanData.put(data.optJSONObject(i));
				}
			} else {
				nianzhanData = data;
			}
			DBHelper.getInstance().insertOrUpdateById(
					new Propertity("nianzhan", data.toString()), "nianzhan");
		}
	}

	@Override
	public void onFail(String url, String code, String msg) {
		if (url.equals(APPConfig.I_getHalls)) {
			// 获取热门展馆失败
			try {
				Propertity p = DBHelper.getInstance().queryOne(
						Propertity.class, "zhanguan");
				zhanguanData = new JSONArray(p.getValue());
			} catch (Exception e) {
				showDialog();
			}
		} else if (url.equals(APPConfig.I_LOGIN)) {
			gotoMain();
		} else if (url.equals(APPConfig.I_getExhPlans)) {
			try {
				Propertity p = DBHelper.getInstance().queryOne(
						Propertity.class, "nianzhan");
				nianzhanData = new JSONArray(p.getValue());
			} catch (Exception e) {
				showDialog();
			}
		}

		else {
			try {
				Propertity p = DBHelper.getInstance().queryOne(
						Propertity.class, "ad");
				adData = new JSONArray(p.getValue());
			} catch (Exception e) {
				showDialog();
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		DialogHelper.showShutDownAPpDialog(this);
	}

	private Dialog mDialog;

	private void disMiss() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	private void showNetDialog() {
		disMiss();
		mDialog = DialogHelper.createNetConectingDialog(this, false, "初始化中");
	}

	private void showDialog() {
		disMiss();
		mDialog = DialogHelper.createHintDialog(this, "提示", "初始化失败，请重试！", "重试",
				"退出", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						updateInfo();

					}
				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						IntentHelper.ShutDownAPP();

					}
				});
	}

}
