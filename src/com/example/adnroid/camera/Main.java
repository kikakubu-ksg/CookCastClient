package com.example.adnroid.camera;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends Activity {
	private Object[] activities = { 
			"Preview", Preview.class, 
			"AutoFocus", AutoFocus.class, "Parameters", Parameters.class, "Overlay",
			Overlay.class, "Test", LiveCameraActivity.class, };

	private class CameraPreviewReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Data sending has done.", Toast.LENGTH_SHORT);
			toast.show();

		}
	}

//	private CameraPreviewService cameraPreviewService;
//	private final CameraPreviewReceiver receiver = new CameraPreviewReceiver();
//	private ServiceConnection serviceConnection = new ServiceConnection() {
//
//		@Override
//		public void onServiceConnected(ComponentName className, IBinder service) {
//			cameraPreviewService = ((CameraPreviewService.CameraPreviewBinder) service)
//					.getService();
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName className) {
//			cameraPreviewService = null;
//		}
//
//	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		CharSequence[] list = new CharSequence[activities.length / 2];
		for (int i = 0; i < list.length; i++) {
			list[i] = (String) activities[i * 2];
		}

		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_list_item_1, list);
		ListView listView = (ListView) findViewById(R.id.ListView01);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Main.this,
						(Class<?>) activities[position * 2 + 1]);
				startActivity(intent);

				// // サービスを開始
				// Intent intent = new Intent(Main.this,
				// CameraPreviewService.class);
				// startService(intent);
				// IntentFilter filter = new
				// IntentFilter(CameraPreviewService.ACTION);
				// registerReceiver(receiver, filter);
				//
				// // サービスにバインド
				// bindService(intent, serviceConnection,
				// Context.BIND_AUTO_CREATE);

			}
		});
	}
}
