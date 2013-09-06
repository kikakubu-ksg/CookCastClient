package com.example.adnroid.camera;

import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class Preview extends Activity implements OnClickListener, SensorEventListener {
	private SurfaceView mSurfaceView;
	private CameraPreview mCameraView;
	private SensorManager manager;
	public boolean video_flag = false;
	public boolean audio_flag = false;
	private int layout_flag = 1;
	
	private boolean mIsMagSensor;
    private boolean mIsAccSensor;
    
    private static final int MATRIX_SIZE = 16;
    /* ��]�s�� */
    float[]  inR = new float[MATRIX_SIZE];
    float[] outR = new float[MATRIX_SIZE];
    float[]    I = new float[MATRIX_SIZE];
    /* �Z���T�[�̒l */
    float[] orientationValues   = new float[3];
    float[] magneticValues      = new float[3];
    float[] accelerometerValues = new float[3];

	@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.preview);
		mSurfaceView = (SurfaceView)findViewById(R.id.SurfaceViewMain);
		mCameraView = new CameraPreview(this, mSurfaceView);
		//mCameraView.setLayoutParams(new FrameLayout.LayoutParams(100, 100));
		//mCameraView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		// setContentView(new CameraPreview(this));
		// WindowManager.LayoutParams lp = getWindow().getAttributes();
		// lp.screenBrightness = 0.0f;
		// getWindow().setAttributes(lp);
		//setContentView(mCameraView);
		// new CameraPreview(this);
		
		SharedPreferences pref =
				getSharedPreferences("pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		Const.setEcocast_ip( pref.getString("ecocast_ip", Const.ECOCAST_IP));
		Const.setEcocast_port( pref.getString("ecocast_port", Const.ECOCAST_PORT));
		Const.setEcocast_sec( pref.getString("ecocast_sec", Const.ECOCAST_SEC));
		
		View showButton = null;
		showButton = findViewById(R.id.settings);
	    showButton.setOnClickListener(this);
	    showButton = findViewById(R.id.subtitle);
	    showButton.setOnClickListener(this);
	    showButton = findViewById(R.id.video);
	    showButton.setOnClickListener(this);
//	    showButton = findViewById(R.id.audio);
//	    showButton.setOnClickListener(this);
//	    showButton = findViewById(R.id.e);
//	    showButton.setOnClickListener(this);
	    
	    manager = (SensorManager)getSystemService(SENSOR_SERVICE);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Listener�̓o�^����
		// manager.unregisterListener(this);
		//�Z���T�[�}�l�[�W���̃��X�i�o�^�j��
	    if (mIsMagSensor || mIsAccSensor) {
	    	manager.unregisterListener(this);
	        mIsMagSensor = false;
	        mIsAccSensor = false;
	    }
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
    public void onClick(View v) {
        if (v.getId() == R.id.settings){ //�ݒ�
        	Toast toast = Toast.makeText(getApplicationContext(),
					"Setting button clicked.", Toast.LENGTH_SHORT);
			toast.show();
			
			Intent intent = new Intent(Preview.this,
					Settings.class);
			startActivity(intent);
        } else if(v.getId() == R.id.video){
        	Toast toast = Toast.makeText(getApplicationContext(),
					"Video button clicked.", Toast.LENGTH_SHORT);
			toast.show();
			
//			if (!video_flag) { // start
//				Button button = (Button) v;
//				button.setText("stop", BufferType.NORMAL);
//				mSurfaceView = (SurfaceView) findViewById(R.id.SurfaceViewMain);
//				mCameraView = new CameraPreview(this, mSurfaceView);
				// cause surfacechange event
				mSurfaceView.setLayoutParams(new LinearLayout.LayoutParams(100 + layout_flag, 100));
				if(layout_flag == 0){ layout_flag = 1;}else{ layout_flag = 0;}
//				video_flag = true;
//			} else { // stop
//				Button button = (Button) v;
//				button.setText("VIDEO", BufferType.NORMAL);
//				mCameraView.stopCameraPreview();
//				// mSurfaceView = null;
//				video_flag = false;
//			}
//			
        	
        } 
//        else if(v.getId() == R.id.audio){
//        	Toast toast = Toast.makeText(getApplicationContext(),
//					"Audio button clicked.", Toast.LENGTH_SHORT);
//			toast.show();
//			
//			if (!audio_flag) { // start
//				Button button = (Button) v;
//				button.setText("stop", BufferType.NORMAL);
//				// start recording
//				AsyncHttpRequestAudio asyncHttpRequest = new AsyncHttpRequestAudio(this);
//				asyncHttpRequest.execute(new Uri.Builder());
//				
//				audio_flag = true;
//			} else { // stop
//				Button button = (Button) v;
//				button.setText("AUDIO", BufferType.NORMAL);
//				// stop recording
//				Singleton instance =  Singleton.getInstance();
//				MediaRecorder recorder = instance.getRecorder();
//				recorder.stop();
//				recorder.reset(); 
//				recorder.release(); 
//
//				try {
//					instance.getPfd().close();
//					instance.getSocket().close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//				audio_flag = false;
//			}
			
//        } 
	else if(v.getId() == R.id.subtitle){
        	// send
        	AsyncHttpRequestMessage asyncHttpRequestMessage = new AsyncHttpRequestMessage(this);
        	Uri.Builder builder = new Uri.Builder();
        	EditText edit = (EditText) findViewById(R.id.editText_sub);
        	builder.appendQueryParameter("text", edit.getText().toString());
        	asyncHttpRequestMessage.execute(builder);
        }
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onResume() {
		//TODO Auto-generated method stub
		super.onResume();
		// Listener�̓o�^
		List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);
//		if (sensors.size() > 0) {
//			Sensor s = sensors.get(0);
//			manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
//		}
		
		// �Z���T�}�l�[�W���փ��X�i�[��o�^(implements SensorEventListener�ɂ��Athis�œo�^����)
        for (Sensor sensor : sensors) {
 
            if( sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            	manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
                mIsMagSensor = true;
            }
 
            if( sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            	manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
                mIsAccSensor = true;
            }
        }
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) return;
		 
	    switch (event.sensor.getType()) {
	        case Sensor.TYPE_MAGNETIC_FIELD:
	            magneticValues = event.values.clone();
	            break;
	        case Sensor.TYPE_ACCELEROMETER:
	            accelerometerValues = event.values.clone();
	                            break;
	    }
	 
	    if (magneticValues != null && accelerometerValues != null) {
	 
	        SensorManager.getRotationMatrix(inR, I, accelerometerValues, magneticValues);
	 
	        //Activity�̕\�����c�Œ�̏ꍇ�B�������ɂȂ�ꍇ�A�C�����K�v�ł�
	        SensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR);
	        SensorManager.getOrientation(outR, orientationValues);
	 
//	        String str = "";
//	        str= String.valueOf( radianToDegree(orientationValues[0]) ) + ", " + //Z������,azmuth
//	            String.valueOf( radianToDegree(orientationValues[1]) ) + ", " + //X������,pitch
//	            String.valueOf( radianToDegree(orientationValues[2]) ) ;       //Y������,roll
//	        TextView tv = (TextView) findViewById(R.id.textView_radian);
//			tv.setText(str, BufferType.NORMAL);
			Const.setEcocast_radian((float)(orientationValues[2]+Math.PI/2));
	    }
	    
//		String str = "";
//		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
//			str = "X��:" + event.values[SensorManager.DATA_X] + "\nY��:"
//					+ event.values[SensorManager.DATA_Y] + "\nZ��:"
//					+ event.values[SensorManager.DATA_Z];
//			
//			TextView tv = (TextView) findViewById(R.id.textView_radian);
//			tv.setText(str, BufferType.NORMAL);
//			
//			Const.setEcocast_radian(event.values[SensorManager.DATA_Z]);
//			
//			//values.setText(str);
//		}
	}
	
	int radianToDegree(float rad){
	    return (int) Math.floor( Math.toDegrees(rad) ) ;
	}
}
