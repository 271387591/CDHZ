package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hyc.androidcore.annotation.HABundle;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HAInstanceState;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.annotation.HASetListener;
import com.hyc.androidcore.util.CommHelper;

//指南
@HALayout(layout = R.layout.activity_desc_web)
public class DescWebActivity extends BaseTitleActivity implements OnKeyListener {

	@HAInstanceState(name = "title")
	@HABundle(name = "title")
	private String mtitle = "信息指南";

	@HAInstanceState(name = "desc")
	@HABundle(name = "desc")
	private String mDescHtml = "";

	@HASetListener(Id = R.id.item_desc, listeners = { "setOnKeyListener" }, listenersClass = { OnKeyListener.class })
	private WebView desc;

	@HAInstanceState(name = "msgtitle")
	@HABundle(name = "msgtitle")
	private String mMsgTitle;

	@HAInstanceState(name = "url")
	@HABundle(name = "url")
	private String mUrl = "";

    @HAInstanceState(name = "webview")
	@HABundle(name = "webview")
	private String mWebview = "";



	@HAFindView(Id = R.id.desc_title)
	private TextView mMsgTitleView;

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
		setTitle(mtitle);
		setRightButton(0, View.GONE);

        WebViewUtils.initView(desc,mWebview,this);


//		desc.getSettings().setJavaScriptEnabled(true);
//		desc.getSettings().setDefaultTextEncodingName("utf-8");
//		if (!CommHelper.checkNull(mUrl)) {
//			desc.loadUrl(mUrl);
//		} else {
//			desc.loadDataWithBaseURL(null, mDescHtml, "text/html", "utf-8",
//					null);
//		}
//		desc.setWebViewClient(new WebViewClient() {
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
		if (mMsgTitle == null || mMsgTitle.trim().length() == 0) {
			mMsgTitleView.setVisibility(View.GONE);
		} else {
			mMsgTitleView.setText(mMsgTitle);
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
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
				desc.getClass().getMethod("onPause")
						.invoke(desc, (Object[]) null);
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
					desc.getClass().getMethod("onResume")
							.invoke(desc, (Object[]) null);
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
