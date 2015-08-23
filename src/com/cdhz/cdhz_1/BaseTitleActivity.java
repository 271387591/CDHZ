package com.cdhz.cdhz_1;

import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdhz.cdhz_1.R;
import com.hyc.androidcore.BaseActivity;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HASetListener;

public class BaseTitleActivity extends BaseActivity implements OnClickListener {

	@HASetListener(Id = R.id.left, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	public ImageView mLeft;

	@HAFindView(Id = R.id.title)
	public TextView mTitle;

	@HASetListener(Id = R.id.right, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	public ImageView mRight;

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.left) {
			this.finish();
		}
	}

	/**
	 * 
	 * @Title: setTitle
	 * @Description: 设置标题
	 * @param @param title 参数
	 * @return void 返回类型
	 * @author huangyc
	 * @date 2014-11-6 下午6:03:44
	 */
	public void setTitle(String title) {
		this.mTitle.setText(title);
	}

	/***
	 * 
	 * @Title: setLeftButton
	 * @Description: 设置左边按钮
	 * @param @param bitmapId
	 * @param @param visible 参数
	 * @return void 返回类型
	 * @throws
	 * @author huangyc
	 * @date 2014-11-6 下午6:06:43
	 */
	public void setLeftButton(int bitmapId, int visible) {
		if (bitmapId != 0) {
			mLeft.setImageResource(bitmapId);
			mLeft.setOnClickListener(this);
		}
		mLeft.setVisibility(visible);
	}

	/***
	 * 
	 * @Title: setRightButton
	 * @Description: 设置右边按钮
	 * @param @param bitmapId
	 * @param @param visible 参数
	 * @return void 返回类型
	 * @author huangyc
	 * @date 2014-11-6 下午6:06:54
	 */
	public void setRightButton(int bitmapId, int visible) {
		if (bitmapId != 0) {
			mRight.setImageResource(bitmapId);
			mRight.setOnClickListener(this);
		}
		mRight.setVisibility(visible);
	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
	}

}
