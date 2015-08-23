package com.cdhz.cdhz_1;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cdhz.cdhz_1.BaseListViewPageAdapter.OnPageListViewListener;
import com.cdhz.cdhz_1.views.RefreshListView;
import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.annotation.BundleType;
import com.hyc.androidcore.annotation.HABundle;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HAInstanceState;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.annotation.HASetListener;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.net.NetRequestListener;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.util.IntentHelper;
import com.hyc.androidcore.views.AsyncImageView;

/**
 * 会展详细
 * 
 * @author Administrator
 *
 */
@HALayout(layout = R.layout.activity_huizhandetail)
public class HuiZhanDetailActivity extends BaseTitleActivity implements
		NetRequestListener, OnPageListViewListener {

	// 数据
	@HAInstanceState(name = "data", type = BundleType.JSONOBJECT)
	@HABundle(name = "data", type = BundleType.JSONOBJECT)
	private JSONObject mData;

	// 类型
	@HASetListener(Id = R.id.item_zhinan1, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mTyp1;
	@HASetListener(Id = R.id.item_zhinan2, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mTyp2;
	@HASetListener(Id = R.id.item_zhinan3, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mTyp3;
	@HASetListener(Id = R.id.item_zhinan4, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mTyp4;
	@HASetListener(Id = R.id.item_zhinan5, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mTyp5;
	@HASetListener(Id = R.id.item_zhinan6, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mTyp6;
	@HASetListener(Id = R.id.item_zhinan7, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mTyp7;
	@HASetListener(Id = R.id.item_zhinan8, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	View mTyp8;
	// 标志
	@HAFindView(Id = R.id.item_flag)
	private TextView mFlag;
	// 时间
	@HAFindView(Id = R.id.huizhan_shijian)
	private TextView mTime;

	// 地址
	@HASetListener(Id = R.id.huizhan_dizhi, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private TextView mAddress;

	// 展馆
	@HASetListener(Id = R.id.huizhan_zhanguan, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private TextView mZhanGuan;

	// 会展
	@HAFindView(Id = R.id.huizhan_name)
	private TextView mHuiZhan;

	// 评论按钮
	@HASetListener(Id = R.id.item_pinlunFlag, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View pinLunFalg;

	// 发布评论
	@HAFindView(Id = R.id.item_fapinlun)
	private View fabupinlun;

	@HAFindView(Id = R.id.item_pinluntext)
	private EditText pinlunText;

	@HASetListener(Id = R.id.item_pinlunButton, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View fabupinlunButton;

	@HAFindView(Id = R.id.item_pinlunList)
	private ListView list;

	@HAFindView(Id = R.id.item_scroll)
	private ScrollView scroll;

	@HASetListener(Id = R.id.item_more, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private TextView more;

	private Dialog mDialog;
	// 类型适配器
	private TypesAdapter mAdapter;

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

	@HAFindView(Id = R.id.item_pic)
	private AsyncImageView pic;

	@Override
	public void onClick(View v) {
		super.onClick(v);
		int id = v.getId();
		if (id == R.id.right) {
			Bundle b = new Bundle();
			String share = mData.optString("shareContent");
			if (CommHelper.checkNull(share)) {
				share = "我正在使用" + getString(R.string.app_name)+",大家一起使用哟！";
			}
			b.putString("shareData", share);
			IntentHelper.startIntent2Activity(this, APPConfig.A_share, b);
		} else if (id == R.id.item_zhinan1) {
			// 参展指南
//			showNetDialog();
//			APPNet.getExhGuide(mData.optString("id"), this);

            Bundle bb = new Bundle();
            bb.putString("title", "参展指南");
//            bb.putString("desc", data.optJSONObject(0).optString("guide"));
            bb.putString("webview", mData.optString("exhguide"));
            IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);

		} else if (id == R.id.item_zhinan2) {
			// 出行指南
//			showNetDialog();
//			APPNet.getExhGuideTo(mData.optString("id"), this);

            Bundle bb = new Bundle();
            bb.putDouble("lat", mData.optDouble("lat"));
            bb.putDouble("lng", mData.optDouble("lng"));
//            bb.putString("desc", data.optJSONObject(0).optString("guideTo"));
            bb.putString("webview", mData.optString("exhguideto"));

            System.out.println("mData.optDouble(\"lat\")=="+mData.optDouble("lat"));
            System.out.println("mData.optDouble(\"lng\")=="+mData.optDouble("lng"));
            System.out.println("mData.optDouble(\"exhguideto\")=="+mData.optString("exhguideto"));
            IntentHelper.startIntent2Activity(this, APPConfig.A_chuxin, bb);

		} else if (id == R.id.item_zhinan3) {
			// 展会资讯
			Bundle bb = new Bundle();
			bb.putInt("type", 0);
			bb.putString("id", mData.optString("id"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_zixun, bb);
		} else if (id == R.id.item_zhinan4) {
			// 观展指南
//			showNetDialog();
//			APPNet.getExhTravel(mData.optString("id"), this);

            Bundle bb = new Bundle();
            bb.putString("title", "观展指南");
//            bb.putString("desc", data.optJSONObject(0).optString("travel"));
            bb.putString("webview", mData.optString("exhtravel"));
            IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);

		} else if (id == R.id.item_zhinan5) {
			// 会展介绍
//			showNetDialog();
//			APPNet.getExhDescription(mData.optString("id"), this);

            Bundle bb = new Bundle();
            bb.putString("title", "展会介绍");
//            bb.putString("desc", data.optJSONObject(0).optString("description"));
            bb.putString("webview", mData.optString("exhdes"));
            IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);

		} else if (id == R.id.item_zhinan6) {
			// 主办信息
//			showNetDialog();
//			APPNet.getExhSponsor(mData.optString("id"), this);


            Bundle bb = new Bundle();
            bb.putString("title", "主办信息");
//            bb.putString("desc", data.optJSONObject(0).optString("sponsor"));
            bb.putString("webview", mData.optString("exhsponsor"));
            IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);

		} else if (id == R.id.item_zhinan7 || id == R.id.huizhan_zhanguan) {
			// 展馆介绍
			showNetDialog();
			APPNet.getHall(mData.optString("hallId"), this);
		} else if (id == R.id.item_zhinan8) {
			// 行业资讯
			disMissDialog();
			Bundle bb = new Bundle();
			bb.putInt("type", 1);
			bb.putString("title", "行业资讯");
			bb.putString("id", mData.optString("id"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_zixun, bb);
		} else if (id == R.id.huizhan_dizhi) {
			Bundle bunle = new Bundle();
			bunle.putDouble("lat", mData.optDouble("lat"));
			bunle.putDouble("lng", mData.optDouble("lng"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_map, bunle);
		} else if (id == R.id.item_pinlunFlag) {
			this.pinLunFalg.setVisibility(View.INVISIBLE);
			this.fabupinlun.setVisibility(View.VISIBLE);
			this.list.setVisibility(View.VISIBLE);
			this.more.setVisibility(View.VISIBLE);
			showNetDialog();
			page = 0;
			requstPinLun(page);
			this.scroll.scrollTo(0, 40);
		} else if (id == R.id.item_pinlunButton) {
			dealFabuPinglun();
		} else if (id == R.id.item_more) {
			// 更多
			if ((mAdapter.getCount() / APPConfig.PAGE_SIZE) > page) {
				page++;
			} else if (mAdapter.getCount() < APPConfig.PAGE_SIZE) {
				page = 0;
				more.setVisibility(View.VISIBLE);
			}
			requstPinLun(page);
		}
	}

	private void dealFabuPinglun() {
		String text = pinlunText.getText().toString();
		if (text.length() == 0) {
			DialogHelper.showToast(this, "请填写评论", 2);
			return;
		}
		if (!APPHelper.islogin()) {
			IntentHelper.startIntent2Activity(this, APPConfig.A_login);
			return;
		}
		showNetDialog();
		APPNet.comment("Exhibition", mData.optString("id"), text, "1", this);
	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
		super.handleMessage(msg, mainThread);
	}

	@Override
	protected void init() {
		super.init();
		setTitle("展会详情");
		setRightButton(R.drawable.share, View.VISIBLE);
		pic.setImageUrl(mData.optString("picUrl"), R.drawable.comm_zhanwei_1);
		mHuiZhan.setText(mData.optString("name"));
		mZhanGuan.setText(mData.optString("hallName"));
		mAddress.setText(mData.optString("address"));
		mTime.setText(getTime(mData.optString("startDate"),
				mData.optString("endDate")));
		if (mData.optBoolean("willStart", false)) {
			mFlag.setVisibility(View.VISIBLE);
		} else {
			mFlag.setVisibility(View.GONE);
		}
		fabupinlun.setVisibility(View.GONE);
		list.setVisibility(View.GONE);
		this.more.setVisibility(View.GONE);
		mAdapter = new TypesAdapter(this, new JSONArray());
		this.list.setAdapter(mAdapter);
	}

	private String getTime(String startTime, String endTime) {
		String ret = "";
		if (startTime != null) {
			ret += startTime;
		}
		if (endTime != null) {
			ret += "至" + endTime;
		}
		return ret;
	}

	@Override
	public void onSuccess(String url, JSONArray data, int total) {
		disMissDialog();
		if (url.startsWith(APPConfig.I_getExhGuideTo)) {
			// 出行指南
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "暂无出行指南信息！", 2);
				return;
			}
			Bundle bb = new Bundle();
			bb.putDouble("lat", data.optJSONObject(0).optDouble("lat"));
			bb.putDouble("lng", data.optJSONObject(0).optDouble("lng"));
            bb.putString("desc", data.optJSONObject(0).optString("guideTo"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_chuxin, bb);
		} else if (url.startsWith(APPConfig.I_getExhGuide)) {
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "暂无参展指南信息！", 2);
				return;
			}
			Bundle bb = new Bundle();
			bb.putString("title", "参展指南");
			bb.putString("desc", data.optJSONObject(0).optString("guide"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);
		} else if (url.startsWith(APPConfig.I_getExhTravel)) {
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "暂无观展指南信息！", 2);
				return;
			}
			Bundle bb = new Bundle();
			bb.putString("title", "观展指南");
			bb.putString("desc", data.optJSONObject(0).optString("travel"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);
		} else if (url.startsWith(APPConfig.I_getExhDescription)) {
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "暂无展会介绍信息！", 2);
				return;
			}
			Bundle bb = new Bundle();
			bb.putString("title", "展会介绍");
			bb.putString("desc", data.optJSONObject(0).optString("description"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);
		} else if (url.startsWith(APPConfig.I_getExhSponsor)) {
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "暂无主办信息信息！", 2);
				return;
			}
			Bundle bb = new Bundle();
			bb.putString("title", "主办信息");
			bb.putString("desc", data.optJSONObject(0).optString("sponsor"));
			IntentHelper.startIntent2Activity(this, APPConfig.A_webdesc, bb);
		} else if (url.startsWith(APPConfig.I_getHall)) {
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "暂无展馆介绍信息！", 2);
				return;
			}
			// 展馆介绍
			Bundle bundle = new Bundle();
			bundle.putString("data", data.optJSONObject(0).toString());
			IntentHelper.startIntent2Activity(this, APPConfig.A_zhanguandetail,
					bundle);
		} else if (url.startsWith(APPConfig.I_comment)) {
			page = 0;
			more.setVisibility(View.VISIBLE);
			requstPinLun(page);
			HandlerHelper.getInstance().getMainThreadHandler()
					.postDelayed(new Runnable() {

						@Override
						public void run() {
							disMissDialog();
						}
					}, 3000);
		} else if (url.startsWith(APPConfig.I_getComments)) {
			if (data == null || data.length() == 0) {
				DialogHelper.showToast(this, "暂无评论！", 2);
				return;
			}
			if (page == 0) {
				mAdapter.setData(data);
			} else {
				this.mAdapter.addData(data);
			}
			if (mAdapter.getCount() == total) {
				this.more.setVisibility(View.INVISIBLE);
			} else if (mAdapter.getCount() > 0) {
				more.setText("获取更多评论");
			}
			HandlerHelper.getInstance().getMainThreadHandler()
					.postDelayed(new Runnable() {

						@Override
						public void run() {
							disMissDialog();
							CommHelper.requestLayoutListView(list);
						}
					}, 1500);
		}
	}

	private int page = 0;

	private void requstPinLun(int page) {
		showNetDialog();
		APPNet.getComments("Exhibition", mData.optString("id"), page
				* APPConfig.PAGE_SIZE, APPConfig.PAGE_SIZE, this);
	}

	@Override
	public void onFail(String url, String code, String msg) {
		disMissDialog();
		if (url.startsWith(APPConfig.I_comment)) {
			DialogHelper.showToast(this, "发布评论失败！", 2);
		} else if (url.startsWith(APPConfig.I_getComments)) {
			page--;
			if (page <= 0) {
				page = 0;
			}
		} else {
			DialogHelper.showToast(this, "获取信息失败！", 2);
		}
	}

	@Override
	public void loadFirstPageFail() {
		disMissDialog();
		DialogHelper.showToast(this, "获取评论失败！", 2);
		HandlerHelper.getInstance().getMainThreadHandler()
				.postDelayed(new Runnable() {

					@Override
					public void run() {
						CommHelper.requestLayoutListView(list);

					}
				}, 400);
	}

	@Override
	public void loadFirstPageOk() {
		disMissDialog();
		HandlerHelper.getInstance().getMainThreadHandler()
				.postDelayed(new Runnable() {

					@Override
					public void run() {
						CommHelper.requestLayoutListView(list);

					}
				}, 400);
	}

	@Override
	public void loadFirstPageZero() {
		disMissDialog();
		DialogHelper.showToast(this, "暂无评论！", 2);
		HandlerHelper.getInstance().getMainThreadHandler()
				.postDelayed(new Runnable() {

					@Override
					public void run() {
						CommHelper.requestLayoutListView(list);

					}
				}, 400);
	}

	@Override
	public void loadDataOk() {
		HandlerHelper.getInstance().getMainThreadHandler()
				.postDelayed(new Runnable() {

					@Override
					public void run() {
						CommHelper.requestLayoutListView(list);

					}
				}, 400);

	}

}
