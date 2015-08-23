package com.cdhz.cdhz_1.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdhz.cdhz_1.APPHelper;
import com.cdhz.cdhz_1.R;
import com.hyc.androidcore.HBaseAdapter;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.views.AsyncImageView;

public class ReMenZhuanGuanAdapter extends HBaseAdapter {

	public ReMenZhuanGuanAdapter(Context context, JSONArray data) {
		super(context, data);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_remenzhuanguan, null);
			ViewHolder vh = new ViewHolder();
			vh.pic = (AsyncImageView) convertView.findViewById(R.id.item_pic);
			vh.name = (TextView) convertView.findViewById(R.id.item_name);
			convertView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) convertView.getTag();
		vh.pic.setImageUrl(mData.optJSONObject(position).optString("logo1Url"),
				R.drawable.comm_zhanwei_1);
		vh.name.setText(mData.optJSONObject(position).optString("name"));
		return convertView;
	}

	private class ViewHolder {
		public AsyncImageView pic;
		public TextView name;
	}
}
