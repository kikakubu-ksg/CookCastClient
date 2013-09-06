package com.example.adnroid.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class Overlay extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new CameraPreview(this));
		addContentView(new OverlayView(this), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	
	class OverlayView extends View {
		private float x = -1;
		private float y = -1;
		private Bitmap bitmap;

		public OverlayView(Context context) {
			super(context);
			bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.androidmarker);
			setFocusable(true);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawColor(Color.TRANSPARENT);
			if (x >= 0) {
				canvas.drawBitmap(bitmap, x, y, null);
			}
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			x = event.getX();
			y = event.getY();
			invalidate();
			return true;
		}
	}
}
