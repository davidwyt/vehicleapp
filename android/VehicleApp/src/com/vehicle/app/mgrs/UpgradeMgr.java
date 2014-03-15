package com.vehicle.app.mgrs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.vehicle.app.utils.FileUtil;
import com.vehicle.imserver.dao.bean.VersionInfo;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.LatestVersionResponse;

import cn.edu.sjtu.vehicleapp.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class UpgradeMgr {

	private FetchLatestVersionTask mFetchTask;
	private CheckLatestVersionInfo mCheckTask;

	public boolean isChecked = false;

	private static class InstanceHolder {
		private static UpgradeMgr instance = new UpgradeMgr();
	}

	public static UpgradeMgr getInstance() {
		return InstanceHolder.instance;
	}

	public boolean isDownload() {
		return null != mFetchTask;
	}

	private String curApkPath;

	private VersionInfo latestVersion = null;

	// compare cur and remote
	// -1: cur < remote, update
	// 0 : cur == remote, not update
	// 1: cur > remote, what the hell...
	private int compareVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			System.out.println("version:" + version);
			String[] strs = version.split("\\.");
			for (String str : strs) {
				System.out.println("ss:" + str);
			}
			if (null != strs && strs.length > 1) {
				int majorVersion = Integer.parseInt(strs[0]);
				int minorVersion = Integer.parseInt(strs[1]);
				System.out.println("version:" + version);
				System.out.println("maj:" + majorVersion + " min:" + minorVersion);
				if (null == this.latestVersion) {
					return 1;
				}

				if (majorVersion < this.latestVersion.getMajorVersion()) {
					return -1;
				} else if (majorVersion > this.latestVersion.getMajorVersion()) {
					System.err.println("what the hell of cur version....");
				} else {
					if (minorVersion == this.latestVersion.getMinorVersion()) {
						return 0;
					} else {
						return minorVersion < this.latestVersion.getMinorVersion() ? -1 : 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public boolean isChecking() {
		return null != this.mCheckTask;
	}

	public void attemptCheck(final Context context, boolean isTip) {
		if (null != this.mCheckTask)
			return;

		this.mCheckTask = new CheckLatestVersionInfo(context, isTip);
		this.mCheckTask.execute();
	}

	private void appemptUpgrade(final Context context) {
		if (null != this.mFetchTask)
			return;

		if (null == this.latestVersion)
			return;

		final int majorVersion = this.latestVersion.getMajorVersion();
		final int minorVersion = this.latestVersion.getMinorVersion();
		final String url = this.latestVersion.getApkPath();
		
		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

			alertDialogBuilder.setTitle(context.getResources().getString(R.string.zh_upgrade));

			alertDialogBuilder
					.setMessage(
							context.getString(R.string.tip_downloadversionformat, majorVersion + "." + minorVersion))
					.setCancelable(false)
					.setPositiveButton(context.getString(R.string.zh_download), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							MyMessageHandler handler = new MyMessageHandler(context);

							curApkPath = FileUtil.genPathForApk(context, majorVersion, minorVersion);
							mFetchTask = new FetchLatestVersionTask(url, curApkPath, handler);
							mFetchTask.execute();
						}
					})
					.setNegativeButton(context.getString(R.string.zh_nexttime), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void install(Context context) {
		try {
			File file = new File(this.curApkPath);
			if (file.isFile() && file.exists()) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
				context.startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final int MESSAGE_WHAT_PROGRESSUPDATE = 1;
	private static final int MESSAGE_WHAT_DOWNDONE = 2;

	@SuppressLint("HandlerLeak")
	private class MyMessageHandler extends Handler {
		private Context context;

		MyMessageHandler(Context context) {
			this.context = context;
		}

		public void handleMessage(final Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case MESSAGE_WHAT_PROGRESSUPDATE:
					NotificationMgr mgr = new NotificationMgr(context);
					mgr.notifyDowonloadNewVersion(context, msg.arg2, msg.arg1);
					break;
				case MESSAGE_WHAT_DOWNDONE:
					final NotificationMgr mgr2 = new NotificationMgr(context);
					mgr2.notifyDowonloadNewVersion(context, msg.arg2, 100);
					mgr2.cancel(msg.arg2);
					install(context);
					break;
				default:
					break;
				}
			}
		}
	}

	private class CheckLatestVersionInfo extends AsyncTask<Void, Void, LatestVersionResponse> {

		private Context context;
		private boolean isTip;

		CheckLatestVersionInfo(Context context, boolean isTip) {
			this.context = context;
			this.isTip = isTip;
		}

		@Override
		protected LatestVersionResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			LatestVersionResponse resp = null;
			VehicleClient client = new VehicleClient("");
			try {
				resp = client.GetLatestVersion();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(LatestVersionResponse result) {
			try {
				mCheckTask = null;
				if (this.isTip && (null == result || !result.isSucess() || null == result.getVersion())) {
					Toast.makeText(context, context.getString(R.string.tip_getversioninfofailed), Toast.LENGTH_LONG)
							.show();
					return;
				}

				latestVersion = result.getVersion();
				if (compareVersion(context) < 0) {
					appemptUpgrade(context);
				} else if (this.isTip) {
					Toast.makeText(context, context.getString(R.string.tip_curalreadylatest), Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onCancelled() {
			mCheckTask = null;
		}
	}

	private class FetchLatestVersionTask extends AsyncTask<Void, Void, Void> {

		private String url;
		private String path;
		private MyMessageHandler handler;

		FetchLatestVersionTask(String url, String path, MyMessageHandler handler) {
			this.url = url;
			this.path = path;
			this.handler = handler;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			try {
				int curNotifyNum = NotificationMgr.nextNotificationNum();

				URL apkUrl = new URL(url);
				final HttpURLConnection conn = (HttpURLConnection) apkUrl.openConnection();
				conn.setConnectTimeout(5000);

				Message message = new Message();
				int totalLen = conn.getContentLength();
				message.what = MESSAGE_WHAT_PROGRESSUPDATE;
				message.arg1 = 0;
				message.arg2 = curNotifyNum;
				handler.sendMessage(message);

				InputStream is = conn.getInputStream();
				File file = new File(this.path);

				FileOutputStream fos = new FileOutputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] buffer = new byte[1024];
				int len;
				int downloadedLen = 0;
				long lastTime = new Date().getTime();
				while ((len = bis.read(buffer, 0, buffer.length)) > 0) {
					fos.write(buffer, 0, len);
					downloadedLen += len;

					long cur = new Date().getTime();
					if (cur - lastTime > 1000) {
						lastTime = cur;
						Message message1 = new Message();
						message1.what = MESSAGE_WHAT_PROGRESSUPDATE;
						message1.arg1 = (int) (((float) downloadedLen / totalLen) * 100);
						message1.arg2 = curNotifyNum;
						handler.sendMessage(message1);
					}
				}
				Message message2 = new Message();
				message2.what = MESSAGE_WHAT_DOWNDONE;
				message2.arg2 = curNotifyNum;
				handler.sendMessage(message2);

				fos.close();
				bis.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mFetchTask = null;
		}

		@Override
		protected void onCancelled() {
			mFetchTask = null;
		}
	}
}
