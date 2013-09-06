package com.example.adnroid.camera;

import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.util.Log;
import android.view.MotionEvent;

public class AutoFocusPreview extends CameraPreview {
	AutoFocusPreview(Context context) {
		super(context);
	}
	
	void autoFocus() {
		camera.autoFocus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, final Camera camera) {
				ShutterCallback shutter = new ShutterCallback() {
					@Override
					public void onShutter() {
						Log.d("TEST", "onShutter");
					}
				};
				PictureCallback raw = new PictureCallback() {
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						Log.d("TEST", "onPictureTaken: raw: data=" + data);
					}
				};
				PictureCallback jpeg = new PictureCallback() {
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						Log.d("TEST", "onPictureTaken: jpeg: data=" + data);
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream("/sdcard/test.jpg");
							fos.write(data);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (fos != null) {
								try {
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				};
				camera.takePicture(shutter, raw, jpeg);
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
						}
						camera.startPreview();
					}
				}.start();
			}
		});
	}
	
	void cancelAutoFocus() {
		camera.cancelAutoFocus();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		autoFocus();
		return true;
	}
}
