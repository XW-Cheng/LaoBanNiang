package com.lianbi.mezone.b.ui;

import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.Request;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbAppUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.CryptTool;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.EasyDialog;
import cn.com.hgh.view.HttpDialog;
import cn.com.hgh.view.NoScrollViewPager;
import cn.com.hgh.view.SlideSwitch;
import cn.com.hgh.view.SlideSwitch.SlideListener;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.DataObjecte;
import com.lianbi.mezone.b.bean.TableSetBean;
import com.lianbi.mezone.b.fragment.InBusinessFragment;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

public class TableSetActivity extends BaseActivity implements
		OnPageChangeListener, SlideListener {

	private TextView tv_searchtablenum, tv_mingxi, tv_pendingorders,
			tv_service, tv_check, tv_batchprocessing, tv_addtableset;
	private SlideSwitch slide;
	public NoScrollViewPager viewpager;
	public static final int POSITION0 = 0;
	public static final int POSITION1 = 1;
	private MyAdapter adapter;
//	HttpDialog dialog;
	QuickAdapter<TableSetBean> mAdapter;
	private InBusinessFragment mInBusinessFragment;
	private InBusinessFragment mInRestFragment;
	private static final int REQUEST_CODE_SEARCH_RESULT = 1009;
	private static final int REQUEST_CODE_ADDTABLE_RESULT = 1010;
	public int i = 0;
	private ImageView iv_delete_table, iv_addtableset;
	public boolean isSelected = false;
	private ArrayList<TableSetBean> mInBusinessData = new ArrayList<TableSetBean>();
	private ArrayList<TableSetBean> mInRestDatas = new ArrayList<TableSetBean>();
	private ArrayList<TableSetBean> mSearchRsult = new ArrayList<TableSetBean>();
	private ArrayList<TableSetBean> mDatas = new ArrayList<TableSetBean>();
	private ArrayList<TableSetBean> mIntentSearch= new ArrayList<TableSetBean>();

	private boolean Isdeletedsucces = false;
	/**
	 * 当前位置
	 */
	public int curPosition;
	// 营业
	private final String DOBUSINESS = "1";
	// 休息
	private final String ATREST = "0";
	private boolean isOne = true;
	//区分界面是否刷新
	private String   showstatus="";
	private boolean realtimerefresh = true;
	//表示是搜索界面返回的
	private boolean isSearchback = false;
	EasyDialog ers;
	/**
	 * 网络连接状态
	 */
	public boolean ISCONNECTED;
	String tablname ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_tableset, NOTYPE);
		initView();
		initAdapter();
		initListen();
//		dialog = new HttpDialog(this);
//		dialog.show();
		getTableinfo();
	}
       Runnable   mR=new Runnable() {
		
		@Override
		public void run() {
              while(realtimerefresh){
            	  try {
            		  Thread.sleep(5*1000);
				} catch (Exception e) {
                    e.printStackTrace();
				}
                mHandler.sendMessage(mHandler.obtainMessage());            	  
              }
		}
	};
	@SuppressLint("HandlerLeak")
	Handler  mHandler=new Handler(){
		
		public void handleMessage(Message msg) {
			Log.i("tag","运行获取桌子");
            if(realtimerefresh==true){
			   getTableinfo();
            }
		};
	};
	
	public void getTableinfo() {
		okHttpsImp.getTableInfo(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				Log.i("tag", "成功");
				String reString = result.getData();
				int isfbusiness;
				System.out.println("reString-----" + reString);
				if (reString != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(reString);
						isfbusiness = jsonObject.getInt("storeOpenSts");
						reString = jsonObject.getString("tableList");
						if (!TextUtils.isEmpty(reString)) {
							mIntentSearch.clear();
							switch (isfbusiness) {
							case 1:
								mInBusinessData.clear();
								ArrayList<TableSetBean> inBusinessList = (ArrayList<TableSetBean>) JSON
										.parseArray(reString,
												TableSetBean.class);
								mIntentSearch.addAll(inBusinessList);
								if(isSearchback==true){
									mSearchRsult.clear();
									for (int i = 0; i < inBusinessList.size(); i++) {
										if (inBusinessList.get(i).getTableName()
												.contains(tablname)) {
											mSearchRsult.add(inBusinessList.get(i));
											swtFmDo(curPosition, false, false, mSearchRsult);
										}
									}
								}else{
									isSearchback=false;
									mInBusinessData.addAll(inBusinessList);
									swtFmDo(POSITION0, false, false,
											mInBusinessData);
								}
								viewpager.setCurrentItem(0);
								break;
							case 0:
								mInRestDatas.clear();
								ArrayList<TableSetBean> inRestDatasList = (ArrayList<TableSetBean>) JSON
										.parseArray(reString,
												TableSetBean.class);
								mIntentSearch.addAll(inRestDatasList);
								if(isSearchback==true){	
									mSearchRsult.clear();
									for (int i = 0; i < inRestDatasList.size(); i++) {
										if (inRestDatasList.get(i).getTableName()
												.contains(tablname)) {
											mSearchRsult.add(inRestDatasList.get(i));
											swtFmDo(curPosition, false, false, mSearchRsult);
										}
									}
								}else{
									isSearchback=false;
									mInRestDatas.addAll(inRestDatasList);
									swtFmDo(POSITION1, false, false, mInRestDatas);
								}
								viewpager.setCurrentItem(1);
								break;
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
//				dialog.dismiss();
			}

			@Override
			public void onResponseFailed(String msg) {
				Log.i("tag", "失败" + msg);
//				dialog.dismiss();

			}
			@Override
			public void onBefore(Request request) {
			}
			@Override
			public void onAfter() {
			}			
		}, userShopInfoBean.getBusinessId());

	}

	private void swtFmDo(int position, boolean isD, boolean isDel,
			ArrayList<TableSetBean> arrayList) {
		switch (position) {
		case POSITION0:
			if (mInBusinessFragment != null) {
				mInBusinessFragment.doSomthing(isD, isDel, arrayList, position);
			}
			break;

		case POSITION1:
			if (mInRestFragment != null) {
				mInRestFragment.doSomthing(isD, isDel, arrayList, position);
			}
			break;
		}
	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		Intent intent = new Intent(this, InfoDetailsActivity.class);
		startActivity(intent);
	}

	private void initView() {
		setPageTitle("桌位设置");
		setPageRightText("信息");
		// setPageRightResource(R.drawable.more_other);
		tv_searchtablenum = (TextView) findViewById(R.id.tv_searchtablenum);// 搜索框
		slide = (SlideSwitch) findViewById(R.id.swit2);
		viewpager = (NoScrollViewPager) findViewById(R.id.viewpager);
		tv_pendingorders = (TextView) findViewById(R.id.tv_pendingorders);// 带接单
		tv_service = (TextView) findViewById(R.id.tv_service);// 服务
		tv_check = (TextView) findViewById(R.id.tv_check);// 买单
		tv_batchprocessing = (TextView) findViewById(R.id.tv_batchprocessing);// 批量处理
		tv_addtableset = (TextView) findViewById(R.id.tv_addtableset);// 添加桌位
		iv_addtableset = (ImageView) findViewById(R.id.iv_addtableset);// 添加桌位
		iv_delete_table = (ImageView) findViewById(R.id.iv_delete_table);
	}

	@Override
	protected void onTitleRightClick1() {
		super.onTitleRightClick1();
		// Intent intent = new Intent(this, InfoDetailsActivity.class);
		// startActivity(intent);
	}

	private void initListen() {
		tv_searchtablenum.setOnClickListener(this);// 搜索框
		tv_pendingorders.setOnClickListener(this);
		tv_service.setOnClickListener(this);
		tv_check.setOnClickListener(this);
		tv_addtableset.setOnClickListener(this);
		tv_batchprocessing.setOnClickListener(this);
		slide.setContext(this);
		slide.setState(false);
		slide.setSlideListener(this);
		iv_delete_table.setOnClickListener(this);
		iv_addtableset.setOnClickListener(this);
	}

	private void initAdapter() {
		adapter = new MyAdapter(fm);
		viewpager.setAdapter(adapter);
		viewpager.setCurrentItem(0);
		viewpager.setOnPageChangeListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.tv_searchtablenum:// 搜索框

			Intent intent = new Intent(this, SearchActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("TABLLIST", (Serializable) mIntentSearch);
			intent.putExtras(bundle);
			startActivityForResult(intent, REQUEST_CODE_SEARCH_RESULT);
			break;

		case R.id.tv_pendingorders:// 待接单
			Intent intent_web = new Intent(this, WebActivty.class);
			intent_web.putExtra(Constants.NEDDLOGIN, false);
			intent_web.putExtra("NEEDNOTTITLE", false);
			intent_web.putExtra("Re", true);
			intent_web.putExtra(WebActivty.U, getUrl());
			startActivity(intent_web);
			break;
		case R.id.tv_service:// 服务
			Intent intent3 = new Intent(this, InfoDetailsActivity.class);
			intent3.putExtra("TIAOZHUANXIAOXI", "fuwu");
			startActivity(intent3);
			break;
		case R.id.tv_check:// 买单
			Intent intent4 = new Intent(this, InfoDetailsActivity.class);
			intent4.putExtra("TIAOZHUANXIAOXI", "maidan");
			startActivity(intent4);
			break;
		case R.id.tv_batchprocessing:// 批量处理
			for (int i = 0; i < mInBusinessData.size(); i++) {
				mInBusinessData.get(i).setTableStatus(1);
			}
			if (curPosition != POSITION1) {
				boolean isBatch = true;
				swtFmDo(POSITION0, false, false, mInBusinessData);
				mInBusinessFragment.modifyTableStatus("", isBatch,"");
			}
			// 通知服务器
			break;
		case R.id.tv_addtableset:// 添加桌位
			// Intent intent1 = new Intent(this, AddTablesetActivity.class);
			// startActivity(intent1);
			startActivityForResult(new Intent(this, AddTablesetActivity.class),
					REQUEST_CODE_ADDTABLE_RESULT);

			break;
		case R.id.iv_addtableset:// 添加桌位
			// Intent intent2 = new Intent(this, AddTablesetActivity.class);
			// startActivity(intent2);
			startActivityForResult(new Intent(this, AddTablesetActivity.class),
					REQUEST_CODE_ADDTABLE_RESULT);
			break;
		case R.id.iv_delete_table:
			if (isDeteled) {
				isDeteled = false;
				realtimerefresh=true;
		  		new  Thread(mR).start();
				swtFmDo(curPosition, isDeteled, true, null);
			} else {
				isDeteled = true;
				realtimerefresh=false;
				swtFmDo(curPosition, isDeteled, false, null);
			}
			break;
		}
	}

	private String getUrl() {
		String bussniessId = userShopInfoBean.getBusinessId();
//		String bussniessId = "BD2016042920160300000028";
		String status = "1";
		String url = API.HOST_WECHAT_MALL;
		DataObjecte data = new DataObjecte();

		data.setOrderStatus(status);
		data.setStoreId(bussniessId);
		data.setFlag("wl");
		data.setSourceType("tss");
		String dataJson = com.alibaba.fastjson.JSONObject.toJSON(data)
				.toString();
		// String url1 = dataJson; url = encryptionUrl(url, dataJson);

		url = encryptionUrl(url, dataJson);

		// String desStr = CryptTool.getBASE64(dataJson);

		return url;
	}

	private String encryptionUrl(String url, String dataJson) {
		try {
			// 获得的明文数据
			String desStr = dataJson;
			// 转成字节数组
			byte src_byte[] = desStr.getBytes();

			// MD5摘要
			byte[] md5Str = CryptTool.md5Digest(src_byte);
			// 生成最后的SIGN
			String SING = CryptTool.byteArrayToHexString(md5Str);

			desStr = CryptTool.getBASE64(dataJson);

			return url + "sing=" + SING + "&data=" + desStr;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_SEARCH_RESULT:// 搜索结果返回
				isSearchback=true;
				realtimerefresh=false;
				if (data != null) {
					tablname = data.getStringExtra("searchresult");
					mSearchRsult.clear();
					for (int i = 0; i < mInBusinessData.size(); i++) {
						if (mInBusinessData.get(i).getTableName()
								.contains(tablname)) {
							mSearchRsult.add(mInBusinessData.get(i));
							swtFmDo(curPosition, false, false, mSearchRsult);
						}
					}
				}
				
				break;
			case REQUEST_CODE_ADDTABLE_RESULT:// 添加桌子
//				dialog = new HttpDialog(this);
//				dialog.show();
				getTableinfo();
				break;
			default:
				break;
			}
		}
	}

	class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case POSITION0:
				if (mInBusinessFragment == null) {
					mInBusinessFragment = new InBusinessFragment();
				}
				return mInBusinessFragment;

			case POSITION1:
				if (mInRestFragment == null) {
					mInRestFragment = new InBusinessFragment();
				}
				return mInRestFragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (arg0 == 0 && isOne && mInBusinessData.size() != 0) {
			swtFmDo(arg0, isDeteled, false, mInBusinessData);
			isOne = false;
		}
	}

	boolean isDeteled = false;

	@Override
	public void onPageSelected(int arg0) {
		curPosition = arg0;
		switch (arg0) {
		case POSITION0:
			if (mInBusinessData != null) {
				swtFmDo(arg0, isDeteled, false, mInBusinessData);
			}
			slide.setState(false);
			break;

		case POSITION1:
			if (mInRestDatas != null) {
				swtFmDo(arg0, isDeteled, false, mInRestDatas);
			}
			slide.setState(true);
			break;
		}
	}

	public void deleteTable(ArrayList<String> ids,
			ArrayList<TableSetBean> mDatas) {
		this.mDatas = mDatas;
		StringBuffer sb = new StringBuffer();
		int s = ids.size();
		if (s > 0) {
			for (int i = 0; i < s; i++) {
				if (i == (s - 1)) {
					sb.append(ids.get(i));
				} else {
					sb.append(ids.get(i) + ",");
				}
			}
			System.out.println(sb.toString());
		} else {
			return;
		}
		// 向服务器发送请求
		getDeleteTableId(sb.toString());

	}

	public boolean Isdeletedsucces() {
		return Isdeletedsucces;
	}

	private void getDeleteTableId(String tableIds) {
		okHttpsImp.getDeleteTableId(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				Log.i("tag", "成功");
				String reString = result.getData();
				System.out.println("reString-----" + reString);
				if (reString != null) {
					try {
						ContentUtils.showMsg(TableSetActivity.this, "删除成功");
						Isdeletedsucces = true;
						int m = mDatas.size();
						for (int y = 0; y < m; y++) {
							if (mDatas.get(y).isS() == true) {
								mDatas.remove(y--);
								swtFmDo(POSITION0, false, false, mDatas);
								swtFmDo(POSITION1, false, false, mDatas);
//								switch (curPosition) {
//								case POSITION0:
//									mInRestDatas = mDatas;
//									break;
//								case POSITION1:
//									mInBusinessData = mDatas;
//									break;
//
//								}
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onResponseFailed(String msg) {
				Log.i("tag", "失败" + msg);

			}
		}, userShopInfoBean.getBusinessId(), tableIds);

	}

	public void setBusinessStatus(String tradeFlag) {
		okHttpsImp.getmodifyBusinessStatus(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String reString = result.getData();
				System.out.println("reString369-----" + reString);
				if (reString != null) {
					JSONObject jsonObject;
					try {
						// ContentUtils.showMsg(TableSetActivity.this, "修改成功");
						isSearchback=false;
						getTableinfo();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onResponseFailed(String msg) {
				Log.i("tag", "失败" + msg);

			}
		},userShopInfoBean.getBusinessId(), tradeFlag);

	}

	@Override
	public void open() {

	}

	@Override
	public void close() {
	}

	@Override
	public void setUpDate() {
		if (viewpager.getCurrentItem() == 0) {
			// adapter.notifyDataSetChanged();

			ISCONNECTED = AbAppUtil.isNetworkAvailable(this);
			if (!ISCONNECTED) {
				ContentUtils.showMsg(this, "当前网络连接不可用");
				return;
			}
			setBusinessStatus(ATREST);

		} else {
			ISCONNECTED = AbAppUtil.isNetworkAvailable(this);
			if (!ISCONNECTED) {
				ContentUtils.showMsg(this, "当前网络连接不可用");
				return;
			}
			setBusinessStatus(DOBUSINESS);
			// adapter.notifyDataSetChanged();
		}
	}
     @Override
    protected void onStop() {
    	super.onStop();
    	realtimerefresh=false;
     }
     @Override
    protected void onStart() {
    	super.onStart();
//    	if(isSearchback!=true){
    	  realtimerefresh=true;
  		  new  Thread(mR).start();
//    	}
     }

}
