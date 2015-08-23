package com.cdhz.cdhz_1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyc.androidcore.BaseApplication;
import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.cache.GlobalDataHelper;
import com.hyc.androidcore.db.DBHelper;
import com.hyc.androidcore.net.NetHelper;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.util.IntentHelper;
import com.hyc.androidcore.views.AsyncImageLoader;

public class DialogHelper {

	/***
	 * 
	 * @Title: showShutDownAPpDialog
	 * @Description: 显示退出系统对话框
	 * @param @param context 参数
	 * @return void 返回类型
	 * @author huangyc
	 * @date 2014-11-12 下午3:39:16
	 */
	public static void showShutDownAPpDialog(final Activity context) {
		AlertDialog.Builder v_builder = new AlertDialog.Builder(context);
		v_builder.setTitle("退出");
		v_builder.setMessage("您确定要退出软件吗？");
		v_builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (context instanceof Main) {
							context.finish();
							try {
								AsyncImageLoader.getInstance().shutdown();
								NetHelper.getInstance().stop();
								HandlerHelper.getInstance().stop();
								DBHelper.getInstance().closeDB();
								DBHelper.getInstance().close();
								GlobalDataHelper.getInstance().clearAll();
								GlobalDataHelper.getInstance().setIsShutDown(
										true);
								Thread.currentThread().sleep(300);
							} catch (Throwable e) {
								e.printStackTrace();
								IntentHelper.ShutDownAPP();
							}
							System.exit(0);
						} else {
							IntentHelper.ShutDownAPP();
						}
					}
				});
		v_builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		v_builder.create().show();

	}

	/***
	 * 
	 * @Title: showNote
	 * @Description: 显示提示
	 * @param @param context
	 * @param @param msg 参数
	 * @return void 返回类型
	 * @author huangyc
	 * @date 2014-11-18 上午10:21:31
	 */
	public static void showNote(Context context, String msg) {
		AlertDialog.Builder v_builder = new AlertDialog.Builder(context);
		v_builder.setTitle("提示");
		v_builder.setMessage(msg);
		v_builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		v_builder.create().show();
	}

	/***
	 * 客户停用
	 * 
	 * @param activity
	 * @param msg
	 */
	public static void showAPKNoUse(Context context, String msg) {
		AlertDialog.Builder v_builder = new AlertDialog.Builder(context);
		final AlertDialog v_dialog = v_builder.create();
		v_builder.setTitle("提示");
		v_builder.setMessage(msg);
		v_builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialoginterface, int i) {
				v_dialog.dismiss();
				IntentHelper.ShutDownAPP();
			}
		});
		v_builder.show();
	}

	/***
	 * 
	 * @Title: createHintDialog
	 * @Description: 创建提示信息对话框
	 * @param @param context
	 * @param @param title
	 * @param @param mes
	 * @param @param postionButton
	 * @param @param negativeButton
	 * @param @param positivelistener
	 * @param @param negativeListener 参数
	 * @return void 返回类型
	 * @author huangyc
	 * @date 2014-11-12 下午5:51:10
	 */
	public static Dialog createHintDialog(Context context, String title,
			String mes, String postionButton, String negativeButton,
			OnClickListener positivelistener, OnClickListener negativeListener) {
		AlertDialog.Builder v_builder = new Builder(context);
		if (!CommHelper.checkNull(title)) {
			v_builder.setTitle(title);
		} else {
			v_builder.setTitle("提示");
		}
		if (!CommHelper.checkNull(mes)) {
			v_builder.setMessage(mes);
		}
		v_builder.setCancelable(false);
		if (positivelistener != null) {
			v_builder.setPositiveButton(postionButton, positivelistener);
		}
		if (negativeButton != null) {
			v_builder.setNegativeButton(negativeButton, negativeListener);
		}
		Dialog d = v_builder.create();
		v_builder.show();
		return d;
	}

	/***
	 * 创建网络连接对话框
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param cancel
	 * @return
	 */
	public static ProgressDialog createNetConectingDialog(Context context,
			boolean cancel, String msg) {
		ProgressDialog v_dialog = new ProgressDialog(context,
				R.style.Theme_dialog);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dilog_view = inflater.inflate(R.layout.progressbar, null);
		TextView v_text = (TextView) dilog_view
				.findViewById(R.id.framgent_progress_text);
		v_text.setTextColor(context.getResources().getColor(R.color.comm_white));
		if (msg == null) {
			v_text.setText("处理中....");
		} else {
			v_text.setText(msg + "....");
		}
		v_dialog.show();
		v_dialog.setContentView(dilog_view);
		v_dialog.setCancelable(cancel);
		return v_dialog;
	}

	/***
	 * 各种toast提示
	 * 
	 * @param context
	 * @param msg
	 * @param Rimg
	 *            1 ： 成功 2 ：失败 3：提示
	 */
	public static void showToast(final Activity context, Object msg, int Rimg) {
		int v_tp = 0;
		View v_view = context.getLayoutInflater().inflate(R.layout.hint_text,
				null);
		ImageView mHintImg = (ImageView) v_view
				.findViewById(R.id.hint_text_img);
		TextView mHintText = (TextView) v_view
				.findViewById(R.id.hint_text_hint);
		if (Rimg == 1) {
			v_tp = R.drawable.registration_success;
			mHintText.setTextColor(context.getResources().getColor(
					R.color.comm_orange));
		} else if (Rimg == 2) {
			v_tp = R.drawable.registration_fail;
			mHintText
					.setTextColor(context.getResources().getColor(R.color.red));
		} else if (Rimg == 3) {
			v_tp = 0;
			mHintImg.setVisibility(View.GONE);
			mHintText.setTextColor(context.getResources().getColor(
					R.color.comm_dark_gray));
		}
		mHintImg.setImageResource(v_tp);
		mHintText.setText(msg + "");
		Toast v_toast = new Toast(context);
		v_toast.setGravity(Gravity.CENTER, 0, 0);
		v_toast.setDuration(Toast.LENGTH_SHORT);
		v_toast.setView(v_view);
		v_toast.show();
	}
}
