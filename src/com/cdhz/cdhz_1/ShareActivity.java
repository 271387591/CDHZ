package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.annotation.HABundle;
import com.hyc.androidcore.annotation.HAInstanceState;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.annotation.HASetListener;
import com.hyc.androidcore.cache.GlobalDataHelper;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.net.NetHelper;
import com.hyc.androidcore.net.NetRequestListener;
import com.hyc.androidcore.util.CommHelper;

/**
 * 分享
 * 
 * 
 * 
 */
@HALayout(layout = R.layout.activity_share)
public class ShareActivity extends BaseTitleActivity  {

	
	// 新浪微博分享
	@HASetListener(Id = R.id.my_recommend_share_xl_blog, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mShare_XLBlog;
	// QQ分享
	@HASetListener(Id = R.id.my_recommend_share_qq, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mShare_QQ;

	// 微信分享
	@HASetListener(Id = R.id.my_recommend_share_wx, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mShare_WX;

	// 短信分享
	@HASetListener(Id = R.id.my_recommend_share_message, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mShare_Message;

	public Dialog mNetDialog;

	/** 腾讯QQ应用包 **/
	private final String Package_share_qq = "com.tencent.mobileqq";
	/** 新浪微博应用包 ***/
	private final String Package_share_sina = "com.sina.weibo";
	/** 微信分享应用包 ***/
	private final String Package_share_weixin = "com.tencent.mm";
	/** 短信分享 ***/
	private final String Package_share_sm = "com.android.mms";

	// 分享内容
	@HABundle(name = "shareData")
	@HAInstanceState(name = "shareData")
	private String mShareData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			setTitle("分享");
		setRightButton(0, View.INVISIBLE);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		int v_id = v.getId();
		switch (v_id) {
		case R.id.left:
			this.finish();
			break;
		case R.id.my_recommend_share_xl_blog:
			// 新浪微博
			if (ShareUtil.checkAppIsInstall(this, Package_share_sina)) {
				ShareUtil.shareText(this, mShareData, Package_share_sina);
				return;
			}
			if (ShareUtil.checkAppIsInstall(this, "com.sina.weibog3")) {
				ShareUtil.shareText(this, mShareData, "com.sina.weibog3");
				return;
			}
			DialogHelper.showNote(this, "检测到您未安装新浪微博客户端，如需分享，请到官网下载客户端！");
			break;
		case R.id.my_recommend_share_qq:
			if (!ShareUtil.checkAppIsInstall(this, Package_share_qq)) {
				DialogHelper.showNote(this, "检测到您未安装QQ客户端，如需分享，请到官网下载客户端！");
				return;
			}
			ShareUtil.shareText(this, mShareData, Package_share_qq);
			break;
		case R.id.my_recommend_share_wx:
			// 微信
			if (!ShareUtil.checkAppIsInstall(this, Package_share_weixin)) {
				DialogHelper.showNote(this, "检测到您未安装微信客户端，如需分享，请到官网下载客户端！");
				return;
			}
			ShareUtil.shareText(this, mShareData, Package_share_weixin);
			break;
		case R.id.my_recommend_share_message:
			// 短信
			if (!ShareUtil.checkAppIsInstall(this, Package_share_sm)) {
				ShareUtil.shareBySMS(this, mShareData);
				return;
			}
			ShareUtil.shareText(this, mShareData, Package_share_sm);
			break;
		}
	}





}
