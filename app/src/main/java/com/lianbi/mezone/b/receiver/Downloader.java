package com.lianbi.mezone.b.receiver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

public enum Downloader {

	DOWNLOADERL;

	static Context context;

	static DownloadManager downloadManager;

	static boolean canUseSystemDownloader = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;

	public static Downloader getInstance(Context ct) {
		context = ct;
		if (canUseSystemDownloader) {
			downloadManager = ((DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE));
		}
		return DOWNLOADERL;
	}

	/**
	 * 下载，如果是android2.3以下，将使用Agent下载，否则使用系统的下载管理器下载
	 * 
	 * @param url
	 * @return
	 */
	public long download(String url) {
		if (canUseSystemDownloader) {
			return downloadByDownloadManager(url);
		} else {
			downloadByAgent(url);
			return -1;
		}
	}

	/**
	 * 使用系统的下载管理器下载</br> 只支持android2.3或以上
	 * 
	 * @param url
	 * @param allowMobile
	 * @param mimeType
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public long downloadByDownloadManager(String url) {
		Uri uri = Uri.parse(url);
		Request request = new Request(uri);
		// 设置允许使用的网络类型
		int allowed = DownloadManager.Request.NETWORK_WIFI;
		allowed |= DownloadManager.Request.NETWORK_MOBILE;
		request.setAllowedNetworkTypes(allowed);
		request.setTitle("习之老板娘");
		request.setDescription("应用更新");
		request.setMimeType("application/vnd.android.package-archive");

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-ddhh-mm-ss");

		String date = dateformat.format(new Date());

		/**
		 * --------------解决有时候下载找不到文件夹问题
		 */
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录sd卡
		} else {
			sdDir = Environment.getDataDirectory();// 获取跟目录内存
		}

		File folder = new File("link_b");

		boolean isExit = (folder.exists() && folder.isDirectory()) ? true
				: folder.mkdirs();

		// request.setDestinationInExternalFilesDir(context, sdDir.getPath(),
		// date
		// + ".apk"); 设置下载后文件存放的位置--如果目标位置已
		request.setDestinationInExternalPublicDir(sdDir.getPath() + "/"
				+ folder.getPath(), date + ".apk");
		/**
		 * -------------------------------解决有时候下载找不到文件夹问题
		 */

		// 设置为可被媒体扫描器找到

		request.allowScanningByMediaScanner();

		long id = downloadManager.enqueue(request);

		return id;
	}

	/**
	 * 使用浏览器代理下载
	 * 
	 * @param url
	 */
	public void downloadByAgent(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public String getPath(long id) {
		if (id <= 0)
			return null;
		String path = null;

		Query query = new Query();
		query.setFilterById(id);
		Cursor cursor = downloadManager.query(query);

		while (cursor.moveToNext()) {
			String value = cursor.getString(cursor
					.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
			if (value != null) {
				path = value;
				break;
			}
		}
		cursor.close();

		// 如果是一个内容提供器地址
		if (!TextUtils.isEmpty(path) && path.startsWith("content:")) {
			cursor = context.getContentResolver().query(Uri.parse(path), null,
					null, null, null);
			// 移动到下一行
			while (cursor.moveToNext()) {
				String value = cursor.getString(cursor.getColumnIndex("_data"));
				if (value != null) {
					path = value;
					break;
				}
			}
			cursor.close();
		}
		return path;
	}

	public void cancelDownload(long id) {
		if (id <= 0)
			return;
		downloadManager.remove(id);
	}
}
