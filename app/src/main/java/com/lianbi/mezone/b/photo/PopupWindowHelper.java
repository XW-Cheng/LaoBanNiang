package com.lianbi.mezone.b.photo;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.PopupWindow;

/**
 * PopupWindow工具类
 * 
 * @author Noah
 * 
 */
public class PopupWindowHelper {
	/**
	 * 显示弹出窗口在位置
	 * 
	 * @param contentView
	 *            refer to {@link #createPopupWindow}
	 * @param width
	 *            refer to {@link #createPopupWindow}
	 * @param height
	 *            refer to {@link #createPopupWindow}
	 * @param parent
	 *            refer to {@link PopupWindow#showAtLocation}
	 * @param gravity
	 *            refer to {@link PopupWindow#showAtLocation}
	 * @param x
	 *            refer to {@link PopupWindow#showAtLocation}
	 * @param y
	 *            refer to {@link PopupWindow#showAtLocation}
	 * @return
	 */
	public static PopupWindow showAtLocation(View contentView, int width,
			int height, View parent, int gravity, int x, int y) {
		return showAtLocation(contentView, width, height, parent, gravity, y,
				y, 0);
	}

	/**
	 * 显示弹出窗口在位置
	 * 
	 * @param contentView
	 *            refer to {@link #createPopupWindow}
	 * @param width
	 *            refer to {@link #createPopupWindow}
	 * @param height
	 *            refer to {@link #createPopupWindow}
	 * @param parent
	 *            refer to {@link PopupWindow#showAtLocation}
	 * @param gravity
	 *            refer to {@link PopupWindow#showAtLocation}
	 * @param x
	 *            refer to {@link PopupWindow#showAtLocation}
	 * @param y
	 *            refer to {@link PopupWindow#showAtLocation}
	 * @param animationStyle
	 *            refer to {@link PopupWindow#setAnimationStyle}
	 * @return
	 */
	public static PopupWindow showAtLocation(View contentView, int width,
			int height, View parent, int gravity, int x, int y,
			int animationStyle) {
		PopupWindow pw = createPopupWindow(contentView, width, height);
		if (animationStyle != 0)
			pw.setAnimationStyle(animationStyle);
		pw.showAtLocation(parent, gravity, x, y);
		return pw;
	}

	/**
	 * 显示弹出窗口为下拉
	 * 
	 * @param contentView
	 *            refer to {@link #createPopupWindow}
	 * @param width
	 *            refer to {@link #createPopupWindow}
	 * @param height
	 *            refer to {@link #createPopupWindow}
	 * @param anchor
	 *            refer to {@link PopupWindow#showAsDropDown}
	 * @return
	 */
	public static PopupWindow showAsDropDown(View contentView, int width,
			int height, View anchor) {
		return showAsDropDown(contentView, width, height, anchor, 0, 0);
	}

	public static PopupWindow showAsDropDown(View contentView, int width,
			int height, View anchor, int xoff, int yoff) {
		PopupWindow pw = createPopupWindow(contentView, width, height);
		pw.showAsDropDown(anchor, xoff, yoff);
		return pw;
	}

	/**
	 * 创建弹出窗口
	 * 
	 * @param contentView
	 *            refer to {@link PopupWindow#PopupWindow(View, int, int)}
	 * @param width
	 *            refer to {@link PopupWindow#PopupWindow(View, int, int)}
	 * @param height
	 *            refer to {@link PopupWindow#PopupWindow(View, int, int)}
	 * @return
	 */
	public static PopupWindow createPopupWindow(View contentView, int width,
			int height) {
		final PopupWindow popupWindow = new PopupWindow(contentView, width,
				height);
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		return popupWindow;
	}

	/**
	 * 计算窗口y轴上的真实偏移像素
	 * 
	 * @param window
	 * @param yOffset
	 * @return 添加上状态栏等高度后的高度
	 */
	public static int calcYOffsetInWindow(Window window, int yOffset) {
		Rect frame = new Rect();
		window.getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top + yOffset;
	}
}
