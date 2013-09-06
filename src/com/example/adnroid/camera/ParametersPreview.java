package com.example.adnroid.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class ParametersPreview extends AutoFocusPreview {
	private Activity activity;
	private Bundle bundle;
	private boolean isUsingCamera;
	
	ParametersPreview(Context context) {
		super(context);
		activity = (Activity)context;
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		if (camera != null) {
			Camera.Parameters params = camera.getParameters();
			SharedPreferences sp = activity.getSharedPreferences(activity.getPackageName() + "_preferences", Context.MODE_PRIVATE);
			String value;
			if (bundle == null) {
				bundle = new Bundle();
				if ((value = sp.getString("antibanding", params.getAntibanding())) != null) {
					bundle.putString("antibanding", value);
				}
				if ((value = sp.getString("effect", params.getColorEffect())) != null) {
					bundle.putString("effect", value);
				}
				if ((value = sp.getString("flash_mode", params.getFlashMode())) != null) {
					bundle.putString("flash_mode", value);
				}
				if ((value = sp.getString("focus_mode", params.getFocusMode())) != null) {
					bundle.putString("focus_mode", value);
				}
				if ((value = sp.getString("scene_mode", params.getSceneMode())) != null) {
					bundle.putString("scene_mode", value);
				}
				if ((value = sp.getString("white_balance", params.getWhiteBalance())) != null) {
					bundle.putString("white_balance", value);
				}
			}
			if ((value = bundle.getString("antibanding")) != null) {
				params.setAntibanding(value);
			}
			if ((value = bundle.getString("effect")) != null) {
				params.setColorEffect(value);
			}
			if ((value = bundle.getString("flash_mode")) != null) {
				params.setFlashMode(value);
			}
			if ((value = bundle.getString("focus_mode")) != null) {
				params.setFocusMode(value);
			}
			if ((value = bundle.getString("scene_mode")) != null) {
				params.setSceneMode(value);
			}
			if ((value = bundle.getString("white_balance")) != null) {
				params.setWhiteBalance(value);
			}
			camera.setParameters(params);
		}
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (isUsingCamera) {
			if (camera != null) {
				camera.stopPreview();
			}
		} else {
			super.surfaceDestroyed(holder);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("TEST", "x:" + event.getX() + " y:" + event.getY() + " w:" + getWidth() + " h:" + getHeight());
		if (event.getX() > getWidth() / 2 || event.getY() > getHeight() / 2) {
			autoFocus();
		} else {
			Intent intent = new Intent(activity, CameraPreferences.class);
			intent.putExtras(bundle);
			isUsingCamera = true;
			activity.startActivityForResult(intent, 0);
		}
		return true;
	}
	
	public void setCameraParameters(Bundle bundle) {
		this.bundle = bundle;
		isUsingCamera = false;
	}
}
