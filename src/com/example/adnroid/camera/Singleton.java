package com.example.adnroid.camera;

import java.net.Socket;

import android.media.MediaRecorder;
import android.os.ParcelFileDescriptor;

public class Singleton {
	private static Singleton instance;

	//private boolean s_audio_flag = false;
	private MediaRecorder recorder;
	private Socket socket;
	private ParcelFileDescriptor pfd;
	
	public ParcelFileDescriptor getPfd() {
		return pfd;
	}

	public void setPfd(ParcelFileDescriptor pfd) {
		this.pfd = pfd;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	

	public MediaRecorder getRecorder() {
		return recorder;
	}

	public void setRecorder(MediaRecorder recorder) {
		this.recorder = recorder;
	}

	private Singleton() {
	}

	public static synchronized Singleton getInstance() {
		if (instance == null) {
			instance = new Singleton();
		}
		return instance;
	}

}
