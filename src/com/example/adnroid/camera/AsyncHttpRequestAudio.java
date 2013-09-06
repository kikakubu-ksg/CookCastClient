package com.example.adnroid.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

public class AsyncHttpRequestAudio extends AsyncTask<Uri.Builder, Void, String> {

	private Activity activity;

	public AsyncHttpRequestAudio(Activity act) {

		// 呼び出し元のアクティビティ
		this.activity = act;
	}

	@Override
	protected String doInBackground(Uri.Builder... builder) {
		String address = Const.getEcocast_ip();
		String strPort = Const.getEcocast_port();
		int port = Integer.parseInt(strPort);

		Socket socket = null;

		try {
			
			socket = new Socket(address, port);

			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			OutputStreamWriter ow = new OutputStreamWriter(ba);

			StringBuffer sb = new StringBuffer();

			sb.append("POST / HTTP/1.0\r\n");
			sb.append("Host: " + address + ":" + strPort + "\r\n");
			sb.append("Connection: Close\r\n");
			sb.append("User-Agent: EcoCastClient\r\n");
			sb.append("Content-Type: application/x-audio-pushstart\r\n");
			sb.append("\r\n");
			
			ow.write(sb.toString());
			ow.flush();		
			ba.writeTo(socket.getOutputStream());
			
			Singleton instance =  Singleton.getInstance();
			ParcelFileDescriptor pfd = ParcelFileDescriptor.fromSocket(socket);
			MediaRecorder recorder = new MediaRecorder();
			
			instance.setSocket(socket);
			instance.setPfd(pfd);
			instance.setRecorder(recorder);

			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
	        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        recorder.setOutputFile(pfd.getFileDescriptor());
	        
	        //録音準備＆録音開始
	        try {
	            recorder.prepare();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        recorder.start();   //録音開始
	 

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		if (socket != null) {
//			try {
//				socket.close();
//				socket = null;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return null;
	}

	// このメソッドは非同期処理の終わった後に呼び出されます
	@Override
	protected void onPostExecute(String result) {
		// 送信日時更新
		Toast toast = Toast.makeText(this.activity.getApplicationContext(),
				"Subtitle has sent.", Toast.LENGTH_SHORT);
		toast.show();
	}
}