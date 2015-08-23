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

public class ZhanGuanAdapter extends BaseListViewPageAdapter {

	public ZhanGuanAdapter(Context context, RefreshListView listView,
			String interfaceUrl, JSONObject requestData) {
		super(context, listView, interfaceUrl, requestData);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_zhuanguan, null);
			ViewHolder vh=new ViewHolder();
			vh.pic=(AsyncImageView)convertView.findViewById(R.id.item_pic);
			vh.name=(TextView)convertView.findViewById(R.id.item_name);
			vh.flag=(TextView)convertView.findViewById(R.id.item_flag);
			vh.address=(TextView)convertView.findViewById(R.id.item_address);
			convertView.setTag(vh);
		}
		ViewHolder vh=(ViewHolder)convertView.getTag();
		vh.address.setText(mData.optJSONObject(position).optString("address"));
		vh.name.setText(mData.optJSONObject(position).optString("name"));
		vh.pic.setImageUrl(mData.optJSONObject(position).optString("logo1Url"), R.drawable.comm_zhanwei_1);
		if(mData.optJSONObject(position).optBoolean("hasExh", false)){
			vh.flag.setText("近期有展会");
		}else{
			vh.flag.setText("");
		}
		return convertView;
	}
	
	private class ViewHolder{
		public AsyncImageView pic;
		public TextView name;
		public TextView flag;
		public TextView address;
	}

}
