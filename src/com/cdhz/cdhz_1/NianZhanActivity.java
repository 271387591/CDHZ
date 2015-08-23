package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdhz.cdhz_1.BaseListViewPageAdapter.OnPageListViewListener;
import com.cdhz.cdhz_1.views.RefreshListView;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.net.NetRequestListener;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.util.IntentHelper;

@HALayout(layout = R.layout.activity_nianzhanlist)
public class NianZhanActivity extends BaseTitleActivity implements
		OnPageListViewListener, OnItemClickListener, NetRequestListener {

	// 刷新界面
	@HAFindView(Id = R.id.listview)
	private RefreshListView mList;

	private NianZhanAdapter mAdapter;

	@Override
	protected void init() {
		setTitle("年度展会计划");
		setRightButton(0, View.GONE);
		mAdapter = new NianZhanAdapter(this, mList, APPConfig.I_getExhPlans,
				new JSONObject());
		mList.setAdapter(mAdapter);
		mAdapter.requestFirstPage();
		mList.setOnItemClickListener(this);
		showNetDialog();
		mAdapter.setOnPageListViewListener(this);
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
	public void loadFirstPageFail() {
		disMissDialog();
		DialogHelper.showToast(this, "获取资讯信息失败，拉刷新试试！", 2);
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
		disMissDialog();
		DialogHelper.showToast(this, "暂无资讯信息！", 2);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mList.isIDLE()) {
			JSONObject o = mAdapter.getItem((int) arg3);
			if (CommHelper.checkNull(o.optString("outUrl"))) {
//				showNetDialog();
//				APPNet.getExhPlan(o.optString("id"), this);


                Bundle bb = new Bundle();
                bb.putString("title", "年度展会计划");
                bb.putString("msgtitle", o.optString("title"));
                bb.putString("desc", o.optString("content"));
                bb.putString("webview", o.optString("webview"));
                IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);

			} else {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(o.optString("outUrl")));
				startActivity(intent);

				// Bundle bb = new Bundle();
				// bb.putString("title", "年度展会计划");
				// //bb.putString("msgtitle", o.optString("title"));
				// bb.putString("url", o.optString("outUrl"));
				// IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc,
				// bb);
				// }

			}

		}

	}

	@Override
	public void onSuccess(String url, JSONArray data, int total) {
		disMissDialog();
		if (url.startsWith(APPConfig.I_getExhPlan)) {
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "暂无详细信息！", 2);
				return;
			}
			JSONObject o = data.optJSONObject(0);
			Bundle bb = new Bundle();
			bb.putString("title", "年度展会计划");
			bb.putString("msgtitle", o.optString("title"));
			bb.putString("desc", o.optString("content"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);
		}

	}

	@Override
	public void onFail(String url, String code, String msg) {
		disMissDialog();
		if (url.startsWith(APPConfig.I_getExhPlan)) {
			DialogHelper.showToast(this, msg, 2);
		}
	}
}
