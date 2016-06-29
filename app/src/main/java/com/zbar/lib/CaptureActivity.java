package com.zbar.lib;

import java.io.IOException;
import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.DialogCommon;
import cn.com.hgh.view.HttpDialog;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.ui.BaseActivity;
import com.lianbi.mezone.b.ui.MainActivity;

/**
 * 描述: 扫描界面
 */
public class CaptureActivity extends BaseActivity {

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private CameraManager mCameraManager;

	private FrameLayout scanPreview;
	private ImageView scanLine;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;

	EditText edt_qr_scan_code;
	private TextView tv_confirm_code, tip;

	private Rect mCropRect = null;
	private boolean previewing = true;
	private ImageScanner mImageScanner = null;
	/**
	 * 暂时没用
	 */
	private String orderId;

	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_qr_scan, NOTYPE);
		amount = getIntent().getStringExtra("amount");
		orderId = getIntent().getStringExtra("orderId");
		findViewById();
		initViews();
	}

	@Override
	public void onBackPressed() {
		ContentUtils.showMsg(CaptureActivity.this, "不支持系统返回");
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		if (view.getId() == R.id.tv_confirm_code) {
			String code = edt_qr_scan_code.getText().toString().trim();
			if (!TextUtils.isEmpty(code)) {
				getBarCodeScane(code);
			} else {
				ContentUtils.showMsg(this, "输入不能为空");
			}
		}
	}

	private void findViewById() {
		setPageTitle("扫一扫");
		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
		tv_confirm_code = (TextView) findViewById(R.id.tv_confirm_code);
		tip = (TextView) findViewById(R.id.tv_capture);
		edt_qr_scan_code = (EditText) findViewById(R.id.edt_qr_scan_code);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);
		tv_confirm_code.setOnClickListener(this);
	}

	private void initViews() {
		mImageScanner = new ImageScanner();
		mImageScanner.setConfig(0, Config.X_DENSITY, 3);
		mImageScanner.setConfig(0, Config.Y_DENSITY, 3);

		autoFocusHandler = new Handler();
		mCameraManager = new CameraManager(this);
		try {
			mCameraManager.openDriver();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mCamera = mCameraManager.getCamera();
		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		scanPreview.addView(mPreview);

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1f);
		animation.setDuration(3000);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.REVERSE);
		scanLine.startAnimation(animation);
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	/**
	 * 离开相机
	 */
	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * 自动对焦线程
	 */
	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};
	/**
	 * 结果回调
	 */
	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Size size = camera.getParameters().getPreviewSize();

			// 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
			byte[] rotatedData = new byte[data.length];
			for (int y = 0; y < size.height; y++) {
				for (int x = 0; x < size.width; x++)
					rotatedData[x * size.height + size.height - y - 1] = data[x
							+ y * size.width];
			}

			// 宽高也要调整
			int tmp = size.width;
			size.width = size.height;
			size.height = tmp;

			initCrop();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(rotatedData);
			barcode.setCrop(mCropRect.left, mCropRect.top, mCropRect.width(),
					mCropRect.height());

			int result = mImageScanner.scanImage(barcode);
			String resultStr = null;

			if (result != 0) {
				SymbolSet syms = mImageScanner.getResults();
				for (Symbol sym : syms) {
					resultStr = sym.getData();
				}
			}

			if (!TextUtils.isEmpty(resultStr)) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();
				getBarCodeScane(resultStr);
			}
		}
	};

	/**
	 * 1秒对焦一次
	 */
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = mCameraManager.getCameraResolution().y;
		int cameraHeight = mCameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		mCropLayout.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = mCropLayout.getWidth();
		int cropHeight = mCropLayout.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = mContainer.getWidth();
		int containerHeight = mContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	/**
	 * bar高度
	 * 
	 * @return
	 */
	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 价格
	 */
	String amount;
	/**
	 * 返回的订单id
	 */
	protected String mOrderId;

	/**
	 * 出结果 odd:订单，bdp:店铺 pcp:产品 why:货源 uyh:用户
	 * 
	 * @param code
	 */
	void getBarCodeScane(String code) {
		// // barcodeScanned = true;
		// int leanth = code.length();
		// if (leanth == 24) {
		//
		// if (code.contains("odd") || code.contains("bdp")
		// || code.contains("pcp") || code.contains("why")
		// || code.contains("uyh")) {
		// okHttpsImp.getSweep(code, new MyResultCallback<String>() {
		//
		// @Override
		// public void onResponseResult(Result result) {
		// ContentUtils.showMsg(CaptureActivity.this, "订单已完成");
		// finish();
		// }
		//
		// @Override
		// public void onResponseFailed(String msg) {
		// finish();
		//
		// }
		// });
		// } else {
		//
		// finish();
		// }
		// } else {
		// finish();
		// }
		okHttpsImp.pushCard(userShopInfoBean.getUserId(), code, amount,
				orderId, new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						String res = result.getData();
						try {
							JSONObject jb = new JSONObject(res);
							mOrderId = jb.getString("isTrue");

							dialogCommon = new DialogCommon(
									CaptureActivity.this) {

								@Override
								public void onOkClick() {

									isSucess1(mOrderId, true);

								}

								@Override
								public void onCheckClick() {

								}
							};
							dialogCommon.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
							dialogCommon.setOnKeyListener(keylistener);
							dialogCommon.setTextTitle("请确认客户已支付！");
							dialogCommon.setTv_dialog_common_ok("查看支付结果");
							dialogCommon.setTv_dialog_common_cancelV(View.GONE);
							dialogCommon.show();

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onResponseFailed(String msg) {
						setResult(RESULT_OK);
						finish();
					}
				});
	}

	OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	};
	DialogCommon dialogCommon;

	@Override
	protected void onTitleLeftClick() {
		setResult(RESULT_OK);
		super.onTitleLeftClick();
	}

	int count;

	/**
	 * 只掉一次
	 * 
	 * @param orderId2
	 * @param isfirst
	 */
	protected void isSucess1(String orderId2, final boolean isfirst) {
		okHttpsImp.orderQuery(orderId2, 1 + "", new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String res = result.getData();
				try {
					JSONObject jb = new JSONObject(res);
					String isT = jb.getString("isTrue");

					boolean isSucess = isT.equals("1") ? true : false;
					if (isSucess) {
						dialogCommon.dismiss();
						ContentUtils.showMsg(CaptureActivity.this, "支付成功");
						setResult(RESULT_OK);
						finish();
					} else {
						dialogCommon.dismiss();
						ContentUtils.showMsg(CaptureActivity.this,
								"支付失败，请客户不要再支付");
						setResult(RESULT_OK);
						finish();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onResponseFailed(String msg) {
				ContentUtils.showMsg(CaptureActivity.this, "支付失败，请客户不要再支付");
				setResult(RESULT_OK);
				finish();
			}
		});

	}

	/**
	 * 暂时没用
	 * 
	 * @param orderId2
	 * @param isfirst
	 */
	protected void isSucess(String orderId2, final boolean isfirst) {
		if (!isfirst) {
			if (httpDialog == null) {
				httpDialog = new HttpDialog(this);
				httpDialog.setMessage("正在查询支付结果");
				httpDialog.show();
			}
		}
		int s = 0;
		if (count == 3) {
			s = 1;
		}
		okHttpsImp.orderQuery(orderId2, s + "", new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String res = result.getData();
				try {
					JSONObject jb = new JSONObject(res);
					String isT = jb.getString("isTrue");

					boolean isSucess = isT.equals("1") ? true : false;

					if (!isfirst) {
						if (isSucess) {
							httpDialog.dismiss();
							ContentUtils.showMsg(CaptureActivity.this, "支付成功");
							setResult(RESULT_OK);
							finish();
						} else {
							count++;
							if (count <= 3) {
								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										isSucess(mOrderId, false);
									}
								}, 1000 * 6);
							} else {
								httpDialog.dismiss();
								ContentUtils.showMsg(CaptureActivity.this,
										"支付失败，请客户不要再支付");
								setResult(RESULT_OK);
								finish();
							}
						}
					} else {
						if (isSucess) {
							ContentUtils.showMsg(CaptureActivity.this, "支付成功");
							setResult(RESULT_OK);
							finish();
						} else {
							count++;
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									isSucess(mOrderId, false);
								}
							}, 1000 * 6);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onResponseFailed(String msg) {
				ContentUtils.showMsg(CaptureActivity.this, "支付失败，请客户不要再支付");
				setResult(RESULT_OK);
				finish();
			}
		});

	}

	HttpDialog httpDialog;
}