package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.annotation.HASetListener;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.net.NetRequestListener;
import com.hyc.androidcore.util.IntentHelper;

@HALayout(layout = R.layout.activity_about)
public class AboutActivity extends BaseTitleActivity implements
		NetRequestListener {

	@HASetListener(Id = R.id.checkVersion, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View cheVersion;

	private Dialog mDialog;

	public void showNetDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		mDialog = DialogHelper.createNetConectingDialog(this, false, "请求数据中");
		mDialog.show();
	}

	public void disMissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	@Override
	protected void init() {
		super.init();
		setRightButton(0, View.GONE);
		setTitle("关于");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.checkVersion) {
			showNetDialog();
			APPNet.checkVerson(this);
		}
	}

	@Override
	public void onSuccess(String url, JSONArray data, int total) {
		disMissDialog();
		if (url.startsWith(APPConfig.I_getLastVersion)) {
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "当前版本已是最新版本！", 1);
				return;
			}
			JSONObject o = data.optJSONObject(0);

			if (!isUpdate(o.optString("version"))) {
				DialogHelper.showToast(this, "当前版本已是最新版本！", 1);
				return;
			}
			Bundle b = new Bundle();
			b.putString("appurl", o.optString("pacUrl"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_updateAPP);
		}
	}

	private boolean isUpdate(String versionStr) {
		int version=0;
		try {
			String versiona[]=versionStr.split(".");
			for(int i=versiona.length-1;i>0;i--){
				version+=Integer.parseInt(versiona[i])*(100*i+1);
			}
		} catch (Exception e) {
			version = 0;
		}
		int nativeVersion=0;
		try {
			String versiona[]=getString(R.string.app_version).split(".");
			for(int i=versiona.length-1;i>0;i--){
				nativeVersion+=Integer.parseInt(versiona[i])*(100*i+1);
			}
		} catch (Exception e) {
			nativeVersion = 0;
		}
		return version>nativeVersion;

	}

	@Override
	public void onFail(String url, String code, String msg) {
		disMissDialog();
		if (url.startsWith(APPConfig.I_getLastVersion)) {
			DialogHelper.showToast(this, "获取最新版本失败！", 2);
			return;
		}
	}

}
