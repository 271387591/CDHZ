package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;
import com.hyc.androidcore.BaseActivity;
import com.hyc.androidcore.annotation.BundleType;
import com.hyc.androidcore.annotation.HABundle;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HAInstanceState;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.annotation.HASetListener;
import com.hyc.androidcore.cache.GlobalDataHelper;

@HALayout(layout = R.layout.activity_chuxin)
public class ChuXinZhiNanActivity extends BaseActivity implements
		OnKeyListener, OnClickListener {

	@HASetListener(Id = R.id.left, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	public ImageView mLeft;

	@HAFindView(Id = R.id.title)
	public TextView mTitle;

	@HASetListener(Id = R.id.right, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	public TextView mRight;

	@HAFindView(Id = R.id.map)
	private MapView map;

	@HAInstanceState(type = BundleType.DOUBLE, name = "lat")
	@HABundle(type = BundleType.DOUBLE, name = "lat")
	private double positionLat = 30.679879f;

	@HAInstanceState(type = BundleType.DOUBLE, name = "lng")
	@HABundle(type = BundleType.DOUBLE, name = "lng")
	private double positionLng = 104.064855f;

	@HAInstanceState(name = "desc")
	@HABundle(name = "desc")
	private String mDescHtml = "";

    @HAInstanceState(name = "webview")
    @HABundle(name = "webview")
    private String mWebview = "";

	private AMap aMap;

	@HASetListener(Id = R.id.item_desc, listeners = { "setOnKeyListener" }, listenersClass = { OnKeyListener.class })
	private WebView desc;

	@HAFindView(Id = R.id.space)
	private View space;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		map.onCreate(savedInstanceState);// 此方法必须重写
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
	protected void init() {
		super.init();
		mTitle.setText("出行指南");
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight() - 60;
		map.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//		if (mDescHtml != null && mDescHtml.trim().length() > 0) {
//			desc.getSettings().setJavaScriptEnabled(true);
//			desc.getSettings().setDefaultTextEncodingName("utf-8");
//			desc.loadDataWithBaseURL(null, mDescHtml, "text/html", "utf-8",
//					null);
//		} else {
//			desc.setVisibility(View.GONE);
//		}

        WebViewUtils.initView(desc,mWebview,this);


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
		if (aMap == null) {
			aMap = map.getMap();
			LatLng v_point;
			LatLngBounds.Builder bounds = new LatLngBounds.Builder();
			if (GlobalDataHelper.getInstance().containKey(
					APPConfig.USER_POSITION_geoLat)) {
				double lat = GlobalDataHelper.getInstance().getLong(
						APPConfig.USER_POSITION_geoLat) / 100000000.0f;
				double lng = GlobalDataHelper.getInstance().getLong(
						APPConfig.USER_POSITION_geoLng) / 100000000.0f;

				aMap.addMarker(new MarkerOptions()
						.anchor(0.5f, 0.5f)
						.draggable(true)
						.position(v_point = new LatLng(lat, lng))
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.map_start)));
				bounds.include(v_point);
			}
			//
			aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 0.5f)
					.position(v_point = new LatLng(positionLat, positionLng))
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.map_end)));

			bounds.include(v_point);
			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),
					10));
		}
		space.setVisibility(View.GONE);
		map.setVisibility(View.GONE);
	}

	private boolean isOnPause = false;

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		map.onResume();
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
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		map.onPause();
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
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		map.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
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
		map.onDestroy();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.right && map.getVisibility() != View.VISIBLE) {
			space.setVisibility(View.VISIBLE);
			map.setVisibility(View.VISIBLE);
		} else if (v.getId() == R.id.left) {
			this.finish();
		}

	}
}
