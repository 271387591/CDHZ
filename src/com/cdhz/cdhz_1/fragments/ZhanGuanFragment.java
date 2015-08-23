package com.cdhz.cdhz_1.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdhz.cdhz_1.APPConfig;
import com.cdhz.cdhz_1.APPNet;
import com.cdhz.cdhz_1.DialogHelper;
import com.cdhz.cdhz_1.R;
import com.cdhz.cdhz_1.views.RefreshListView;
import com.hyc.androidcore.BaseFragment;
import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.annotation.HFFindView;
import com.hyc.androidcore.annotation.HFSetListener;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.net.NetRequestListener;
import com.hyc.androidcore.util.IntentHelper;

public class ZhanGuanFragment extends BaseFragment implements
		OnItemClickListener ,OnClickListener,NetRequestListener{

	@HFFindView(Id = R.id.left)
	private ImageView mBack;
	// 标题
	@HFFindView(Id = R.id.title)
	private TextView mTitle;
	// 菜单切换按钮
	@HFSetListener(Id = R.id.right, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private ImageView mUser;

	// 刷新界面
	@HFFindView(Id = R.id.listview)
	private RefreshListView mList;

	private ZhanGuanAdapter mAdapter;

	public ZhanGuanFragment() {

	}

	@Override
	public View getView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.fragment_zhanguan, null);
	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
		if (msg.what == APPConfig.H_ZHANGUAN_REFRESH) {
			mAdapter.requestFirstPage();
		}
	}

	private Dialog mDialog;
	public void showNetDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		mDialog = DialogHelper.createNetConectingDialog(mActivity, false, "请求数据中");
		mDialog.show();
	}

	public void disMissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}
	@Override
	public void init() {
		mBack.setVisibility(View.GONE);
		mUser.setVisibility(View.GONE);
		mTitle.setText("展馆大全");
		mAdapter = new ZhanGuanAdapter(mActivity, mList, APPConfig.I_getHalls,
				new JSONObject());
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(this);
		mList.setSelector(new ColorDrawable(0xeeeeee));
		HandlerHelper.getInstance().sendMessage(true, 3000,
				APPConfig.H_ZHANGUAN_REFRESH);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (!mList.isIDLE()) {
			return;
		}
		JSONObject o = mAdapter.getItem((int)arg3);


//		showNetDialog();
//		APPNet.getHall(o.optString("id"), this);

        // 展馆介绍
        Bundle bundle = new Bundle();
        bundle.putString("data", o.toString());
        IntentHelper.startIntent2Activity(mActivity, APPConfig.A_zhanguandetail,
                bundle);

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onSuccess(String url, JSONArray data, int total) {
		disMissDialog();
		if(url.startsWith(url)){
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(mActivity, "暂无展馆介绍信息！", 2);
				return;
			}
			// 展馆介绍
			Bundle bundle = new Bundle();
			bundle.putString("data", data.optJSONObject(0).toString());
			IntentHelper.startIntent2Activity(mActivity, APPConfig.A_zhanguandetail,
					bundle);
		}
		
	}

	@Override
	public void onFail(String url, String code, String msg) {
		disMissDialog();
		if(url.startsWith(url)){
			DialogHelper.showToast(mActivity, msg, 2);
		}
		
	}
}
