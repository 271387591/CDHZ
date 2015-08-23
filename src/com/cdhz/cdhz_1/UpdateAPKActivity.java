package com.cdhz.cdhz_1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.annotation.HABundle;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HAInstanceState;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.util.EnvironmentHelper;
import com.hyc.androidcore.util.IntentHelper;

/***
 * 软件升级页面
 * 
 * @author huangyc
 * 
 */
@HALayout(layout = R.layout.activity_update_app)
public class UpdateAPKActivity extends BaseTitleActivity {

	@HAFindView(Id = R.id.updateapp_progress)
	private TextView mProgress;

	@HAFindView(Id = R.id.updateapp_progressbar)
	private ProgressBar mProgressbar;

	@HABundle(name = "appurl")
	@HAInstanceState(name = "appurl")
	private String mUrl = "";

	@Override
	public void init() {
		mProgressbar.setProgress(0);
		HandlerHelper.getInstance().sendMessage(true, 0, APPConfig.H_update_getApk);
	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
		super.handleMessage(msg, mainThread);
		if (msg.what == APPConfig.H_update_getApk) {
			new DownAppTask(this).execute(mUrl);
		} else if (msg.what == APPConfig.H_update_app) {
			mProgressbar.setProgress(msg.arg1);
			mProgress.setText(msg.arg1 + "%");
		} else if (msg.what == APPConfig.H_update_install) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(msg.obj.toString())), "application/vnd.android.package-archive");
			startActivity(intent);
			IntentHelper.ShutDownAPP();
		}
	}

	private class DownAppTask extends AsyncTask<String, ProgressBar, Integer> implements DialogInterface.OnClickListener {

		private Context mContext;
		private String mApkPath;

		public DownAppTask(Context context) {
			this.mContext = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			HandlerHelper.getInstance().sendMessage(true, 500, APPConfig.H_update_app, 0);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result > 0) {
				HandlerHelper.getInstance().sendMessage(true, 20, APPConfig.H_update_app, 100, 0);
				HandlerHelper.getInstance().sendMessage(true, 2000, APPConfig.H_update_install, mApkPath);
			} else {
				HandlerHelper.getInstance().sendMessage(true, 0, APPConfig.H_update_app, 0);
				DialogHelper.createHintDialog(mContext, "提示", "更新失败！", "重试", "退出", this, this);
			}
		}

		@Override
		protected void onProgressUpdate(ProgressBar... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected Integer doInBackground(String... params) {
			FileOutputStream fos = null;
			InputStream v_is = null;
			try {
				URL v_url = new URL(params[0]);
				HttpURLConnection v_conn = (HttpURLConnection) v_url.openConnection();
				int v_maxlength = v_conn.getContentLength();
				v_is = v_conn.getInputStream();
				mApkPath = getWriteFilePath(params[0]);
				fos = new FileOutputStream(mApkPath);
				byte[] buffer = new byte[25600];
				int v_len = 0;
				int maxProgress = 0;
				while ((v_len = v_is.read(buffer)) > 0) {
					fos.write(buffer, 0, v_len);
					maxProgress += v_len;
					HandlerHelper.getInstance().sendMessage(true, 20, APPConfig.H_update_app, maxProgress * 100 / v_maxlength, 0);
				}
				fos.flush();
				fos.close();
				return 1;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			} finally {
				if (v_is != null) {
					try {
						v_is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

		/*****
		 * 获取apk路径
		 * 
		 * @param url
		 * @return
		 */
		private String getWriteFilePath(String url) {
			File v_f = EnvironmentHelper.getAppFile("update.apk");
			if (v_f.exists()) {
				v_f.delete();
			}
			return v_f.getAbsolutePath();
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == -1) {
				HandlerHelper.getInstance().sendMessage(true, 500, APPConfig.H_update_getApk);
			} else if (which == -2) {
				IntentHelper.ShutDownAPP();
			}
			dialog.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		DialogHelper.createHintDialog(this, "退出", "您确定要退出软件吗？", "确定", "取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialoginterface, int i) {
				dialoginterface.dismiss();
				IntentHelper.ShutDownAPP();
			}
		}, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialoginterface, int i) {
				dialoginterface.dismiss();
			}
		});
	}

}
