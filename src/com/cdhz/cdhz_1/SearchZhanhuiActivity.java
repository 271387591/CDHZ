package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.cdhz.cdhz_1.BaseListViewPageAdapter.OnPageListViewListener;
import com.cdhz.cdhz_1.fragments.HuiZhanAdapter;
import com.cdhz.cdhz_1.views.RefreshListView;
import com.hyc.androidcore.BaseActivity;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.annotation.HASetListener;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.util.IntentHelper;

@HALayout(layout = R.layout.activity_searchzhanhui)
public class SearchZhanhuiActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener ,OnPageListViewListener{

	@HASetListener(Id = R.id.left, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	public ImageView mLeft;

	@HAFindView(Id = R.id.title)
	public EditText mTitle;

	@HASetListener(Id = R.id.right, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	public ImageView mRight;

	// 刷新界面
	@HASetListener(Id = R.id.listview, listeners = { "setOnItemClickListener" }, listenersClass = { OnItemClickListener.class })
	private RefreshListView mList;

	private HuiZhanAdapter mAdapter;

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.left) {
			this.finish();
		} else if (id == R.id.right) {
			doSearch();
		}

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
	private void doSearch() {
		String content = mTitle.getText().toString();
		if (CommHelper.checkNull(content)) {
			DialogHelper.showToast(this, "请输入搜索关键字！", 2);
			return;
		}
		CommHelper.hideKeyBord(this, mTitle);
		mAdapter = new HuiZhanAdapter(this, mList, APPConfig.I_getExhs,
				new JSONObject().put("Q_exh.name_LK", content));
		mList.setAdapter(mAdapter);
		mAdapter.setOnPageListViewListener(this);
		mAdapter.requestFirstPage();
		showNetDialog();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mList.isIDLE()) {
			JSONObject o = mAdapter.getItem((int) arg3);
			Bundle bundler = new Bundle();
			bundler.putString("data", o.toString());
			IntentHelper.startIntent2Activity(this, APPConfig.A_huizhandetail,
					bundler);
		}
	}

	@Override
	public void loadFirstPageFail() {
		disMissDialog();
		DialogHelper.showToast(this, "查询失败,请稍后重试！", 2);
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
		DialogHelper.showToast(this, "没有查询到相关信息！", 1);
	}

}
