package com.shengtao.baseview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * 首页广告ViewPager
 * 
 */
public class HorizontalViewPager extends ViewPager {

	private int startX;
	private int startY;

	public HorizontalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalViewPager(Context context) {
		super(context);
	}

	/**
	 * 事件分发
	 * 
	 * 1. 上下滑动需要拦截 2. 左右滑动:如果当前是第一个页面,向右划需要拦截; 如果当前是最后一个页面,向左划,需要拦截
	 */
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		// 请求父控件和祖宗控件不要拦截事件
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			startX = (int) ev.getX();
//			startY = (int) ev.getY();
//			LogUtil.e("vpSnache", "down1");
//
//			break;
//		case MotionEvent.ACTION_MOVE:
//			int endX = (int) ev.getX();
//			int endY = (int) ev.getY();
//
//			int dx = endX - startX;
//			int dy = endY - startY;
//
//			int count = getAdapter().getCount();// 获取item总数
//
//			if (Math.abs(dx) > Math.abs(dy)) {// 左右滑动
////				if (getCurrentItem() == 0 && dx > 0) {// 第一个页面, 向右划
////					getParent().requestDisallowInterceptTouchEvent(false);// 需要拦截
////				} else if (getCurrentItem() == count - 1 && dx < 0) {// 最后一个页面,向左划
////					getParent().requestDisallowInterceptTouchEvent(false);// 需要拦截
////				}
//			} else {// 上下滑动
////				getParent().requestDisallowInterceptTouchEvent(false);// 需要拦截
//			}
//			break;
//
//		default:
//			break;
//		}
//
//		return super.dispatchTouchEvent(ev);
//	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		LogUtil.e("vpSnache",ev.getAction()+"lalala");
//		if(ev.getAction() == MotionEvent.ACTION_DOWN ){
//			return true;
//		}
//		return false;
//	}
}
