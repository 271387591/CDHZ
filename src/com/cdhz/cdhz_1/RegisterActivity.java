package com.cdhz.cdhz_1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.net.NetRequestListener;
import com.hyc.androidcore.util.IntentHelper;

/***
 * 注册
 * 
 * @author Administrator
 *
 */
@HALayout(layout = R.layout.activity_register)
public class RegisterActivity extends BaseTitleActivity implements
		NetRequestListener {

	@HAFindView(Id = R.id.login_tel)
	private EditText mUserName;
	@HAFindView(Id = R.id.login_mail)
	private EditText mMail;
	@HAFindView(Id = R.id.login_nickname)
	private EditText mNickName;
	@HAFindView(Id = R.id.login_pwd1)
	private EditText mPass1;
	@HAFindView(Id = R.id.login_pwd2)
	private EditText mPass2;

	@HAFindView(Id = R.id.login_regist)
	private View mRegisterbutton;
	private Dialog mDialog;

	@Override
	protected void init() {
		super.init();
		setTitle("注册");
		setRightButton(0, View.GONE);
		mRegisterbutton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.login_regist) {
			register();
		}
	}

	private void register() {
		String userName = mUserName.getText().toString();
		if (userName.trim().length() == 0) {
			DialogHelper.showToast(this, "请输入用户名！", 2);
			return;
		}
		String mail = mMail.getText().toString();
		if (mail.trim().length() == 0) {
			DialogHelper.showToast(this, "请输入邮箱！", 2);
			return;
		}
		if (!checkMail(mail)) {
			DialogHelper.showToast(this, "请输入正确的邮箱！", 2);
			return;
		}
		String nickName = mNickName.getText().toString();
		if (nickName.trim().length() == 0) {
			DialogHelper.showToast(this, "请输入昵称！", 2);
			return;
		}
		String pass1 = mPass1.getText().toString().trim();
		if (pass1.length() < 6) {
			DialogHelper.showToast(this, "请输入密码！", 2);
			return;
		}
		String pass2 = mPass2.getText().toString().trim();
		if (!pass1.equals(pass2)) {
			DialogHelper.showToast(this, "两次输入密码不一致！", 2);
			return;
		}
		showNetDialog();
		APPNet.register(userName, mail, nickName, pass1, this);

	}

	private boolean checkMail(String mail) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(mail);
		return matcher.matches();
	}

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
	public void onSuccess(String url, JSONArray data, int total) {
		if (url.startsWith(APPConfig.I_register)) {
			APPHelper.saveUserInfo(mUserName.getText().toString(), mPass1
					.getText().toString().trim(), mMail.getText().toString(),
					mNickName.getText().toString(), "","0");
			APPNet.login(mUserName.getText().toString(), mPass1.getText()
					.toString().trim(), this);
		} else {
			disMissDialog();
			APPHelper.saveUserInfo(mUserName.getText().toString(), mPass1
					.getText().toString().trim(), mMail.getText().toString(),
					mNickName.getText().toString(), "","1");
			DialogHelper.createHintDialog(this, "提示", "恭喜您注册成功！", "确定", null,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Bundle bundle = new Bundle();
							bundle.putString("loginOK", "1");
							IntentHelper.backIntent2Activity(
									RegisterActivity.this, APPConfig.A_login,
									bundle);
						}
					}, null);
		}
	}

	@Override
	public void onFail(String url, String code, String msg) {
		disMissDialog();
		if (url.startsWith(APPConfig.I_register)) {
			DialogHelper.showNote(this, msg);
		} else {
			DialogHelper.createHintDialog(this, "提示", "注册成功，请手动登录!", "确定",
					null, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Bundle bundle = new Bundle();
							bundle.putString("loginOK", "0");
							bundle.putString("userName", mUserName.getText().toString());
							IntentHelper.backIntent2Activity(
									RegisterActivity.this, APPConfig.A_login,
									bundle);
						}
					}, null);
		}
	}
}
