package com.lianbi.mezone.b.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.Result;

import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.BankCardList;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

/**
 * 选择开户行
 * 
 * @time 下午4:36:43
 * @date 2016-1-12
 * @author hongyu.yang
 * 
 */
public class BankListActivity extends BaseActivity {
	ListView list_bank;
	EditText edit_bank_list;
	ImageView iv_banklist_delete;
	private ArrayList<BankCardList> arrayList;
	private ArrayList<BankCardList> arrayList_search;
	private ArrayList<BankCardList> arrayList_all;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bank_list, HAVETYPE);
		initView();
		initAdapter();
		getBankBanklist();
	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("开户行");
		list_bank = (ListView) findViewById(R.id.list_bank);
		edit_bank_list = (EditText) findViewById(R.id.edit_bank_list);
		iv_banklist_delete = (ImageView) findViewById(R.id.iv_banklist_delete);
		arrayList = new ArrayList<BankCardList>();
		arrayList_search = new ArrayList<BankCardList>();
		arrayList_all = new ArrayList<BankCardList>();
		setLisenter();
	}

	/**
	 * ListView监听
	 */
	private void setLisenter() {
		iv_banklist_delete.setOnClickListener(this);
		list_bank.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("bankname", arrayList.get(position).getName());
				intent.putExtra("bankid", arrayList.get(position).getId());
				intent.putExtra("bankImg", arrayList.get(position).getBankImg());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		edit_bank_list.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (arrayList_all.size() > 0) {
					if (!TextUtils.isEmpty(s.toString())) {
						arrayList.clear();
						arrayList_search.clear();
						for (int i = 0; i < arrayList_all.size(); i++) {
							if (arrayList_all.get(i).getName()
									.contains(s.toString())) {
								arrayList_search.add(arrayList_all.get(i));
							}
						}
						arrayList.addAll(arrayList_search);
						adapter.replaceAll(arrayList);
					} else {
						arrayList.clear();
						arrayList.addAll(arrayList_all);
						adapter.replaceAll(arrayList);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					iv_banklist_delete.setVisibility(View.VISIBLE);
				} else {
					iv_banklist_delete.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.iv_banklist_delete:
			edit_bank_list.setText("");
			if (TextUtils.isEmpty(edit_bank_list.getText().toString().trim())) {
				edit_bank_list.setHint(getResources().getString(
						R.string.search_bank));
			}
			break;
		}
	}

	/**
	 * 获取银行列表
	 */
	
	

	private void getBankBanklist() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getAllBank(
					OkHttpsImp.md5_key,
					uuid,"app",reqTime,
					new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					try {
						JSONObject jbB = new JSONObject(result.getData());
						String reStr = jbB.getString("list");
						
						List<BankCardList> curArray = com.alibaba.fastjson.JSONObject
								.parseArray(reStr, BankCardList.class);
						arrayList.clear();
						arrayList_all.clear();
						arrayList.addAll(curArray);
						arrayList_all.addAll(curArray);
						adapter.replaceAll(arrayList);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			   
				@Override
				public void onResponseFailed(String msg) {

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	QuickAdapter<BankCardList> adapter;

	/**
	 * 初始化Adapter
	 */
	private void initAdapter() {
		adapter = new QuickAdapter<BankCardList>(this, R.layout.item_bank_card,
				arrayList) {

			@Override
			protected void convert(BaseAdapterHelper helper, BankCardList item) {
				TextView tv_bank_card = helper.getView(R.id.tv_bank_card);
				ImageView img_bank_card = helper.getView(R.id.img_bank_card);
				String url = item.getImgurl();
				Glide.with(context).load(url).error(R.mipmap.bankdefault)
						.into(img_bank_card);
				tv_bank_card.setText(item.getName());
			}
		};
		list_bank.setAdapter(adapter);
	}
}
