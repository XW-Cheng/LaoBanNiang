package com.lianbi.mezone.b.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import cn.com.hgh.utils.ContentUtils;

import com.xizhi.mezone.b.R;

@SuppressLint("NewApi")
public class PhotoUtills {

	/**
	 * 底部弹窗
	 */
	PopupWindow pw = null;

	/**
	 * 底部弹窗View
	 */
	View pickView;
	/** 全局的LayoutInflater对象，已经完成初始化. */
	public LayoutInflater mInflater;
	Context mContext;
	/**
	 * SD卡可用状态
	 */
	static boolean isSDCardAvaliable = true;

	/**
	 * 相册选择并裁剪
	 */
	public static final int REQUEST_IMAGE_FROM_ALBUM_AND_CROP = 0x1001;
	/**
	 * 相机拍照并裁剪
	 */
	public static final int REQUEST_IMAGE_FROM_CAMERA_AND_CROP = 0x1003;
	/**
	 * 裁剪
	 */
	public static final int REQUEST_IMAGE_CROP = 0x1005;

	/**
	 * 默认裁剪图片输出宽度像素
	 */
	protected int DEFAULT_IMG_X = 1200;
	/**
	 * 默认裁剪图片输出高度像素
	 */
	protected int DEFAULT_IMG_Y = 800;
	/**
	 * 当前正在处理的图片的File
	 */
	public static File photoCurrentFile;
	/**
	 * 默认裁剪图片输出高度比例
	 */
	protected int DEFAULT_IMG_ASPECT_Y = 2;
	/**
	 * 默认裁剪图片输出宽度比例
	 */
	protected int DEFAULT_IMG_ASPECT_X = 3;
	/**
	 * 默认裁剪图片输出格式（JPEG）
	 */
	public static final String DEFAULT_IMG_FORMAT = Bitmap.CompressFormat.JPEG
			.toString();
	/**
	 * 图片裁剪设置
	 */
	public PickImageDescribe defaultImageDescribe;
	/**
	 * 缓存文件的图片路径
	 */
	public static File cacheImageDir;
	/**
	 * 缓存文件路径
	 */
	static File cacheDir;
	/**
	 * 当前正在处理的图片的Uri
	 */
	static Uri photoCurrentUri;

	public PhotoUtills(Context ct) {
		this.mContext = ct;
		mInflater = LayoutInflater.from(ct);
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
			cacheImageDir = cacheDir;
			if (!cacheImageDir.exists()) {
				cacheImageDir.mkdirs();
			}
		}

		defaultImageDescribe = new PickImageDescribe();
		defaultImageDescribe.setFile(photoCurrentFile);
		defaultImageDescribe.setOutputX(DEFAULT_IMG_X);
		defaultImageDescribe.setOutputY(DEFAULT_IMG_Y);
		defaultImageDescribe.setAspectX(DEFAULT_IMG_ASPECT_X);
		defaultImageDescribe.setAspectY(DEFAULT_IMG_ASPECT_Y);
		defaultImageDescribe.setOutputFormat(DEFAULT_IMG_FORMAT);
		setNewImageFile();
	}

	/**
	 * 从相册选择照片或从相机拍照，调用此方法弹窗
	 */
	public void pickImage() {
		if (pw == null) {
			pw = PopupWindowHelper.createPopupWindow(pickView,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			pw.setAnimationStyle(R.style.slide_up_in_down_out);
		}
		pw.showAtLocation(((Activity) mContext).getWindow().getDecorView(),
				Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
	}

	/**
	 * 选择相册或相机的底部弹窗控件实例化
	 * 
	 */
	public void initPickView() {
		pickView = mInflater.inflate(R.layout.mineinfo_selecttouxiang, null);
		pickView.findViewById(R.id.photoFromAlbum_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						pw.dismiss();
						startPickPhotoFromCameraWithCrop();
					}
				});
		pickView.findViewById(R.id.photoFromCamera_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						pw.dismiss();
						startPickPhotoFromAlbumWithCrop();
					}
				});
		pickView.findViewById(R.id.photoCancle_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						pw.dismiss();
					}
				});
		pickView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
	}

	/**
	 * 固定宽高100
	 * 
	 * @return
	 */
	public static Bitmap getBitmap() {
		Bitmap bm = null;
		try {
			int w = 100;
			int h = 100;
			if (w > 0 && h > 0) {
				Options opt = new Options();
				opt.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(
						new FileInputStream(photoCurrentFile), null, opt);
				opt.inSampleSize = AbImageUtil.calculateInSampleSize(opt, w, h);
				opt.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeStream(new FileInputStream(
						photoCurrentFile), null, opt);
			} else {
				bm = BitmapFactory.decodeStream(new FileInputStream(
						photoCurrentFile));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bm;
	}

	/**
	 * 自定义宽高
	 * 
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap getBitmap(int w, int h) {
		Bitmap bm = null;
		try {
			if (w > 0 && h > 0) {
				Options opt = new Options();
				opt.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(
						new FileInputStream(photoCurrentFile), null, opt);
				opt.inSampleSize = AbImageUtil.calculateInSampleSize(opt, w, h);
				opt.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeStream(new FileInputStream(
						photoCurrentFile), null, opt);
			} else {
				bm = BitmapFactory.decodeStream(new FileInputStream(
						photoCurrentFile));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bm;
	}

	/**
	 * 获得新的图片文件路径
	 * 
	 * @author shaowei.ma
	 * @date 2014年9月24日
	 */
	protected void setNewImageFile() {
		photoCurrentFile = new File(cacheImageDir, new Date().getTime() + "."
				+ DEFAULT_IMG_FORMAT);

		photoCurrentFile.setReadable(true);
		photoCurrentFile.setWritable(true);
		photoCurrentFile.setExecutable(true);

		photoCurrentUri = Uri.fromFile(photoCurrentFile);
		defaultImageDescribe.setFile(photoCurrentFile);
	}

	/**
	 * 照相并裁剪
	 */
	protected void startPickPhotoFromCameraWithCrop() {
		if (!isSDCardAvaliable) {
			ContentUtils.showMsg(mContext, "当前您的SD卡不可用，您将无法使用相册/拍照上传功能");
			return;
		}
		setNewImageFile();
		Intent captureIntent = ImageHelper.getCamaraPickIntent(photoCurrentUri);
		((Activity) mContext).startActivityForResult(captureIntent,
				REQUEST_IMAGE_FROM_CAMERA_AND_CROP);
	}

	/**
	 * 相册选择并裁剪
	 */
	protected void startPickPhotoFromAlbumWithCrop() {
		if (!isSDCardAvaliable) {
			ContentUtils.showMsg(mContext, "当前您的SD卡不可用，您将无法使用相册/拍照上传功能");
			return;
		}
		setNewImageFile();
		Intent captureIntent = ImageHelper.getAlbumPickIntent();
		((Activity) mContext).startActivityForResult(captureIntent,
				REQUEST_IMAGE_FROM_ALBUM_AND_CROP);
	}

	/**
	 * 启动图片裁剪
	 */
	public void startCropImage() {
		PickImageDescribe pid = getPickImageDescribe();
		Intent cropIntent = ImageHelper.getCropIntent(null, photoCurrentUri,
				pid.getOutputX(), pid.getOutputY(), pid.getAspectX(),
				pid.getAspectY(), pid.getOutputFormat());
		((Activity) mContext).startActivityForResult(cropIntent,
				REQUEST_IMAGE_CROP);
	}

	public static String getPath(Context context, Uri uri) {

		boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider

			if (isExternalStorageDocument(uri)) {
				String docId = DocumentsContract.getDocumentId(uri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equals(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			} else if (isDownloadDocument(uri)) { // DownloadsProvider

				String id = DocumentsContract.getDocumentId(uri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(uri)) { // MediaProvider

				String docId = DocumentsContract.getDocumentId(uri);
				String[] split = docId.split(":");
				String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				String selection = "_id=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {

			if (isGooglePhotosUri(uri)) {
				return uri.getLastPathSegment();
			}
			return getDataColumn(context, uri, null, null);
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The contex
	 * @param uri
	 *            The Uri to query
	 * @param selection
	 *            selection (Optional) Filter used in the query
	 * @param selectionArgs
	 *            selectionArgs (Optional) Selection arguments used in the query
	 * @return The value of the _data columnm, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		String column = "_data";
		String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check
	 * @return Whether the Uri authority is ExternalStorageProvider
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check
	 * @return Whether the Uri authority is DownloadsProvider
	 */
	public static boolean isDownloadDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check
	 * @return Whether the Uri authority is MediaProvider
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	protected PickImageDescribe getPickImageDescribe() {
		return defaultImageDescribe;
	}

	/**
	 * 清除图片缓存
	 * 
	 * @param dir
	 */
	public void cleanFile(File dir) {
		if (dir == null)
			return;
		if (!dir.exists())
			return;
		File[] files = dir.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				cleanFile(f);
			} else if (f.isFile()) {
				f.delete();
			}
		}
	}
}
