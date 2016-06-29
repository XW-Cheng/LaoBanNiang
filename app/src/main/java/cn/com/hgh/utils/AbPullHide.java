package cn.com.hgh.utils;

import cn.com.hgh.view.AbPullToRefreshView;

public class AbPullHide {
	public static void hideRefreshView(boolean isRefreshing,
			AbPullToRefreshView abpulltorefreshview) {
		if (isRefreshing) {
			abpulltorefreshview.onHeaderRefreshFinish();
		} else {
			abpulltorefreshview.onFooterLoadFinish();
		}
	}
}
