package com.cdhz.cdhz_1.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.cdhz.cdhz_1.R;
import com.hyc.androidcore.comm.CommViewPagerAdapter;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.views.AsyncImageView;
import com.hyc.androidcore.views.RecyclableImageView;

public class AdWidget extends FrameLayout implements OnClickListener, OnPageChangeListener {

	/** 滑动圆点 ***/
	private LinearLayout mDots;

	private ViewPager mViewPager;
	/** 数据 ***/
	private JSONArray mData;
	/** 上下文 **/
	private Context mContext;
	/** OnItemClickListener接口 ***/
	private OnItemClickListener mListrner;

	private int mSelectedResource, mSelectResource;
	private int mDefaultResId;

	public AdWidget(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public AdWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	public AdWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 *            JSONArray数据
	 * @param dotSelectId
	 *            当前选中圆点 dot_1
	 * @param dotUnSelectId
	 *            未选中圆点 dot_0
	 */
	public void setData(JSONArray data, int dotSelectId, int dotUnSelectId, int defaultResId) {
		this.mData = data;
		this.mSelectedResource = dotSelectId;
		this.mSelectResource = dotUnSelectId;
		this.mDefaultResId = defaultResId;
		initView();
	}

	public void setSelectId(int id) {
		mViewPager.setCurrentItem(id);
	}

	public int getSelectId() {
		return mViewPager.getCurrentItem();
	}

	public int getViewPagerChildNum() {
		return mData.length() == 0 ? 1 : mData.length();
	}

	/**
	 * 刷新圆点
	 * 
	 * @param index
	 */
	private void refreshDot(int index) {
		int v_count = mDots.getChildCount();
		if (index < 0 || index > v_count) {
			return;
		}
		for (int i = 0; i < v_count; i++) {
			if (i == index) {
				((ImageView) mDots.getChildAt(i)).setImageResource(mSelectedResource);
			} else {
				((ImageView) mDots.getChildAt(i)).setImageResource(mSelectResource);
			}
		}
	}

	/**
	 * 初始化
	 */
	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_ad, this);
		mViewPager = (ViewPager) view.findViewById(R.id.start_viewpager);
		mDots = (LinearLayout) view.findViewById(R.id.start_dots);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mListrner = listener;
	}

	public interface OnItemClickListener {

		public void onItemClick(int id, String obj);

	}

	/**
	 * 
	 * 初始化界面
	 */
	private void initView() {
		List<View> mView = new ArrayList<View>(3);
		mDots.removeAllViews();
		if (mData.length() == 0) {
			RecyclableImageView v = new RecyclableImageView(mContext);
			v.setLayoutParams(new LayoutParams(-1, -1));
			v.setImageResource(mDefaultResId);
			v.setScaleType(ScaleType.CENTER_CROP);
			mView.add(v);
			mDots.setVisibility(View.GONE);
		} else {
			mDots.setVisibility(View.VISIBLE);
			int v_space = CommHelper.dip2px(mContext, 4);
			for (int i = 0; i < mData.length(); i++) {
				AsyncImageView v = new AsyncImageView(mContext);
				v.setLayoutParams(new LayoutParams(-1, -1));
				v.setImageUrl(mData.optString(i), mDefaultResId);
				v.setScaleType(ScaleType.FIT_CENTER);
				v.setId(i);
				v.setOnClickListener(this);
				mView.add(v);

				RecyclableImageView v_dot = new RecyclableImageView(mContext);
				v_dot.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
				v_dot.setPadding(v_space, 0, v_space, 0);
				v_dot.setImageResource(mSelectResource);
				mDots.addView(v_dot, i);
			}
			if (mData.length() <= 1) {
				mDots.setVisibility(View.GONE);
				mDots.removeAllViews();
			}
		}
		mViewPager.setAdapter(new CommViewPagerAdapter(mView));
		setSelectId(0);
		mViewPager.setOnPageChangeListener(this);
		refreshDot(0);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		refreshDot(arg0);
	}

	@Override
	public void onClick(View view) {
		if (mListrner != null) {
			mListrner.onItemClick(view.getId(), mData.optString(view.getId()));
		}
	}
}
