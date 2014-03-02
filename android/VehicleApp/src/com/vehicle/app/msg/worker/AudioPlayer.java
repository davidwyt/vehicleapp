package com.vehicle.app.msg.worker;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;

public class AudioPlayer {

	public void play(final String path) {

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				MediaPlayer player = new MediaPlayer();
				try {

					player.setDataSource(path);
					player.prepare();

					// int duration = player.getDuration();

					player.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			asyncTask.execute();
		}
	}
}
