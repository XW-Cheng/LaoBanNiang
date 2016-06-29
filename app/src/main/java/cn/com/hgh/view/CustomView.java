package cn.com.hgh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

public class CustomView extends LinearLayout {
	/**
	 * 滚动控制器
	 */
	private Scroller mScroller;

	public CustomView(Context context) {
		this(context, null);
	}

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		/**
		 * 这两个方法必须有
		 */
		setClickable(true);
		setLongClickable(true);
		mScroller = new Scroller(context);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initContentAdapterView();
	}

	/** list or grid */
	private AdapterView<?> mAdapterView;

	/** Scrollview */
	private ScrollView mScrollView;
	/**
	 * 是否是除了这两种之外的布局
	 */
	private boolean isNomAdapterView_mScrollView = false;

	/**
	 * init AdapterView like ListView, GridView and so on; or init ScrollView.
	 */
	private void initContentAdapterView() {
		int count = getChildCount();
		View view = null;
		for (int i = 0; i <= count - 1; ++i) {
			view = getChildAt(i);
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;
			}
			if (view instanceof ScrollView) {
				mScrollView = (ScrollView) view;
			}
		}
		if (mAdapterView == null && mScrollView == null) {
			isNomAdapterView_mScrollView = true;
		}
	}

	/**
	 * 判断滑动方向，和是否响应事件.
	 * 
	 * @param deltaY
	 *            deltaY > 0 是向下运动,< 0是向上运动
	 * @return true, if is refresh view scroll
	 */
	private boolean isRefreshViewScroll(int deltaY) {
		// 对于ListView和GridView
		if (mAdapterView != null) {
			// 子view(ListView or GridView)滑动到最顶端
			if (deltaY > mDistance) {
				View child = mAdapterView.getChildAt(0);
				if (child == null) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& child.getTop() == 0) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				int top = child.getTop();
				int padding = mAdapterView.getPaddingTop();
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& Math.abs(top - padding) <= 11) {// 这里之前用3可以判断,但现在不行,还没找到原因
					mPullState = PULL_DOWN_STATE;
					return true;
				}

			} else if (deltaY < -mDistance) {
				View lastChild = mAdapterView.getChildAt(mAdapterView
						.getChildCount() - 1);
				if (lastChild == null) {
					mPullState = PULL_UP_STATE;
					return true;
				}

				// 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
				// 等于父View的高度说明mAdapterView已经滑动到最后
				if (lastChild.getBottom() <= getHeight()
						&& mAdapterView.getLastVisiblePosition() == mAdapterView
								.getCount() - 1) {
					mPullState = PULL_UP_STATE;
					return true;
				}
			}
		}
		// 对于ScrollView
		if (mScrollView != null) {
			// 子scroll view滑动到最顶端
			View child = mScrollView.getChildAt(0);
			if (deltaY > mDistance && mScrollView.getScrollY() == 0) {
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < -mDistance
					&& child.getMeasuredHeight() <= getHeight()
							+ mScrollView.getScrollY()) {
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/** 滑动状态. */
	private int mPullState;

	/** 上滑动作. */
	private static final int PULL_UP_STATE = 0;

	/** 下拉动作. */
	private static final int PULL_DOWN_STATE = 1;
	/** y上一次保存的. */
	private int mLastMotionY;

	/**
	 * 调用此方法滚动到目标位置
	 * 
	 * @param fx
	 * @param fy
	 */
	public void smoothScrollTo(int fx, int fy) {
		int dx = fx - mScroller.getFinalX();
		int dy = fy - mScroller.getFinalY();
		smoothScrollBy(dx, dy);
	}

	/**
	 * 调用此方法设置滚动的相对偏移
	 * 
	 * @param dx
	 * @param dy
	 */
	public void smoothScrollBy(int dx, int dy) {

		// 设置mScroller的滚动偏移量
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx,
				dy);
		invalidate();// 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
	}

	@Override
	public void computeScroll() {
		// 先判断mScroller滚动是否完成
		if (mScroller.computeScrollOffset()) {
			// 这里调用View的scrollTo()完成实际的滚动
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// 必须调用该方法，否则不一定能看到滚动效果
			postInvalidate();
		}
		super.computeScroll();
	}

	/**
	 * 手指移动距离
	 */
	final int mDistance = 15;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int y = (int) e.getRawY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 首先拦截down事件,记录y坐标
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// deltaY > 0 是向下运动,deltaY< 0是向上运动
			int deltaY = y - mLastMotionY;
			if (isNomAdapterView_mScrollView) {
				if (deltaY > mDistance) {
					mPullState = PULL_DOWN_STATE;
				} else {
					mPullState = PULL_UP_STATE;
				}
				return true;
			} else {
				if (isRefreshViewScroll(deltaY)) {
					return true;
				}
			}
			break;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			if (mPullState == PULL_DOWN_STATE) {
				// 执行下拉
				smoothScrollBy(0, -deltaY);
			} else if (mPullState == PULL_UP_STATE) {
				// 执行上拉
				smoothScrollBy(0, -deltaY);
			}
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
			smoothScrollTo(0, 0);
			break;
		}
		return super.onTouchEvent(event);
	}

}