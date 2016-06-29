package com.lianbi.mezone.b.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.view.PagerSlidingTabStrip;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.fragment.MineServiceFragment;
import com.lianbi.mezone.b.fragment.OnLineServiceFragment;

/**
 * 服务商城
 * 
 * @author guanghui.han
 * 
 */
public class ServiceShopActivity extends BaseActivity {
	final String[] titles = { "我的服务", "线上服务" };
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	public static final int POSITION0 = 0;
	public static final int POSITION1 = 1;
	MineServiceFragment mineServiceFragment;
	OnLineServiceFragment onLineServiceFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(RESULT_OK);
		setContentView(R.layout.act_serviceshopactivity, NOTYPE);
		initView();
	}

	private void initView() {
		setPageTitle("服务商城");
		pager = (ViewPager) findViewById(R.id.pager_act_serviceshopactivity);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_act_serviceshopactivity);
		tabs.setTextSize((int) AbViewUtil.sp2px(this, 15));
		pager.setAdapter(new MyAdapter(getSupportFragmentManager(), titles));
		tabs.setViewPager(pager);
		tabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					if (mineServiceFragment != null) {
						mineServiceFragment.reFresh();
					}
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	public class MyAdapter extends FragmentPagerAdapter {
		String[] _titles;

		public MyAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			_titles = titles;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return _titles[position];
		}

		@Override
		public int getCount() {
			return _titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case POSITION0:
				if (mineServiceFragment == null) {
					mineServiceFragment = new MineServiceFragment();
				}
				return mineServiceFragment;
			case POSITION1:
				if (onLineServiceFragment == null) {
					onLineServiceFragment = new OnLineServiceFragment();
				}
				return onLineServiceFragment;
			}
			return null;
		}
	}
}
