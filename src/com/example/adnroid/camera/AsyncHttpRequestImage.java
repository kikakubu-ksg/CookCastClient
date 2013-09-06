package com.example.adnroid.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

public class AsyncHttpRequestImage extends AsyncTask<Uri.Builder, Void, String> {

	private View mView;

	public AsyncHttpRequestImage(View view) {

		// 呼び出し元のアクティビティ
		this.mView = view;
	}

	@Override
	protected String doInBackground(Uri.Builder... builder) {
		Log.d("TEST", "compressed-size: " + ((CameraPreview)this.mView).compressed.length);
		String address = Const.getEcocast_ip();
		String strPort = Const.getEcocast_port();
		int port = Integer.parseInt(strPort);

		Socket socket = null;

		try {
			//Camera.Parameters params = ((CameraPreview)this.mView).camera.getParameters();
			//Camera.Size size = params.getPreviewSize();
			Camera.Size size = Const.getEcocast_size();
			if(size == null){throw new IOException();}
			
			socket = new Socket(address, port);
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			OutputStreamWriter ow = new OutputStreamWriter(ba);

			StringBuffer sb = new StringBuffer();
			
			//String content = "text=aiueo";

			sb.append("POST / HTTP/1.0\r\n");
			sb.append("Host: " + address + ":" + strPort + "\r\n");
			sb.append("Connection: Close\r\n");
			sb.append("User-Agent: EcoCastClient\r\n");
			sb.append("Content-Type: image/jpeg\r\n");
			sb.append("Content-Length: " + ((CameraPreview)this.mView).compressed.length + "\r\n");
			sb.append("Preview-Width: " + size.width + "\r\n");
			sb.append("Preview-Height: " + size.height + "\r\n");
			sb.append("Preview-Angle: " + Const.getEcocast_radian() + "\r\n");
			sb.append("\r\n");
			//sb.append(content);
			
			ow.write(sb.toString());
			ow.flush();
			ba.write(((CameraPreview)this.mView).compressed);		
			ba.writeTo(socket.getOutputStream());
			

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// このメソッドは非同期処理の終わった後に呼び出されます
	@Override
	protected void onPostExecute(String result) {
		// 送信日時更新
	}
}