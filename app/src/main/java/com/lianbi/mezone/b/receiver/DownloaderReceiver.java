package com.lianbi.mezone.b.receiver;

import java.io.File;

import com.lianbi.mezone.b.app.Constants;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import cn.com.hgh.utils.AbAppUtil;
import cn.com.hgh.utils.ContentUtils;

public class DownloaderReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Downloader downloader = Downloader.getInstance(context);
		String action = intent.getAction();
		if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
			long id = ContentUtils.getSharePreLong(context,
					Constants.SHARED_PREFERENCE_NAME, Constants.APPDOWNLOAD_ID);
			long did = intent
					.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			if (id > 0 && id == did) {
				String apkPath = downloader.getPath(id);
				if (!TextUtils.isEmpty(apkPath) && apkPath.endsWith(".apk")) {
					AbAppUtil.installApk(context, new File(apkPath));
				}
			}
		}
	}

}
