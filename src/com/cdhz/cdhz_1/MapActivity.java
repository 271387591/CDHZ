package com.cdhz.cdhz_1;

import android.os.Bundle;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;
import com.hyc.androidcore.annotation.BundleType;
import com.hyc.androidcore.annotation.HABundle;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HAInstanceState;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.cache.GlobalDataHelper;

@HALayout(layout=R.layout.activity_map)
public class MapActivity extends BaseTitleActivity {

	@HAFindView(Id = R.id.map)
	private MapView map;

	@HAInstanceState(type = BundleType.DOUBLE, name = "lat")
	@HABundle(type = BundleType.DOUBLE, name = "lat")
	private double positionLat = 30.679879f;

	@HAInstanceState(type = BundleType.DOUBLE, name = "lng")
	@HABundle(type = BundleType.DOUBLE, name = "lng")
	private double positionLng = 104.064855f;

	private AMap aMap;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		map.onCreate(savedInstanceState);// 此方法必须重写
	}

	@Override
	protected void init() {
		super.init();
		setRightButton(0, View.GONE);
		setTitle("位置");
		if (aMap == null) {
			aMap = map.getMap();
			LatLng v_point ;
			LatLngBounds.Builder bounds = new LatLngBounds.Builder();
			if (GlobalDataHelper.getInstance().containKey(
					APPConfig.USER_POSITION_geoLat)) {
				double lat = GlobalDataHelper.getInstance().getLong(
						APPConfig.USER_POSITION_geoLat) / 100000000.0f;
				double lng = GlobalDataHelper.getInstance().getLong(
						APPConfig.USER_POSITION_geoLng) / 100000000.0f;
				
				aMap.addMarker(new MarkerOptions()
						.anchor(0.5f, 0.5f).draggable(true)
						.position(v_point=new LatLng(lat, lng))
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.map_start)));
				bounds.include(v_point);
			}
			//
			aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 0.5f)
					.position(v_point=new LatLng(positionLat, positionLng))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_end)));
			
			bounds.include(v_point);
			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 10));
		}
	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		map.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		map.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		map.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		map.onDestroy();
	}
}
