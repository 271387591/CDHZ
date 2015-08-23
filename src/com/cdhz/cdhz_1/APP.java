package com.cdhz.cdhz_1;

import com.hyc.androidcore.BaseApplication;
import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.db.DBHelper;
import com.hyc.androidcore.log.DefalutLogger;
import com.hyc.androidcore.net.NetHelper;

public class APP extends BaseApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		
		DBHelper.createInstance(this, "bb", 1);
		DBHelper.getInstance().addEntityClass(Propertity.class);

	}

}
