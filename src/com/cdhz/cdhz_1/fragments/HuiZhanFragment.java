package com.cdhz.cdhz_1.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdhz.cdhz_1.APPConfig;
import com.cdhz.cdhz_1.BaseListViewPageAdapter.OnPageListViewListener;
import com.cdhz.cdhz_1.DialogHelper;
import com.cdhz.cdhz_1.R;
import com.cdhz.cdhz_1.views.RefreshListView;
import com.hyc.androidcore.BaseFragment;
import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.annotation.HFFindView;
import com.hyc.androidcore.annotation.HFSetListener;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.util.IntentHelper;

public class HuiZhanFragment extends BaseFragment implements OnClickListener,
		OnItemClickListener,OnPageListViewListener {

	public HuiZhanFragment() {

	}

	@HFFindView(Id = R.id.left)
	private ImageView mBack;
	// 标题
	@HFFindView(Id = R.id.title)
	private TextView mTitle;
	// 菜单切换按钮
	@HFSetListener(Id = R.id.right, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private ImageView mUser;

	@HFSetListener(Id = R.id.type_0, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mType0;

	@HFSetListener(Id = R.id.type_1, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mType1;

	@HFSetListener(Id = R.id.type_2, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View mType2;

	@HFFindView(Id = R.id.type_0_flag)
	private ImageView mType0Flag;

	@HFFindView(Id = R.id.type_1_flag)
	private ImageView mType1Flag;

	@HFFindView(Id = R.id.type_2_flag)
	private ImageView mType2Flag;

	// 刷新界面
	@HFSetListener(Id = R.id.listview, listeners = { "setOnItemClickListener" }, listenersClass = { OnItemClickListener.class })
	private RefreshListView mList;

	private HuiZhanAdapter mAdapter;

	
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
	public View getView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.fragment_huizhan, null);
	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
		if (msg.what == APPConfig.H_HUIZHAN_REFRESH) {
			mAdapter.requestFirstPage();
		}
	}

	@Override
	public void init() {
		mBack.setVisibility(View.GONE);
		mTitle.setText("近期会展");
		mAdapter = new HuiZhanAdapter(mActivity, mList, APPConfig.I_getExhs,
				new JSONObject().put("Q_exh.createDate", "DESC"));
		mList.setAdapter(mAdapter);
		mList.setSelector(new ColorDrawable(0xeeeeee));
		HandlerHelper.getInstance().sendMessage(true, 3000,
				APPConfig.H_HUIZHAN_REFRESH);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mList.isIDLE()) {
			JSONObject o = mAdapter.getItem((int) arg3);
			Bundle bundler = new Bundle();
			bundler.putString("data", o.toString());
			IntentHelper.startIntent2Activity(mActivity,
					APPConfig.A_huizhandetail, bundler);
		}
	}

	int count = 0;

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.type_0) {
			if (count == 0) {
				count = 1;
				mType0Flag.setImageResource(R.drawable.arrow_up);
			} else {
				count = 0;
				mType0Flag.setImageResource(R.drawable.arrow_down);
			}
			showNetDialog();
			mAdapter.setData(new JSONArray());
			mAdapter.setRequst(new JSONObject().put("Q_h.name",
					count == 0 ? "DESC" : "ASC"));
			mAdapter.setOnPageListViewListener(this);
			mAdapter.requestFirstPage();
		} else if (id == R.id.type_1) {
			if (count == 2) {
				count = 3;
				mType1Flag.setImageResource(R.drawable.arrow_up);
			} else {
				count = 2;
				mType1Flag.setImageResource(R.drawable.arrow_down);
			}
			showNetDialog();
			mAdapter.setData(new JSONArray());
			mAdapter.setRequst(new JSONObject().put("Q_exh.createDate",
					count == 2 ? "DESC" : "ASC"));
			mAdapter.requestFirstPage();
			mAdapter.setOnPageListViewListener(this);
		} else if (id == R.id.type_2) {
			if (count == 4) {
				count = 5;
				mType2Flag.setImageResource(R.drawable.arrow_up);
			} else {
				count = 4;
				mType2Flag.setImageResource(R.drawable.arrow_down);
			}
			showNetDialog();
			mAdapter.setData(new JSONArray());
			mAdapter.setRequst(new JSONObject().put("Q_exh.tradeNames",
					count == 4 ? "DESC" : "ASC"));
			mAdapter.setOnPageListViewListener(this);
			mAdapter.requestFirstPage();
		}else if(id==R.id.right){
			IntentHelper.startIntent2Activity(mActivity, APPConfig.A_huizhanSearch);
		}

	}

	@Override
	public void loadFirstPageFail() {
		disMissDialog();
		DialogHelper.showToast(mActivity, "获取会展信息失败，下拉试试！", 2);
	}

	@Override
	public void loadFirstPageOk() {
		// TODO Auto-generated method stub
		disMissDialog();
	}

	@Override
	public void loadDataOk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadFirstPageZero() {
		disMissDialog();
		DialogHelper.showToast(mActivity, "暂无会展信息！", 2);
	}
}
