package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.ServiceMallBean;
import com.lianbi.mezone.b.bean.WebProductManagementBean;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.xizhi.mezone.b.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.CryptTool;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.WebEncryptionUtil;
import cn.com.hgh.view.HttpDialog;

/**
 * 服务商城
 */
public class ServiceMallActivity extends BaseActivity {

	private ArrayList<ServiceMallBean> mDatas = new ArrayList<ServiceMallBean>();
	private ArrayList<ServiceMallBean> mData = new ArrayList<ServiceMallBean>();
	// private GridViewAdapter mAdapter;
	HttpDialog dialog;
	private static final int REQUEST_CODE_RESULT = 1000;
	private QuickAdapter<ServiceMallBean> mAdapter;
//	private GridView gridview;
	private ListView  listview_service;
	ImageView iv_store_service,iv_servicemall_empty;
	ServiceMallBean  mServiceMallBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(RESULT_OK);
		setContentView(R.layout.act_servicemallactivity, NOTYPE);
		initView();
		initListAdapter();
		dialog = new HttpDialog(this);
		dialog.show();
		getCandownloadMall();// 获取可供下载服务商城列表
	}

	private void initListAdapter() {
		mAdapter = new QuickAdapter<ServiceMallBean>(ServiceMallActivity.this,
				R.layout.item_servicemall_list, mDatas) {

			@Override
			protected void convert(BaseAdapterHelper helper,
					final ServiceMallBean item) {
				LinearLayout  llt_servicemall = helper
						.getView(R.id.llt_servicemall);
				ImageView img_itemmall = helper
						.getView(R.id.img_itemmall);
				TextView tv_servicename = helper
						.getView(R.id.tv_servicename);
				TextView tv_download = helper
						.getView(R.id.tv_download);
				TextView tv_newprice = helper
						.getView(R.id.tv_newprice);
				TextView tv_oldprice = helper
						.getView(R.id.tv_oldprice);
				ImageView img_right = helper
						.getView(R.id.img_right);
				tv_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
				tv_oldprice.setText("¥"+String.valueOf(item.getOriginalPrice())+String.valueOf(item.getUnit()));
				tv_newprice.setText("¥"+String.valueOf(item.getPresentPrice())+String.valueOf(item.getUnit()));
				tv_servicename.setText(item.getAppName());
				if(item.getDownload().equals("N")){
				  Glide.with(ServiceMallActivity.this).load(item.getIcoUrl()).error(R.mipmap.about_us).into(img_itemmall);
				  tv_download.setVisibility(View.VISIBLE);
				  img_right.setVisibility(View.INVISIBLE);
				}else 
			    if(item.getDownload().equals("Y")){
				  Glide.with(ServiceMallActivity.this).load(item.getIcoUrl()).error(R.mipmap.about_us).into(img_itemmall);
				  tv_download.setVisibility(View.GONE);
				  img_right.setVisibility(View.VISIBLE);
				}
				helper.getView(R.id.tv_download).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								String  isfdownload=item.getDownload();
								final String  serviceId=String.valueOf(item.getId());
								int primaryID = item.getId();
								mServiceMallBean=item;
								if(isfdownload.equals("Y")){
//									iv_store_service
//									.setBackgroundResource(R.drawable.icon_storservice);
									switch (primaryID) {
									case 1:
										
										        Intent intent = new Intent(
												ServiceMallActivity.this,
												TableSetActivity.class);
										        startActivity(intent);
//									        	finish();
										
										
										break;
				                    case 2:
				                    	Intent intent_web = new Intent(ServiceMallActivity.this,
												H5WebActivty.class);
										intent_web.putExtra(Constants.NEDDLOGIN, false);
										intent_web.putExtra("NEEDNOTTITLE", false);
										intent_web.putExtra("Re", true);
										intent_web.putExtra(WebActivty.T, "产品管理");
										intent_web.putExtra(WebActivty.U, getUrl(API.TOSTORE_PRODUCT_MANAGEMENT));
										startActivity(intent_web);
										break;

									case 3:
											Intent intent_web2 = new Intent(ServiceMallActivity.this,
													H5WebActivty.class);
										intent_web2.putExtra(Constants.NEDDLOGIN, false);
										intent_web2.putExtra("NEEDNOTTITLE", false);
										intent_web2.putExtra("Re", true);
										intent_web2.putExtra(WebActivty.T, "货源批发");
										intent_web2.putExtra(WebActivty.U, getUrl(API.TOSTORE_Supply_Wholesale+
												"storeId="+
												userShopInfoBean.getBusinessId()));
										startActivity(intent_web2);
										break;
									}
								}else if(isfdownload.equals("N")){
									dialog.setMessage("下载中...");
									dialog.show();
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {
											
										 goDownloadMall(serviceId,mServiceMallBean);
										
										}
									}, 2000);
									
								}
							} 
						});
				
				helper.getView(R.id.llt_servicemall).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
				                final String  serviceId=String.valueOf(item.getId());
								int primaryID = item.getId();
				                String  introduceurl=item.getIntroduceUrl();
								String  isfdownload=item.getDownload();
								if(isfdownload.equals("Y")){
//									iv_store_service
//									.setBackgroundResource(R.drawable.icon_storservice);
									switch (primaryID) {
									case 1:
										
										        Intent intent = new Intent(
												ServiceMallActivity.this,
												TableSetActivity.class);
										        startActivity(intent);
//									        	finish();
										
										
										break;
				                    case 2:
				                    	Intent intent_web = new Intent(ServiceMallActivity.this,
												H5WebActivty.class);
										intent_web.putExtra(Constants.NEDDLOGIN, false);
										intent_web.putExtra("NEEDNOTTITLE", false);
										intent_web.putExtra("Re", true);
										intent_web.putExtra(WebActivty.T, "产品管理");
										intent_web.putExtra(WebActivty.U, getUrl(API.TOSTORE_PRODUCT_MANAGEMENT));
										startActivity(intent_web);
										break;
									case 3:
										Intent intent_web2 = new Intent(ServiceMallActivity.this,
													H5WebActivty.class);
										intent_web2.putExtra(Constants.NEDDLOGIN, false);
										intent_web2.putExtra("NEEDNOTTITLE", false);
										intent_web2.putExtra("Re", true);
										intent_web2.putExtra(WebActivty.T, "货源批发");
										intent_web2.putExtra(WebActivty.U, getUrl(API.TOSTORE_Supply_Wholesale+
												"storeId="+
														userShopInfoBean.getBusinessId())
										);
										startActivity(intent_web2);
										break;
									}
							        
								}else if(isfdownload.equals("N")){
									  
									  Intent intent = new Intent(
									  ServiceMallActivity.this,
									  MallIntroduceActivity.class);
									  intent.putExtra("serviceid",serviceId);
									  intent.putExtra("introduceurl",introduceurl);
									  startActivityForResult(intent,REQUEST_CODE_RESULT);
								}
							} 
						});
			}
		};
		// 设置适配器
		listview_service.setAdapter(mAdapter);
	}

	private void initView() {
		setPageTitle("服务商城");
//		gridview = (GridView) findViewById(R.id.gview);
		listview_service= (ListView) findViewById(R.id.activity_servicemall_list);
		iv_servicemall_empty = (ImageView) findViewById(R.id.iv_servicemall_empty);
		// mAdapter = new GridViewAdapter(this);
		// gridview.setAdapter(mAdapter);
//		gridview.setOnItemClickListener(new OnItemClickListener() {
//
//			private ImageView iv_store_service;
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					final int position, long id) {
//				iv_store_service = (ImageView) view
//						.findViewById(R.id.iv_store_service);
//				String  isfdownload=mDatas.get(position).getDownload();
//				final String  serviceId=String.valueOf(mDatas.get(position).getId());
//				int primaryID = mDatas.get(position).getId();
//				mServiceMallBean=mDatas.get(position);
//				if(isfdownload.equals("Y")){
////					iv_store_service
////					.setBackgroundResource(R.drawable.icon_storservice);
//					switch (primaryID) {
//					case 1:
//						
//						        Intent intent = new Intent(
//								ServiceMallActivity.this,
//								TableSetActivity.class);
//						        startActivity(intent);
////					        	finish();
//						
//						
//						break;
//                    case 2:
//                    	Intent intent_web = new Intent(ServiceMallActivity.this,
//								H5WebActivty.class);
//						intent_web.putExtra(Constants.NEDDLOGIN, false);
//						intent_web.putExtra("NEEDNOTTITLE", false);
//						intent_web.putExtra("Re", true);
//						intent_web.putExtra(WebActivty.T, "产品管理");
//						intent_web.putExtra(WebActivty.U, getUrl());
//						startActivity(intent_web);
//						break;
//					}
//			        
//				}else if(isfdownload.equals("N")){
//					dialog.setMessage("下载中...");
//					dialog.show();
//					new Handler().postDelayed(new Runnable() {
//
//						@Override
//						public void run() {
//							
//						 goDownloadMall(serviceId,mServiceMallBean);
//						
//						}
//					}, 2000);
//					
//				}
//			}
//		});
	}
	public String getUrl(String address) {
		String bussniessId = BaseActivity.userShopInfoBean.getBusinessId();
		String url = address;
		WebProductManagementBean data = new WebProductManagementBean();
		data.setBusinessId(bussniessId);
		String dataJson = com.alibaba.fastjson.JSONObject.toJSON(data)
				.toString();
		url = encryptionUrl(url, dataJson);
		return url;
	}
	/**
	 * 加密
	 */
	private String encryptionUrl(String url, String dataJson) {
		try {
			// 获得的明文数据
			String desStr = dataJson;
			// 转成字节数组
			byte src_byte[] = desStr.getBytes();

			// MD5摘要
			byte[] md5Str = WebEncryptionUtil.md5Digest(src_byte);
			// 生成最后的SIGN
			String SING = WebEncryptionUtil.byteArrayToHexString(md5Str);

			desStr = CryptTool.getBASE64(dataJson);
			// http://localhost:8080/order/orderContler/?sing=key&data=密文
			return url + "sing=" + SING + "&&data=" + desStr + "&&auth=wcm";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	private void  goDownloadMall(String  serviceId,final ServiceMallBean  mServiceMallBean){
		okHttpsImp.getdownloadServer(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				Log.i("tag","成功");
				String reString = result.getData();
				if (reString != null) {
					JSONObject jsonObject;
					try {
					ContentUtils.showMsg(ServiceMallActivity.this, "下载成功");
					mServiceMallBean.setDownload("Y");
					updateView(mData);
					finish();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				dialog.dismiss();
			}

			@Override
			public void onResponseFailed(String msg) {
				Log.i("tag","失败"+msg);
				dialog.dismiss();
			}
		}, userShopInfoBean.getBusinessId(),serviceId);
		
		
	}
	private void  getCandownloadMall(){
		okHttpsImp.getCandownloadServerMall(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				Log.i("tag","成功");
				String reString = result.getData();
				if (reString != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(reString);
						reString = jsonObject.getString("appsList");
						Log.i("tag","reString 339---->"+reString );
						if (!TextUtils.isEmpty(reString)) {
							mData.clear();
							ArrayList<ServiceMallBean> downloadListMall = (ArrayList<ServiceMallBean>) JSON
									.parseArray(reString,
											ServiceMallBean.class);
							mData.addAll(downloadListMall);
							updateView(mData);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				dialog.dismiss();
			}

			@Override
			public void onResponseFailed(String msg) {
				Log.i("tag","失败"+msg);
				dialog.dismiss();
			}
		}, userShopInfoBean.getBusinessId());
		
	} 

	protected void updateView(ArrayList<ServiceMallBean> arrayList) {
		mDatas.clear();
		mDatas.addAll(arrayList);
		mAdapter.replaceAll(mDatas);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_RESULT:

				getCandownloadMall();// 获取可供下载服务商城列表

				break;
			}
		}
	}

}
