package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.ActionPBean;

/**
 * 功能介绍
 * 
 * @time 下午4:36:43
 * @date 2016-1-12
 * @author hongyu.yang
 * 
 */
public class ActionProduceActivity extends BaseActivity {
	ListView list_bank;
	private ArrayList<ActionPBean> arrayList_all = new ArrayList<ActionPBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actionproduceactivity_list, NOTYPE);
		initView();
		initAdapter();
	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("功能介绍");
		list_bank = (ListView) findViewById(R.id.activity_actionproduceactivity_list_list);
		getBankBanklist();

	}

	/**
	 * 获取数据
	 */
	private void getBankBanklist() {
		String[] name_items = getResources().getStringArray(R.array.acpda_name);
		String[] content_items = getResources().getStringArray(
				R.array.acpda_content);
		int s = name_items.length;
		for (int i = 0; i < s; i++) {
			arrayList_all.add(new ActionPBean(name_items[i], content_items[i]));
		}
	}

	QuickAdapter<ActionPBean> adapter;

	/**
	 * 初始化Adapter
	 */
	private void initAdapter() {
		adapter = new QuickAdapter<ActionPBean>(this,
				R.layout.item_apa_list, arrayList_all) {

			@Override
			protected void convert(BaseAdapterHelper helper, ActionPBean item) {
				helper.setText(R.id.item_apa_list_name, item.getName());
				helper.setText(R.id.item_apa_list_content, item.getContent());
			}
		};
		list_bank.setAdapter(adapter);
	}
}
