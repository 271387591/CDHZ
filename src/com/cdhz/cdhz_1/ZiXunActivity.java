package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdhz.cdhz_1.BaseListViewPageAdapter.OnPageListViewListener;
import com.cdhz.cdhz_1.views.RefreshListView;
import com.hyc.androidcore.annotation.BundleType;
import com.hyc.androidcore.annotation.HABundle;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HAInstanceState;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.net.NetRequestListener;
import com.hyc.androidcore.util.IntentHelper;

/***
 * 资讯
 * 
 * @author Administrator
 *
 */
@HALayout(layout = R.layout.activity_zixun)
public class ZiXunActivity extends BaseTitleActivity implements
		OnItemClickListener, OnPageListViewListener, NetRequestListener {

	// 刷新界面
	@HAFindView(Id = R.id.listview)
	private RefreshListView mList;

	private ZiXunAdapter mAdapter;

	@HAInstanceState(name = "id")
	@HABundle(name = "id")
	private String ZhanhuiId;

	@HAInstanceState(name = "title")
	@HABundle(name = "title")
	private String titel = "展会资讯";

	@HAInstanceState(name = "type", type = BundleType.INT)
	@HABundle(name = "type", type = BundleType.INT)
	private int type = 0;

	@Override
	protected void init() {
		super.init();
		setTitle(titel);
		setRightButton(0, View.GONE);
		mList.setOnItemClickListener(this);
		if (type == 0) {
			mAdapter = new ZiXunAdapter(this, mList, APPConfig.I_getExhNews,
					new JSONObject().put("Q_exhId_EQ", ZhanhuiId));
		} else if (type == 1) {
			mAdapter = new ZiXunAdapter(this, mList, APPConfig.I_getExhTrade,
					new JSONObject().put("exhId", ZhanhuiId));
		} else if (type == 2) {
			mAdapter = new ZiXunAdapter(this, mList, APPConfig.I_getNews,
					new JSONObject());
		} else if (type == 3) {
			mAdapter = new ZiXunAdapter(this, mList,
					APPConfig.I_getExhServices, new JSONObject());
		}
		mList.setAdapter(mAdapter);
		mAdapter.requestFirstPage();
		mAdapter.setOnPageListViewListener(this);
		showNetDialog();
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mList.isIDLE()) {
			JSONObject o = mAdapter.getItem((int) arg3);
			if (type == 2) {
				// 新闻资讯
//				showNetDialog();
//				APPNet.getNewOne(o.optString("id"), this);


                Bundle bb = new Bundle();
                bb.putString("title", titel);
                bb.putString("msgtitle", o.optString("title"));
                bb.putString("desc", o.optString("news"));
                bb.putString("webview", o.optString("webview"));
                IntentHelper
                        .startIntent2Activity(this, APPConfig.A_webdesc, bb);


				return;
			} else if (type == 3) {
				// 会展服务
//				showNetDialog();
//				APPNet.getExhService(o.optString("id"), this);


                Bundle bb = new Bundle();
                bb.putString("title", titel);
                bb.putString("msgtitle", o.optString("title"));
                bb.putString("desc", o.optString("content"));
                bb.putString("webview", o.optString("webview"));
                IntentHelper
                        .startIntent2Activity(this, APPConfig.A_webdesc, bb);

				return;
			}

			Bundle bb = new Bundle();
			bb.putString("title", titel);
			bb.putString("msgtitle", o.optString("title"));
			bb.putString("desc", o.optString("news"));
            bb.putString("webview", o.optString("webview"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);

		}
	}

	@Override
	public void loadFirstPageFail() {
		disMissDialog();
		DialogHelper.showToast(this, "获取信息失败，拉刷新试试！", 2);
	}

	@Override
	public void loadFirstPageOk() {
		disMissDialog();
	}

	@Override
	public void loadDataOk() {

	}

	@Override
	public void loadFirstPageZero() {
		DialogHelper.showToast(this, "暂无信息！", 2);

	}

	@Override
	public void onSuccess(String url, JSONArray data, int total) {
		disMissDialog();
		if (url.startsWith(APPConfig.I_getNew)) {
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "暂无新闻资讯信息！", 2);
			} else {
				JSONObject o = data.optJSONObject(0);
				Bundle bb = new Bundle();
				bb.putString("title", titel);
				bb.putString("msgtitle", o.optString("title"));
				bb.putString("desc", o.optString("news"));
				IntentHelper
						.startIntent2Activity(this, APPConfig.A_webdesc, bb);
			}
		} else if (url.startsWith(APPConfig.I_getExhService)) {
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "暂无会展服务信息！", 2);
			} else {
				JSONObject o = data.optJSONObject(0);
				Bundle bb = new Bundle();
				bb.putString("title", titel);
				bb.putString("msgtitle", o.optString("title"));
				bb.putString("desc", o.optString("content"));
				IntentHelper
						.startIntent2Activity(this, APPConfig.A_webdesc, bb);
			}
		}
	}

	@Override
	public void onFail(String url, String code, String msg) {
		disMissDialog();
		DialogHelper.showToast(this, msg, 2);

	}

}
