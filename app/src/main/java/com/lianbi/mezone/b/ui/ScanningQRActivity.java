package com.lianbi.mezone.b.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.DialogCommon;
import cn.com.hgh.view.HttpDialog;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.Ades_ImageEs;
import com.lianbi.mezone.b.bean.OrderListBean;
import com.lianbi.mezone.b.bean.QrcodeDetails;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

public class ScanningQRActivity extends BaseActivity {

	private TextView tv_tablename, tv_keepqr;
	private ImageView iv_scanningqr;
	HttpDialog dialog;
	String tableid;
	private String url,http;
	private String imageUrl="";
	QrcodeDetails  mQrcodeDetails;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_scanningqr, NOTYPE);
		initView();
		listen();
		dialog = new HttpDialog(this);
		dialog.show();
		getTableDetail(tableid);
	}
	public void getHttpBitmap(final String url,final ImageView  img){
    	try{
    		new Thread(){  
    			  @Override  
    			   public void run()  
    			   {  
    		    		try {
    		    			URL	myFileURL = new URL(url);
							//获得连接
	    		    		HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
	    		    		conn.setConnectTimeout(6000);
	    		    		conn.setDoInput(true);
	    		    		//不使用缓
	    		    		conn.setUseCaches(false);
	    		    		InputStream is = conn.getInputStream();
	    		    		//解析得到图片
	    		    		Bitmap bitmap = BitmapFactory.decodeStream(is);
	    		    		//关闭数据流
//	    		    		img.setImageBitmap(bitmap);
	    					Message msg = new Message();
							msg.obj=bitmap;
							mHandler.sendMessage(msg);
	    		    		is.close();  
	    				  //把网络访问的代码放在这里    
    		    		} catch (Exception e) {
							e.printStackTrace();
						}
    		    		 
    			  }  
    			}.start();  

    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		
    	public void handleMessage(Message msg) {
    		iv_scanningqr.setImageBitmap((Bitmap) msg.obj);

    		
    	}
		
	};

	 
	private void listen() {
		tv_keepqr.setOnClickListener(this);
		tv_tablename.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				/* 开启Pictures画面Type设定为image */
				intent.setType("image/*");
				/* 使用Intent.ACTION_GET_CONTENT这个Action */
				intent.setAction(Intent.ACTION_GET_CONTENT);
				/* 取得相片后返回本画面 */
				startActivity(intent);
			}
		});
	}

	private void initView() {
		setPageTitle("扫二维码");
		String tablename = getIntent().getStringExtra("TABLENAME");
		String tableqr = getIntent().getStringExtra("TABLEQR");
		tableid=getIntent().getStringExtra("TABLEID");
//		Glide.with(ScanningQRActivity.this).load(tableqr).error(R.id.defaultimg_11).into(iv_scanningqr);
		tv_tablename = (TextView) findViewById(R.id.tv_tablename);
		if (!(null == tablename && TextUtils.isEmpty(tablename))) {
			tv_tablename.setText(tablename);
		} else {
			tv_tablename.setText("获取桌位名称失败");
		}
		tv_keepqr = (TextView) findViewById(R.id.tv_keepqr);
		iv_scanningqr = (ImageView) findViewById(R.id.iv_scanningqr);
//		iv_scanningqr.setBackgroundResource(R.drawable.addtableset);
	}

	@Override
	protected void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.tv_keepqr:
			try {
				Bitmap bitmap = ((BitmapDrawable) ((ImageView) iv_scanningqr)
						.getDrawable()).getBitmap();
				if (null != bitmap) {
					if (saveImageToGallery(this, bitmap)) {
						DialogCommon dialogCommon = new DialogCommon(
								ScanningQRActivity.this) {

							@Override
							public void onCheckClick() {
								dismiss();
							}

							@Override
							public void onOkClick() {
								Intent intent = new Intent();
								intent.setType("image/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								startActivity(intent);
								dismiss();
							}

						};
						dialogCommon.setTextTitle("保存图片成功");
						dialogCommon.setTv_dialog_common_ok("立即查看");
						dialogCommon.setTv_dialog_common_cancel("稍后再看");
						dialogCommon.show();
					}else{
						ContentUtils.showMsg(this, "保存图片失败");
					}
				}
				
			} catch (Exception e) {

			e.printStackTrace();
			
			}
			break;

		}
	}
	   public void getTableDetail(String tableId){
	   	okHttpsImp.getTableDetail(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				Log.i("tag","成功");
				String reString = result.getData();
				if (reString != null) {
					JSONObject jsonObject;
					try {
						jsonObject= new JSONObject(reString);
						reString = jsonObject.getString("detail");
						mQrcodeDetails=JSON.parseObject(
								reString, QrcodeDetails.class);
						url=mQrcodeDetails.getCodeUrl();
						http=jsonObject.getString("httpInfo").toString();
					    imageUrl=http+url;
//						Glide.with(ScanningQRActivity.this).load(imageUrl).error(R.drawable.about_us).into(iv_scanningqr);
						getHttpBitmap(imageUrl, iv_scanningqr);
					    
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				dialog.dismiss();
			}

			@Override
			public void onResponseFailed(String msg) {
				Log.i("tag","失败"+msg);
				ContentUtils.showMsg(ScanningQRActivity.this,msg);
				dialog.dismiss();
			}
		}, userShopInfoBean.getBusinessId(),tableId);
	   
	   }

	public static boolean saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(),
				"Boohee");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.fromFile(new File(file.getPath()))));
		return true;
	}
}
