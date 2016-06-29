package cn.com.hgh.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lianbi.mezone.b.app.Constants;

/**
 * 操作文件sharedpreference的工具类
 */
public final class ContentUtils {

	/**
	 * uri2path
	 * 
	 * @param context
	 * @param contentUri
	 */
	public static String uri2path(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Bitmap2File
	 * 
	 * @param bitName
	 * @param bitmap
	 * @return
	 * @throws java.io.IOException
	 */
	public static File bitmap2file(String bitName, Bitmap bitmap)
			throws IOException {
		File f = new File("mnt/sdcard/" + bitName + ".jpg");
		f.createNewFile();
		FileOutputStream fOut = null;
		fOut = new FileOutputStream(f);
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		fOut.flush();
		fOut.close();
		return f;
	}

	/**
	 * 取出whichSp中field字段对应的int类型的值如果该字段没对应值，则取出-1
	 * 
	 * @param mContext
	 * @param whichSp
	 * @param field
	 * @return
	 */
	public static int getSharePreInt(Context mContext, String whichSp,
			String field) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(whichSp, 0);
		int i = sp.getInt(field, -1);// 如果该字段没对应值，则取出-1
		return i;
	}

	/**
	 * 取出whichSp中field字段对应的boolean类型的值如果该字段没对应值，则取出false
	 * 
	 * @param mContext
	 * @param whichSp
	 * @param field
	 * @return
	 */
	public static boolean getSharePreBoolean(Context mContext, String whichSp,
			String field) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(whichSp, 0);
		boolean i = sp.getBoolean(field, false);
		return i;
	}

	/**
	 * 取出whichSp中long字段对应的long类型的值如果该字段没对应值，则取出0L
	 * 
	 * @param mContext
	 * @param whichSp
	 * @param field
	 * @return
	 */
	public static long getSharePreLong(Context mContext, String whichSp,
			String field) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(whichSp, 0);
		return sp.getLong(field, 0L);
	}

	/**
	 * 取出whichSp中field字段对应的String类型的值如果该字段没对应值，则取出空字符串
	 * 
	 * @param mContext
	 * @param whichSp
	 * @param field
	 * @return
	 */
	public static String getSharePreString(Context mContext, String whichSp,
			String field) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(whichSp, 0);
		String s = sp.getString(field, "");// 如果该字段没对应值，则取出空字符串
		return s;
	}

	/**
	 * 保存long类型的value到whichSp中的field字段
	 * 
	 * @param mContext
	 * @param whichSp
	 * @param field
	 * @param value
	 */
	public static void putSharePre(Context mContext, String whichSp,
			String field, long value) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(whichSp, 0);
		sp.edit().putLong(field, value).commit();
	}

	/**
	 * 保存string类型的value到whichSp中的field字段
	 * 
	 * @param mContext
	 * @param whichSp
	 * @param field
	 * @param value
	 */
	public static void putSharePre(Context mContext, String whichSp,
			String field, String value) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(whichSp, 0);
		sp.edit().putString(field, value).commit();
	}

	/**
	 * 保存int类型的value到whichSp中的field字段
	 * 
	 * @param mContext
	 * @param whichSp
	 * @param field
	 * @param value
	 */
	public static void putSharePre(Context mContext, String whichSp,
			String field, int value) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(whichSp, 0);
		sp.edit().putInt(field, value).commit();
	}

	/**
	 * 保存boolen类型的value到whichSp中的field字段(主要做登陆状态)
	 * 
	 * @param mContext
	 * @param whichSp
	 * @param field
	 * @param value
	 */
	public static void putSharePre(Context mContext, String whichSp,
			String field, boolean value) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(whichSp, 0);
		sp.edit().putBoolean(field, value).commit();
	}

	/**
	 * 删除某个文件
	 * 
	 * @param mContext
	 * @param whichSp
	 */
	public static void deleteField(Context mContext, String whichSp) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(whichSp, 0);
		sp.edit().clear();
	}

	private static Toast toast = null;

	/**
	 * Toast的封装
	 * 
	 * @param mContext
	 *            上下文，来区别哪一个activity调用的
	 * @param msg
	 *            你希望显示的值。
	 */
	public static void showMsg(final Context act, final String msg) {
		if (toast != null) {
			toast.setText(msg);
		} else {
			toast = Toast.makeText(act, msg, Toast.LENGTH_SHORT);
		}
		toast.show();
	}

	/**
	 * 获取当前的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentVersionCode(Context context) {
		String versionCode = "";
		PackageManager manager = context.getPackageManager();
		try {
			versionCode = manager.getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionCode;
	}

	/**
	 * 获得登陆状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getLoginStatus(Context context) {
		return ContentUtils.getSharePreBoolean(context,
				Constants.SHARED_PREFERENCE_NAME, Constants.LOGINED_IN);
	}

	/**
	 * 根据指定内容生成自定义宽高的二维码图片
	 * 
	 * param logoBm logo图标 param content 需要生成二维码的内容 param width 二维码宽度 param
	 * height 二维码高度 throws WriterException 生成二维码异常
	 */
	public static Bitmap makeQRImage(Bitmap logoBmp, String content,
			int QR_WIDTH, int QR_HEIGHT) throws WriterException {
		try {
			// 图像数据转换，使用了矩阵转换
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 容错率
			BitMatrix bitMatrix = new QRCodeWriter().encode(content,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			for (int y = 0; y < QR_HEIGHT; y++) {
				// 下面这里按照二维码的算法，逐个生成二维码的图片，//两个for循环是图片横列扫描的结果
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y))
						pixels[y * QR_WIDTH + x] = 0xff000000;
					else
						pixels[y * QR_WIDTH + x] = 0xffffffff;
				}
			}
			// ------------------添加图片部分------------------//
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			// 设置像素点
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			// 获取图片宽高
			int logoWidth = logoBmp.getWidth();
			int logoHeight = logoBmp.getHeight();
			if (QR_WIDTH == 0 || QR_HEIGHT == 0) {
				return null;
			}
			if (logoWidth == 0 || logoHeight == 0) {
				return bitmap;
			}
			// 图片绘制在二维码中央，合成二维码图片
			// logo大小为二维码整体大小的1/2
			float scaleFactor = QR_WIDTH * 1.0f / 2 / logoWidth;
			try {
				Canvas canvas = new Canvas(bitmap);
				canvas.drawBitmap(bitmap, 0, 0, null);
				canvas.scale(scaleFactor, scaleFactor, QR_WIDTH / 2,
						QR_HEIGHT / 2);
				canvas.drawBitmap(logoBmp, (QR_WIDTH - logoWidth) / 2,
						(QR_HEIGHT - logoHeight) / 2, null);
				canvas.save(Canvas.ALL_SAVE_FLAG);
				canvas.restore();
				return bitmap;
			} catch (Exception e) {
				bitmap = null;
				e.getStackTrace();
			}
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 在图片右下角添加水印
	 * 
	 * @param srcBMP
	 *            原图
	 * @param markBMP
	 *            水印图片
	 * @return 合成水印后的图片
	 */
	public static Bitmap composeWatermark(Bitmap srcBMP, Bitmap markBMP) {
		if (srcBMP == null) {
			return null;
		}
		// 创建一个新的和SRC长度宽度一样的位图
		Bitmap newb = Bitmap.createBitmap(srcBMP.getWidth(),
				srcBMP.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas cv = new Canvas(newb);
		// 在 0，0坐标开始画入原图
		cv.drawBitmap(srcBMP, 0, 0, null);
		// 在原图的右下角画入水印
		cv.drawBitmap(markBMP, srcBMP.getWidth() - markBMP.getWidth() * 4 / 5,
				srcBMP.getHeight() * 2 / 7, null);
		// 保存
		cv.save(Canvas.ALL_SAVE_FLAG);
		// 存储
		cv.restore();
		return newb;
	}

	/**
	 * 给二维码图片加背景
	 * 
	 */
	public static Bitmap addBackground(Bitmap foreground, Bitmap background) {
		int bgWidth = background.getWidth();
		int bgHeight = background.getHeight();
		int fgWidth = foreground.getWidth();
		int fgHeight = foreground.getHeight();
		Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newmap);
		canvas.drawBitmap(background, 0, 0, null);
		canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2,
				(bgHeight - fgHeight) * 3 / 5 + 70, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newmap;
	}

	/**
	 * 生成二维码图片的宽度
	 */
	public static final int QR_WIDTH = 300;
	/**
	 * 生成二维码图片的高度
	 */
	public static final int QR_HEIGHT = 300;

	/**
	 * 生成QR图
	 * 
	 * @param path
	 * @param whith
	 *            是否黑白色
	 * @return
	 */
	public static Bitmap createQrBitmap(String path, boolean whith) {
		try {
			if (TextUtils.isEmpty(path)) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(path,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						if (whith) {
							pixels[y * QR_WIDTH + x] = 0xff000000;

						} else {
							if (x < QR_WIDTH / 2 && y < QR_HEIGHT / 2) {
								pixels[y * QR_WIDTH + x] = 0xFF0094FF;// 蓝色
								Integer.toHexString(new Random().nextInt());
							} else if (x < QR_WIDTH / 2 && y > QR_HEIGHT / 2) {
								pixels[y * QR_WIDTH + x] = 0xFFff3c25;// 黄色
							} else if (x > QR_WIDTH / 2 && y > QR_HEIGHT / 2) {
								pixels[y * QR_WIDTH + x] = 0xFF5ACF00;// 绿色
							} else {
								pixels[y * QR_WIDTH + x] = 0xFF000000;// 黑色
							}
						}

					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;// 白色
					}

				}
			}

			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);

			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param path
	 * @param whith
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap createQrBitmap(String path, boolean whith, int w, int h) {
		try {
			if (TextUtils.isEmpty(path)) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(path,
					BarcodeFormat.QR_CODE, w, h, hints);
			int[] pixels = new int[w * h];
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					if (bitMatrix.get(x, y)) {
						if (whith) {
							pixels[y * w + x] = 0xff000000;

						} else {
							if (x < w / 2 && y < h / 2) {
								pixels[y * w + x] = 0xFF0094FF;// 蓝色
								Integer.toHexString(new Random().nextInt());
							} else if (x < w / 2 && y > h / 2) {
								pixels[y * w + x] = 0xFFff3c25;// 黄色
							} else if (x > w / 2 && y > h / 2) {
								pixels[y * w + x] = 0xFF5ACF00;// 绿色
							} else {
								pixels[y * w + x] = 0xFF000000;// 黑色
							}
						}

					} else {
						pixels[y * w + x] = 0xffffffff;// 白色
					}

				}
			}

			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

			bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}
}