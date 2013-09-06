package com.example.adnroid.camera;

import java.util.List;

import android.hardware.Camera.Size;

public class Const {

	public static String ECOCAST_IP = "192.168.0.2";
	public static String ECOCAST_PORT = "12345";
	public static String ECOCAST_SEC = "5";
	public static String ecocast_ip = null;
	public static String ecocast_port = null;
	public static List<Size> supportedSizes = null;
	public static Size ecocast_size = null;
	public static int ecocast_sizeindex = 0;
	public static String ecocast_sec = null;
	
	public static String getEcocast_sec() {
		return ecocast_sec;
	}
	public static void setEcocast_sec(String ecocast_sec) {
		Const.ecocast_sec = ecocast_sec;
	}
	public static float ecocast_radian = 0;
	
	
	public static float getEcocast_radian() {
		return ecocast_radian;
	}
	public static void setEcocast_radian(float ecocast_radian) {
		Const.ecocast_radian = ecocast_radian;
	}
	public static int getEcocast_sizeindex() {
		return ecocast_sizeindex;
	}
	public static void setEcocast_sizeindex(int ecocast_sizeindex) {
		Const.ecocast_sizeindex = ecocast_sizeindex;
	}
	public static Size getEcocast_size() {
		return ecocast_size;
	}
	public static void setEcocast_size(Size ecocast_size) {
		Const.ecocast_size = ecocast_size;
	}
	public static List<Size> getSupportedSizes() {
		return supportedSizes;
	}
	public static void setSupportedSizes(List<Size> supportedSizes) {
		Const.supportedSizes = supportedSizes;
	}
	
	public static String getEcocast_ip() {
		return ecocast_ip;
	}
	public static void setEcocast_ip(String ecocast_ip) {
		Const.ecocast_ip = ecocast_ip;
	}
	public static String getEcocast_port() {
		return ecocast_port;
	}
	public static void setEcocast_port(String ecocast_port) {
		Const.ecocast_port = ecocast_port;
	}
	
	
	
}
