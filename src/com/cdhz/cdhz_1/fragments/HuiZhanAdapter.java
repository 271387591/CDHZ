package com.cdhz.cdhz_1.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdhz.cdhz_1.BaseListViewPageAdapter;
import com.cdhz.cdhz_1.R;
import com.cdhz.cdhz_1.views.RefreshListView;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.views.AsyncImageView;

public class HuiZhanAdapter extends BaseListViewPageAdapter {

	public HuiZhanAdapter(Context context, RefreshListView listView,
			String interfaceUrl, JSONObject requestData) {
		super(context, listView, interfaceUrl, requestData);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_huizhan, null);
			ViewHolder vh = new ViewHolder();
			vh.pic = (AsyncImageView) convertView.findViewById(R.id.item_pic);
			vh.name = (TextView) convertView.findViewById(R.id.item_name);
			vh.address = (TextView) convertView.findViewById(R.id.item_address);
			vh.time = (TextView) convertView.findViewById(R.id.item_time);
			vh.flag = (TextView) convertView.findViewById(R.id.item_flag);
			convertView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) convertView.getTag();
		vh.pic.setImageUrl(mData.optJSONObject(position).optString("logoUrl"),
				R.drawable.comm_zhanwei_2);
		vh.name.setText(mData.optJSONObject(position).optString("name"));
		vh.address.setText(mData.optJSONObject(position).optString("hallName"));
		vh.time.setText(getTime(
				mData.optJSONObject(position).optString("startDate"), mData
						.optJSONObject(position).optString("endDate")));
		if (mData.optJSONObject(position).optBoolean("willStart", false)) {
			vh.flag.setVisibility(View.VISIBLE);
		} else {
			vh.flag.setVisibility(View.GONE);
		}
		return convertView;
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

	private class ViewHolder {

		public AsyncImageView pic;
		public TextView name;
		public TextView address;
		public TextView time;
		public TextView flag;
	}
}
