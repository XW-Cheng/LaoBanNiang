package com.lianbi.mezone.b.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import cn.com.hgh.indexscortlist.CharacterParser;
import cn.com.hgh.indexscortlist.ClearEditText;
import cn.com.hgh.indexscortlist.PinyinComparator;
import cn.com.hgh.indexscortlist.SideBar;
import cn.com.hgh.indexscortlist.SideBar.OnTouchingLetterChangedListener;
import cn.com.hgh.indexscortlist.SortAdapter;
import cn.com.hgh.utils.AbAppUtil;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.EasyDialog;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.SalesClerklist;
import com.lianbi.mezone.b.bean.SalesMan;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 店员管理
 */
@SuppressLint("ResourceAsColor")
public class EmployeeManageActivity extends BaseActivity {

	ClearEditText act_employee_manage_filter_edit;
	ListView act_employee_manage_country_lvcountry;
	SideBar act_employee_manage_sidrbar;
	TextView act_employee_manage_dialog;
	FrameLayout frame_employee_manage_have;
	private ImageView img_employee_empty;
	private SortAdapter adapter;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SalesMan> SourceDateList = new ArrayList<SalesMan>();

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	/**
	 * 店铺员工信息
	 */
	private ArrayList<SalesMan> salesList = new ArrayList<SalesMan>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		setContentView(R.layout.act_employee_manage, NOTYPE);
		initView();
	}

	private void initView() {
		setPageTitle("店员管理");
		setPageRightText("新增");
		img_employee_empty = (ImageView) findViewById(R.id.img_employee_empty);
		setPageRightTextColor(R.color.colores_news_01);
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getemployeeEmployeelist();
	}

	/**
	 * 加载雇员列表
	 */
	private void getemployeeEmployeelist() {
		okHttpsImp.getSalesClerklist(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String data = result.getData();
				SalesClerklist salesList1 = JSON.parseObject(data,
						SalesClerklist.class);
				salesList = salesList1.getSalesclerkList();
				if (salesList != null && salesList.size() > 0) {
					frame_employee_manage_have.setVisibility(View.VISIBLE);
					img_employee_empty.setVisibility(View.GONE);
					refreshData(salesList);
				} else {
					frame_employee_manage_have.setVisibility(View.GONE);
					img_employee_empty.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onResponseFailed(String msg) {

			}
		}, userShopInfoBean.getBusinessId());
	}

	/**
	 * 新增
	 */
	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		Intent intent_add = new Intent(this, AddEmployeeActivity.class);
		startActivity(intent_add);
	}

	/**
	 * 字母化数据
	 */
	private void refreshData(ArrayList<SalesMan> employees) {
		SourceDateList.clear();
		SourceDateList.addAll(employees);
		filledData(SourceDateList);
		Collections.sort(SourceDateList, pinyinComparator);
		adapter.updateListView(SourceDateList);
	}

	private void initViews() {
		act_employee_manage_filter_edit = (ClearEditText) findViewById(R.id.act_employee_manage_filter_edit);
		act_employee_manage_country_lvcountry = (ListView) findViewById(R.id.act_employee_manage_country_lvcountry);
		act_employee_manage_sidrbar = (SideBar) findViewById(R.id.act_employee_manage_sidrbar);
		act_employee_manage_dialog = (TextView) findViewById(R.id.act_employee_manage_dialog);
		frame_employee_manage_have = (FrameLayout) findViewById(R.id.frame_employee_manage_have);
		act_employee_manage_sidrbar.setTextView(act_employee_manage_dialog);
		adapter = new SortAdapter(this, SourceDateList);
		act_employee_manage_country_lvcountry.setAdapter(adapter);
		listen();
	}

	private void listen() {
		// 根据输入框输入值的改变来过滤搜索
		act_employee_manage_filter_edit
				.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
						filterData(s.toString());
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
		act_employee_manage_filter_edit
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE
								|| actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
							String response = act_employee_manage_filter_edit
									.getText().toString().trim();
							filterData(response);
						}
						AbAppUtil.closeSoftInput(EmployeeManageActivity.this);
						return false;
					}
				});
		// 设置右侧触摸监听
		act_employee_manage_sidrbar
				.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

					@Override
					public void onTouchingLetterChanged(String s) {
						// 该字母首次出现的位置
						int position = adapter.getPositionForSection(s
								.charAt(0));
						if (position != -1) {
							act_employee_manage_country_lvcountry
									.setSelection(position);
						}

					}
				});

		act_employee_manage_country_lvcountry
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
						Intent intent_detail = new Intent(
								EmployeeManageActivity.this,
								SalesDetailActivity.class);
						intent_detail.putExtra("SalesMan",
								adapter.getItem(position));
						EmployeeManageActivity.this
								.startActivity(intent_detail);
					}
				});
		act_employee_manage_country_lvcountry
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						deleteItem(adapter.getItem(arg2).getSalesclerk_id(),
								arg1);
						return true;
					}
				});
	}

	/**
	 * 
	 * @param ids删除店员
	 * @param p
	 */
	EasyDialog ers;

	/**
	 * 删除店员对话框
	 * 
	 * @param ids
	 *            店员id
	 * @param arg1
	 */
	protected void deleteItem(final int ids, View arg1) {
		View layout = LayoutInflater.from(EmployeeManageActivity.this).inflate(
				R.layout.dialog_delete, null);
		TextView tv_dialogtext = (TextView) layout
				.findViewById(R.id.tv_dialogtext);
		TextView tv_delete_cancle = (TextView) layout
				.findViewById(R.id.tv_delete_cancle);
		TextView tv_delete_ok = (TextView) layout
				.findViewById(R.id.tv_delete_ok);
		tv_dialogtext.setText("确定删除店员？");
		ers = showDioag(arg1, layout);
		tv_delete_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ers.dismiss();
			}
		});
		tv_delete_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 掉删除接口
				ers.dismiss();
				getemployeeEmployeedel(ids);
			}
		});

	}

	/**
	 * 删除店员
	 */
	private void getemployeeEmployeedel(final int ids) {
		okHttpsImp.postDelsalesClerkbyid(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				Toast.makeText(EmployeeManageActivity.this, "删除店员成功",
						Toast.LENGTH_SHORT).show();
				getemployeeEmployeelist();
			}

			@Override
			public void onResponseFailed(String msg) {
				Toast.makeText(EmployeeManageActivity.this, "删除店员失败",
						Toast.LENGTH_SHORT).show();

			}
		}, ids);
	}

	private EasyDialog showDioag(View arg1, View layout) {
		EasyDialog es = new EasyDialog(EmployeeManageActivity.this)
				.setLayout(layout)
				.setBackgroundColor(
						EmployeeManageActivity.this.getResources().getColor(
								R.color.white))
				.setLocationByAttachedView(arg1)
				.setAnimationTranslationShow(EasyDialog.DIRECTION_Y, 1000,
						-AbViewUtil.dip2px(this, 1000), 100, -50, 50, 0)
				.setAnimationTranslationDismiss(EasyDialog.DIRECTION_Y, 500, 0,
						-AbViewUtil.dip2px(this, 1000))
				.setGravity(EasyDialog.GRAVITY_TOP)
				.setTouchOutsideDismiss(true)
				.setMatchParent(false)
				.setMarginLeftAndRight(24, 24)
				.setOutsideColor(
						EmployeeManageActivity.this.getResources().getColor(
								R.color.color_8000)).show();
		return es;

	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */

	private List<SalesMan> filledData(List<SalesMan> nl) {
		int length = nl.size();
		for (int i = 0; i < length; i++) {
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(nl.get(i)
					.getSalesclerk_name());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				nl.get(i).setSortLetters(sortString.toUpperCase());
			} else {
				nl.get(i).setSortLetters("#");
			}
		}
		return nl;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SalesMan> filterDateList = new ArrayList<SalesMan>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SalesMan sortModel : SourceDateList) {
				String name = sortModel.getSalesclerk_name();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
