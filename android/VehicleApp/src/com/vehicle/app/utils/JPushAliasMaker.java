package com.vehicle.app.utils;

import java.util.Set;
import java.util.concurrent.Semaphore;

import com.vehicle.app.mgrs.SelfMgr;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JPushAliasMaker {

	private Context context;
	private String alias;

	static AliasThread aliasThread = null;

	public JPushAliasMaker(Context context, String alias) {
		this.context = context;
		this.alias = alias;
	}

	public void makeAlias() {

		if (null != aliasThread) {
			try {
				aliasThread.stop = true;
				aliasThread = null;

				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			aliasThread = new AliasThread();
			aliasThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class AliasThread extends Thread {

		boolean stop = false;
		Semaphore lock = new Semaphore(0);

		class MyAliasCallCallback implements TagAliasCallback {

			@Override
			public void gotResult(int responseCode, String alias, Set<String> tags) {
				if (6002 == responseCode) {

				} else if (0 == responseCode) {
					stop = true;
				} else {
					stop = true;
				}

				try {
					lock.release();
				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("result code:" + responseCode + " alias:" + alias);
			}
		}

		@Override
		public void run() {
			TagAliasCallback callBack = new MyAliasCallCallback();

			while (!stop) {
				try {
					JPushInterface.setAlias(context, alias, callBack);

					try {
						lock.acquire();
					} catch (Exception e) {
						e.printStackTrace();
					}

					System.out.println("call back return");

					try {
						if (!stop && SelfMgr.getInstance().isLogin()) {
							Thread.sleep(100);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			System.out.println("out loop");
		}
	}
}
