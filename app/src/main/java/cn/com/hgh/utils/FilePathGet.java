package cn.com.hgh.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FilePathGet {
	private static boolean isSDCardAvaliable = false;
	static File cacheDir;

	/** 保存方法 */
	public static void saveBitmap(Bitmap bm, String name, int k,
			HashMap<String, File> mapFile) {
		bm = compressImage(bm);
		// 初始化图片保存路径
		File dirFile = new File(createSDCardDir("cacheImages") + name);
		try {
			FileOutputStream out = new FileOutputStream(dirFile);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			mapFile.put(k + "", dirFile);
			// files.add(dirFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/** 保存方法 */
	public static File saveBitmap(Bitmap bm) {
		bm = compressImage(bm);
		// 初始化图片保存路径
		File dirFile = new File(FilePathGet.createSDCardDir("cacheImages")
				+ File.separator + System.currentTimeMillis() + ".jpg");
		try {
			FileOutputStream out = new FileOutputStream(dirFile);
			bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
			out.flush();
			out.close();
			return dirFile;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getPath(Context ct) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd card 可用
			isSDCardAvaliable = true;
		} else if (isSDCardAvaliable != false) {
			// 当前不可用
			ContentUtils.showMsg(ct, "当前您的SD卡不可用，您将无法使用相册/拍照上传功能");
			isSDCardAvaliable = false;
		}

		if (cacheDir == null)
			cacheDir = ct.getExternalCacheDir();
		if (cacheDir == null) {
			ContentUtils.showMsg(ct, "当前您的SD卡不可用，您将无法使用相册/拍照上传功能");
			isSDCardAvaliable = false;
		} else {
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
		}
		return cacheDir.toString();
	}

	/**
	 * 创建文件夹
	 * 
	 * @return 文件夹绝对路径
	 */
	public static String createSDCardDir(String fileName) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + File.separator + fileName;
			File path1 = new File(path);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
			}
			return path1.getAbsolutePath();
		} else {
			File mFile = new File(Environment.getExternalStorageDirectory()
					.getAbsoluteFile() + File.separator + fileName);
			if (!mFile.exists()) {
				mFile.mkdir();
			}
			return mFile.getAbsolutePath();
		}
	}

}
