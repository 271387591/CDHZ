package com.cdhz.cdhz_1;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyc.androidcore.HBaseAdapter;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;

public class TypesAdapter extends HBaseAdapter {



	public TypesAdapter(Context context, JSONArray data) {
		super(context, data);
	}


	public void addData(JSONArray data){
		addData(data, true, "id");
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_pinlun, null);
			ViewHolder vh = new ViewHolder();
			vh.desc = (TextView) convertView.findViewById(R.id.item_desc);
			vh.name = (TextView) convertView.findViewById(R.id.item_name);
			vh.time = (TextView) convertView.findViewById(R.id.item_time);
			convertView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) convertView.getTag();
		vh.name.setText(mData.optJSONObject(position).optString("username"));
		vh.desc.setText(mData.optJSONObject(position).optString("content"));
		vh.time.setText(formatTime(mData.optJSONObject(position).optString("createDate")));
		return convertView;
	}


	private String formatTime(String time){
		try{
		long dd=Long.valueOf(time);
		Date date=new Date(dd);
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sf.format(date);
		}catch(Exception e){
			return time;
		}
	}
	private class ViewHolder {
		public TextView desc;
		public TextView time;
		public TextView name;
	}
}
