package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.annotation.HASetListener;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.net.NetRequestListener;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.util.IntentHelper;

@HALayout(layout = R.layout.activity_login)
public class LoginActivity extends BaseTitleActivity implements
		NetRequestListener {

	// 登录名
	@HAFindView(Id = R.id.login_tel)
	private EditText mPhone;
	// 密码
	@HAFindView(Id = R.id.login_pwd)
	private EditText mPwd;

	// 注册
	@HASetListener(Id = R.id.login_regist, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mRegister;
	// 登录
	@HASetListener(Id = R.id.login_login, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mLogin;


	@Override
	protected void init() {
		super.init();
		setTitle("登录");
		setRightButton(0, View.GONE);
	}
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
	public void onClick(View v) {
		super.onClick(v);
		int v_id = v.getId();
		switch (v_id) {
		case R.id.login_regist:
			IntentHelper.startIntent2Activity(this, APPConfig.A_register);
			break;
		case R.id.login_login:
			String v_tel = mPhone.getText().toString().trim();
			if (CommHelper.checkNull(v_tel)) {
				DialogHelper.showToast(this, "请输入正确的用户名！", 2);
				return;
			}
			String mPwdStr = mPwd.getText().toString().trim();
			if (CommHelper.checkNull(mPwdStr)) {
				DialogHelper.showToast(this, "请输入密码！", 2);
				return;
			}
			CommHelper.hideKeyBord(this, mPhone);
			login(v_tel, mPwdStr);
			break;
		}
	}

	private void login(String userName, String pass) {
		this.showNetDialog();
		APPNet.login(userName, pass, this);
	}

	@Override
	public void onSuccess(String url, JSONArray data, int total) {
		if (url.equals(APPConfig.I_LOGIN)) {
			disMissDialog();
			JSONObject o = data.optJSONObject(0);
			APPHelper.saveUserInfo(mPhone.getText().toString().trim(), mPwd
					.getText().toString().trim(), o.optString("email"),
					o.optString("nickName"), o.optString("portraitUrl"),"1");
			DialogHelper.createHintDialog(this, "提示", "登录成功!", "确定", null,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							LoginActivity.this.finish();
						}

					}, null);

		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Bundle b = intent.getExtras();
		if(b!=null){
			String loginOK=b.getString("loginOK");
			if("1".equals(loginOK)){
				this.finish();
			}else{
				mPhone.setText(b.getString("userName"));
			}
		}
	}

	@Override
	public void onFail(String url, String code, String msg) {
		disMissDialog();
		DialogHelper.showNote(this, msg);
	}
}
