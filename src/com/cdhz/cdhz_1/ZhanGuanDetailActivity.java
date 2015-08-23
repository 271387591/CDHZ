package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hyc.androidcore.annotation.BundleType;
import com.hyc.androidcore.annotation.HABundle;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HAInstanceState;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.annotation.HASetListener;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.util.IntentHelper;
import com.hyc.androidcore.views.AsyncImageView;

/***
 * 展馆详细
 * 
 * @author Administrator
 *
 */
@HALayout(layout = R.layout.activity_zhuanguandetail)
public class ZhanGuanDetailActivity extends BaseTitleActivity implements OnKeyListener{

	@HAFindView(Id=R.id.item_name)
	private TextView name;
	
	@HAFindView(Id=R.id.item_pic)
	private AsyncImageView pic;
	
	@HAFindView(Id=R.id.item_address)
	private TextView address;
	
	@HASetListener(Id = R.id.item_desc, listeners = { "setOnKeyListener" }, listenersClass = { OnKeyListener.class })
	private WebView desc;

	
	@HASetListener(Id = R.id.location, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mLocation;
	
	//数据内容
	@HAInstanceState(name="data",type=BundleType.JSONOBJECT)
	@HABundle(name="data",type=BundleType.JSONOBJECT)
	private JSONObject mData=new JSONObject();
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId()==R.id.location) {
			Bundle bunle=new Bundle();
			bunle.putDouble("lat", mData.optDouble("lat"));
			bunle.putDouble("lng", mData.optDouble("lng"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_map, bunle);
		}
	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
		super.handleMessage(msg, mainThread);
	}
	
//	private Dialog mDialog;
//	public void showNetDialog() {
//		if (mDialog != null) {
//			mDialog.dismiss();
//		}
//		mDialog = DialogHelper.createNetConectingDialog(this, false, "请求数据中");
//		mDialog.show();
//	}
//
//	public void disMissDialog() {
//		if (mDialog != null) {
//			mDialog.dismiss();
//		}
//	}

	@Override
	protected void init() {
		super.init();
		setTitle("展馆详细");
		setRightButton(0, View.GONE);
		name.setText(mData.optString("name"));
		pic.setImageUrl(mData.optString("logo2Url"), R.drawable.comm_zhanwei_1);
		address.setText(mData.optString("address"));
//		desc.getSettings().setJavaScriptEnabled(true);
//		desc.getSettings().setDefaultTextEncodingName("utf-8") ;
//		desc.loadDataWithBaseURL(null, mData.optString("description"), "text/html", "utf-8", null);


        String webviewUrl=mData.optString("webview");
        WebViewUtils.initView(desc,webviewUrl,this);


//		desc.setWebViewClient(new WebViewClient(){
//
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				disMissDialog();
//				super.onPageFinished(view, url);
//			}
//
//			@Override
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				showNetDialog();
//				super.onPageStarted(view, url, favicon);
//				
//			}
//
//			@Override
//			public void onReceivedError(WebView view, int errorCode,
//					String description, String failingUrl) {
//				disMissDialog();
//				super.onReceivedError(view, errorCode, description, failingUrl);
//			}
//
//			@Override
//			public void onReceivedSslError(WebView view,
//					SslErrorHandler handler, SslError error) {
//				disMissDialog();
//				super.onReceivedSslError(view, handler, error);
//			}
//			
//		});
		//desc.loadData(mData.optString("description"), "text/html", "utf-8");
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
				finish();
		}
		return false;
	}

	private boolean isOnPause = false;
	
	@Override
	protected void onPause() {
		super.onPause();
		try {
			if (desc != null) {
				isOnPause = true;
				desc.getClass().getMethod("onPause").invoke(desc, (Object[]) null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 当Activity执行onResume()时让WebView执行resume
	 */

	@Override
	protected void onResume() {
		super.onResume();
		try {
			if (isOnPause) {
				if (desc != null) {
					desc.getClass().getMethod("onResume").invoke(desc, (Object[]) null);
				}
				isOnPause = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 该处的处理尤为重要:
	 * 
	 * 应该在内置缩放控件消失以后,再执行mWebView.destroy()
	 * 
	 * 否则报错WindowLeaked
	 */

	@Override
	protected void onDestroy() {
		try {
			if (desc != null) {
				desc.removeAllViews();
				desc.setVisibility(View.GONE);
				desc.destroy();
				desc = null;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		isOnPause = false;
		super.onDestroy();
	}
	
}
