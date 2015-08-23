package com.cdhz.cdhz_1;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cdhz.cdhz_1.views.RefreshListView;
import com.hyc.androidcore.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NianZhanAdapter extends BaseListViewPageAdapter {

	public NianZhanAdapter(Context context, RefreshListView listView,
			String interfaceUrl, JSONObject requestData) {
		super(context, listView, interfaceUrl, requestData);
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(arg1==null){
			arg1=LayoutInflater.from(mContext).inflate(R.layout.item_nianzhan, null);
			ViewHolder vh=new ViewHolder();
			vh.name=(TextView)arg1.findViewById(R.id.item_name);
			vh.time=(TextView)arg1.findViewById(R.id.item_time);
			vh.address=(TextView)arg1.findViewById(R.id.item_address);
			arg1.setTag(vh);
		}
		ViewHolder vh=(ViewHolder)arg1.getTag();
		vh.name.setText(mData.optJSONObject(arg0).optString("title"));
		vh.time.setText(mData.optJSONObject(arg0).optString("holdDate"));
		vh.address.setText(mData.optJSONObject(arg0).optString("address"));
		return arg1;
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
	
	private class ViewHolder{
		public TextView name;
		public TextView time;
		public TextView address;
	}
}
