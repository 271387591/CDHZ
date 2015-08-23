package com.cdhz.cdhz_1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.hyc.androidcore.BaseActivity;
import com.hyc.androidcore.util.CommHelper;

/***
 * 分享工具类
 * 
 * @author huangyc
 * 
 */
public class ShareUtil {

	/***
	 * 通过短信进行分析
	 * 
	 * @param activity
	 * @param msg
	 */
	public static void shareBySMS(Activity activity, String msg) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", msg);
		activity.startActivity(intent);
	}

	/***
	 * 分享文字
	 * 
	 * @param context
	 * @param msg
	 *            消息内容
	 * @param packageName
	 *            packageName为空进行短信分享
	 * @param type
	 *            分享类型 1:微信分享 2：微信朋友圈 3：其他
	 */
	public static void shareText(BaseActivity context, String msg, String packageName) {
		try {
			if (CommHelper.checkNull(packageName)) {
				shareBySMS(context, msg);
				return;
			}
			ResolveInfo v_ResolveInfo = getShareApp(context, packageName);
			if (v_ResolveInfo == null) {
				return;
			}
			Intent intent = new Intent();
			ComponentName componentName = new ComponentName(packageName, v_ResolveInfo.activityInfo.name);
			intent.setComponent(componentName);
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, msg);
			context.startActivity(intent);
		} catch (Throwable e) {
			e.printStackTrace();
			CommHelper.showToastShort(context, "分享失败！");
		}
	}

	/***
	 * 分享图片
	 * 
	 * @param context
	 * @param pic
	 * @param packageName
	 */
	public static void sharePic(BaseActivity context, File pic, String packageName) {
		try {
			ResolveInfo v_ResolveInfo = getShareApp(context, packageName);
			if (v_ResolveInfo == null) {
				return;
			}
			Intent intent = new Intent();
			ComponentName componentName = new ComponentName(packageName, v_ResolveInfo.activityInfo.name);
			intent.setComponent(componentName);
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("image/*");
			Uri u = Uri.fromFile(pic);
			intent.putExtra(Intent.EXTRA_STREAM, u);
			context.startActivity(intent);
		} catch (Throwable e) {
			e.printStackTrace();
			CommHelper.showToastShort(context, "分享失败！");
		}
	}

	/***
	 * 根据packageName获取分享窗口
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static ResolveInfo getShareApp(Context context, String packageName) {
		List<ResolveInfo> v_list = getShareApps(context);
		for (ResolveInfo v_info : v_list) {
			if (v_info.activityInfo.packageName.equals(packageName)) {
				return v_info;
			}
		}
		return null;
	}

	/****
	 * 获取能够分享的应用
	 * 
	 * @param context
	 * @return
	 */
	public static List<ResolveInfo> getShareApps(Context context) {
		List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		PackageManager pManager = context.getPackageManager();
		mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		return mApps;
	}

	/***
	 * 检测程序是否安装
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean checkAppIsInstall(Context context, String packageName) {
		try {
			PackageManager v_pm = context.getPackageManager();
			ApplicationInfo info = v_pm.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			if (CommHelper.checkNull(info.packageName)) {
				return false;
			}
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

}
