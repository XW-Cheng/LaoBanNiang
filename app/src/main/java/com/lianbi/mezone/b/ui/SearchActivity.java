package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.com.hgh.indexscortlist.ClearEditText;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.TableSetBean;

public class SearchActivity extends BaseActivity implements
		OnEditorActionListener {

	private ClearEditText searchview;
	private TextView tv_cancel_sousuo;
	private ArrayList<TableSetBean> mTableInfomation = new ArrayList<TableSetBean>();
	private ImageView iv_searchtable;
	private LinearLayout ll_searchtable;
	//表示有匹配的
	private boolean  ishaveate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_searchactivity, NOTYPE);

		initView();
		initData();
		setLisenter();
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		setPageTitle("搜索");
		mTableInfomation = (ArrayList<TableSetBean>) this.getIntent()
				.getSerializableExtra("TABLLIST");
		searchview = (ClearEditText) findViewById(R.id.searchview);
		tv_cancel_sousuo = (TextView) findViewById(R.id.tv_cancel_sousuo);
		iv_searchtable = (ImageView) findViewById(R.id.iv_searchtable);
		ll_searchtable = (LinearLayout) findViewById(R.id.ll_searchtable);
	}

	private void initData() {

	}

	private void setLisenter() {
		searchview.setOnEditorActionListener(this);
		tv_cancel_sousuo.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.tv_cancel_sousuo:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {

			// 先隐藏键盘
			((InputMethodManager) searchview.getContext().getSystemService(
					INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

			String searchresult = searchview.getText().toString().trim();
			for(int i=0;i<mTableInfomation.size();i++){
				
				if(!indexOfString(mTableInfomation.get(i).getTableName(),searchresult)){
					Log.i("trg","查询是否包含该字符92"+mTableInfomation.get(i).getTableName()+" "
							+searchresult
							);
//					iv_searchtable.setVisibility(View.VISIBLE);
//					return false;
				}else{
					// 跳转到搜索到结果的activity
					Log.i("trg","查询是否包含该字符96"+mTableInfomation.get(i).getTableName()+" "
							+searchresult
							);
					ishaveate=true;
					iv_searchtable.setVisibility(View.GONE);
					Intent intent = new Intent(this, TableSetActivity.class);
					intent.putExtra("searchresult", searchresult);
					setResult(RESULT_OK, intent);
					finish();
				}
				
//				if(!mTableInfomation.get(i).getTableName().contains(searchresult)){
//					iv_searchtable.setVisibility(View.VISIBLE);
//					return false;
//				}else{
//					// 跳转到搜索到结果的activity
//					iv_searchtable.setVisibility(View.GONE);
//					Intent intent = new Intent(this, TableSetActivity.class);
//					intent.putExtra("searchresult", searchresult);
//					setResult(RESULT_OK, intent);
//					finish();
//				}
			}
			if(ishaveate==false){
				iv_searchtable.setVisibility(View.VISIBLE);
			}
			return true;
		}
		return false;
	}
	/*
	 * 判断字符串是否包含一些字符 indexOf
	 */
	public static boolean indexOfString(String src, String dest) {
		boolean flag = false;
		if (src.indexOf(dest)!=-1) {
			Log.i("trg","包含该字符");
			flag = true;
		}
		return flag;
	}

}
