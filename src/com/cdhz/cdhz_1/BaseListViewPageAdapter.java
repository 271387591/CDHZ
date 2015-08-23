package com.cdhz.cdhz_1;

import android.content.Context;
import android.widget.BaseAdapter;

import com.cdhz.cdhz_1.views.RefreshListView;
import com.cdhz.cdhz_1.views.RefreshListView.OnRefreshListener;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.net.NetHelper;
import com.hyc.androidcore.net.NetRequestListener;

public abstract class BaseListViewPageAdapter extends BaseAdapter implements OnRefreshListener, NetRequestListener {
	// 容器
	protected Context mContext;
	// 数据
	protected JSONArray mData;
	// 数据总条数
	protected int mTotal;
	// ListView
	protected RefreshListView mListView;
	// 请求地址
	protected String mRequestUrl;
	// 是否是加载第一页刷新
	protected boolean mIsRefresh;
	// 请求数据
	protected JSONObject mRequstData;
	// 是否是正在加载数据
	protected boolean isLoadingData = false;
	// 分页加载监听器
	protected OnPageListViewListener mListener;

	public BaseListViewPageAdapter(Context context, RefreshListView listView, String interfaceUrl, JSONObject requestData) {
		this.mContext = context;
		this.mData = new JSONArray();
		this.mListView = listView;
		listView.setOnRefreshListener(this);
		this.mRequestUrl = interfaceUrl;
		this.mRequstData = requestData;
		mRequstData.put("limit", APPConfig.PAGE_SIZE);
		this.mTotal = 0;
	}

	
	public boolean isReLoading(){
		return isLoadingData;
	}
	
	public void setRequst(JSONObject request){
		this.mRequstData=request;
		mRequstData.put("start", 0);
		mRequstData.put("limit", APPConfig.PAGE_SIZE);
		this.mTotal=0;
	}
	/***
	 * 
	 * @Title: requestFirstPage
	 * @Description: 请求第一页数据
	 * @param 参数
	 * @return void 返回类型
	 * @author huangyc
	 * @date 2014-11-13 下午2:01:16
	 */
	public void requestFirstPage() {
		mRequstData.put("start", 0);
		mRequstData.put("limit", APPConfig.PAGE_SIZE);
		isLoadingData = true;
		this.mData = new JSONArray();
		NetHelper.getInstance().requestCancel(mRequestUrl);
		NetHelper.getInstance().request(mRequestUrl, mRequstData.toString(), this);
	}

	/**
	 * 添加数据
	 */
	public void addData(JSONArray data, boolean unique, String idStr) {
		for (int i = 0; i < data.length(); i++) {
			if (unique) {
				if (!isContain(data.optJSONObject(i), idStr)) {
					mData.put(data.optJSONObject(i));
				}
			} else {
				mData.put(data.optJSONObject(i));
			}
		}
		this.notifyDataSetChanged();
	}

	/***
	 * 返回数据
	 * 
	 * @return
	 */
	public JSONArray getData() {
		return mData;
	}

	/***
	 * 设置数据
	 */
	public void setData(JSONArray data) {
		this.mData = data;
		this.notifyDataSetInvalidated();
	}

	/***
	 * 是否包含某个数据
	 */
	public boolean isContain(JSONObject data, String idStr) {
		for (int i = 0; i < mData.length(); i++) {
			JSONObject v_temp = mData.optJSONObject(i);
			if (v_temp.optString(idStr).equals(data.optString(idStr))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getCount() {
		return mData.length();
	}

	@Override
	public JSONObject getItem(int i) {
		return mData.optJSONObject(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public void onRefresh() {
		mIsRefresh = true;
		if (!isLoadingData) {
			mRequstData.put("start", 0);
			isLoadingData = true;
			NetHelper.getInstance().request(mRequestUrl, mRequstData.toString(), this);
		} else {
			mListView.onRefreshFinish();
		}
	}

	@Override
	public void onLoadMoring() {
		if (mTotal > mData.length() && !isLoadingData) {
			mRequstData.put("start", mData.length());
			isLoadingData = true;
			mIsRefresh = false;
			NetHelper.getInstance().request(mRequestUrl, mRequstData.toString(), this);
		} else {
			mListView.onRefreshFinish();
		}

	}

	@Override
	public void onSuccess(String url, JSONArray data, int total) {
		if (url.equals(mRequestUrl)) {
			mTotal = total;
			if (mIsRefresh) {
				mData = data;
				notifyDataSetInvalidated();
				mListView.onRefreshFinish();
				mIsRefresh = false;
			} else {
				this.addData(data, false, null);
				mListView.onRefreshFinish();
				if(mListener!=null){
					mListener.loadDataOk();
				}
			}
			if (mListener != null && mRequstData.optInt("start", 0) == 0) {
				if (data == null || data.length() == 0) {
					mListener.loadFirstPageZero();
				} else {
					mListener.loadFirstPageOk();
				}
			}
			isLoadingData = false;
		}
	}

	@Override
	public void onFail(String url, String code, String msg) {
		if (url.equals(mRequestUrl)) {
			if (mListener != null && mRequstData.optInt("start", 0) == 0) {
				mListener.loadFirstPageFail();
			}
			int v_pageIndex = mRequstData.optInt("start", 0);
			if (v_pageIndex > 0) {
				mRequstData.put("start", v_pageIndex - APPConfig.PAGE_SIZE);
			}
			mListView.onRefreshFinish();
		}
		isLoadingData = false;
		mIsRefresh = false;
	}

	/***
	 * 
	 * @Title: setOnPageListViewListener
	 * @Description: 设置分页加载监听器
	 * @param @param listener 参数
	 * @return void 返回类型
	 * @author huangyc
	 * @date 2014-11-13 下午2:33:18
	 */
	public void setOnPageListViewListener(OnPageListViewListener listener) {
		this.mListener = listener;
	}

	/**
	 * 
	 * @ClassName: OnPageListViewListener
	 * @Description: 分页加载监听器
	 * @author huangyc
	 * @date 2014-11-13 下午2:33:39
	 * 
	 */
	public interface OnPageListViewListener {

		public void loadFirstPageFail();

		public void loadFirstPageOk();
		
		
		public void loadDataOk();

		/***
		 * 
		 * @Title: loadFirstPageZero
		 * @Description: 第一页数据为空
		 * @param 参数
		 * @return void 返回类型
		 * @author huangyc
		 * @date 2014-11-13 下午5:02:16
		 */
		public void loadFirstPageZero();
	}

}
