package com.example.adnroid.camera;

import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class CameraPreviewService extends Service {
	class CameraPreviewBinder extends Binder {
		
		CameraPreviewService getService() {
			return CameraPreviewService.this;
		}
		
	}
	
	public static final String ACTION = "Camera Preview Service";
	private Timer timer;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("TEST", "onCreate");
		Intent intent = new Intent(this, Preview.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("TEST", "onStartCommand");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("TEST", "onDestroy");
	}

}
