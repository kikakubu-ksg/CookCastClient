package com.example.adnroid.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Deflater;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	public CameraPreview cp;
	protected Context context;
	private SurfaceHolder holder;
	public Camera camera;
	public byte[] compressed = null;
	public Timer timer = null;
	private Handler handler = new Handler();
	protected boolean camera_status = false;
	public String ecocast_ip;
	public String ecocast_port;
	
	
//	private int surWidth = 240;
//	private int surHeight = 400;
//	private Matrix matrix90 = new Matrix();			//90度回転用
//	private int[] rgb_bitmap = new int[128 * 128];	//画像切り出しよう

	private final Camera.PreviewCallback _previewCallback = new Camera.PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			// Log.d("TEST", "onPreviewFrame: preview: data=" + data);
			if (camera == null) {
				return;
			}
			try {
				// stop
				camera.stopPreview();
				camera.setPreviewCallback(null);
				//((Activity) context).finish();

				// zip
				Deflater compresser = new Deflater();
				compresser.setLevel(Deflater.BEST_COMPRESSION);

				compresser.setInput(data);
				compresser.finish();
				
				ByteArrayOutputStream compos = new ByteArrayOutputStream(
						data.length);

				byte[] buf = new byte[1024];
				int count;
				while (!compresser.finished()) {
					count = compresser.deflate(buf);
					compos.write(buf, 0, count);
				}
				Log.d("TEST", "Size:" + data.length + "->"
						+ compos.toByteArray().length);

				compressed = compos.toByteArray();
				//compressed = data;

				// send
				AsyncHttpRequestImage asyncHttpRequest = new AsyncHttpRequestImage(cp);
				asyncHttpRequest.execute(new Uri.Builder());

				// wait 連続時

				// restart
				// camera.setPreviewCallback(_previewCallback);
				//camera.startPreview();
				camera_status = false;
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

		}
	};

	CameraPreview(Context context) {
		super(context);
		this.context = context;
		cp = this;
		holder = getHolder();
		holder.addCallback(this);
	}
	
	CameraPreview(Context context, SurfaceView sv) {
		super(context);
		this.context = context;
		cp = this;
		holder = sv.getHolder();
		holder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("TEST", "surfaceCreated");
		if (camera == null) {
			try {
				camera = Camera.open();
				Camera.Parameters param = camera.getParameters();
				Const.setSupportedSizes(param.getSupportedPreviewSizes());
			} catch (RuntimeException e) {
				((Activity) context).finish();
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
						.show();
			}
		}
		if (camera != null) {
			camera.setPreviewCallback(_previewCallback);
		}
		try {
			
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			camera.release();
			camera = null;
			((Activity) context).finish();
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("TEST", "surfaceChanged");
		if (camera == null) {
			((Activity) context).finish();
		} else if(((Preview)context).video_flag) {
			camera.stopPreview();
			setPictureFormat(format);
			//setPreviewSize(width, height);
			List<Size> supporetedSize = Const.getSupportedSizes();
			if(Const.ecocast_size != null && supporetedSize.contains(Const.ecocast_size)){
				setPreviewSize(Const.ecocast_size.width, Const.ecocast_size.height);
			} else {
				Size previewSize = Const.getSupportedSizes().get(0);
				setPreviewSize(previewSize.width, previewSize.height);
				Const.setEcocast_size(Const.getSupportedSizes().get(0));
			}
			
//			Camera.Parameters params = camera.getParameters();
//			params.setPreviewSize(640, 480);
//			camera.setParameters(params);
//			getLayoutParams().width = 50;
//			getLayoutParams().height = 50;
			// 親の大きさも設定
//			View parent = (View)getParent();
//			parent.getLayoutParams().width = 50;
//			parent.getLayoutParams().height = 50;
            if(timer == null)
            {
                //==== タイマー作成 & スタート ====//
            	String sec = Const.getEcocast_sec();
            	Integer intsec = Integer.parseInt(sec);
                timer = new Timer();
                timer.schedule(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        handler.post(new Runnable()
                        {
                            public void run()
                            {
								if (camera != null && !camera_status) {
									camera.setPreviewCallback(_previewCallback);
									camera.startPreview();
									camera_status = true;
								}

                            }
                        });
                    }
                //}, 0, 5000);
                }, 0, intsec * 1000);
            }
//			camera.setPreviewCallback(_previewCallback);
//			camera.startPreview();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("TEST", "surfaceDestroyed");
		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}

	protected void setPictureFormat(int format) {
		try {
			Camera.Parameters params = camera.getParameters();
			List<Integer> supported = params.getSupportedPictureFormats();
			if (supported != null) {
				for (int f : supported) {
					if (f == format) {
						params.setPreviewFormat(format);
						camera.setParameters(params);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setPreviewSize(int width, int height) {
		Camera.Parameters params = camera.getParameters();
		List<Camera.Size> supported = params.getSupportedPreviewSizes();
		if (supported != null) {
			for (Camera.Size size : supported) {
				if (size.width <= width && size.height <= height) {
					params.setPreviewSize(size.width, size.height);
					camera.setParameters(params);
					break;
				}
			}
		}
	}

	protected void setAntibanding(String antibanding) {
		Camera.Parameters params = camera.getParameters();
		List<String> supported = params.getSupportedAntibanding();
		if (supported != null) {
			for (String ab : supported) {
				if (ab.equals(antibanding)) {
					params.setAntibanding(antibanding);
					camera.setParameters(params);
					break;
				}
			}
		}
	}

	protected void setColorEffect(String effect) {
		Camera.Parameters params = camera.getParameters();
		List<String> supported = params.getSupportedColorEffects();
		if (supported != null) {
			for (String e : supported) {
				if (e.equals(effect)) {
					params.setColorEffect(effect);
					camera.setParameters(params);
					break;
				}
			}
		}
	}

	protected void setFlashMode(String flash_mode) {
		Camera.Parameters params = camera.getParameters();
		List<String> supported = params.getSupportedFlashModes();
		if (supported != null) {
			for (String fm : supported) {
				if (fm.equals(flash_mode)) {
					params.setFlashMode(flash_mode);
					camera.setParameters(params);
					break;
				}
			}
		}
	}

	protected void setFocusMode(String focus_mode) {
		Camera.Parameters params = camera.getParameters();
		List<String> supported = params.getSupportedFocusModes();
		if (supported != null) {
			for (String fm : supported) {
				if (fm.equals(focus_mode)) {
					params.setFocusMode(focus_mode);
					camera.setParameters(params);
					break;
				}
			}
		}
	}

	protected void setSceneMode(String scene_mode) {
		Camera.Parameters params = camera.getParameters();
		List<String> supported = params.getSupportedSceneModes();
		if (supported != null) {
			for (String sm : supported) {
				if (sm.equals(scene_mode)) {
					params.setSceneMode(scene_mode);
					camera.setParameters(params);
					break;
				}
			}
		}
	}

	protected void setWhiteBalance(String white_balance) {
		Camera.Parameters params = camera.getParameters();
		List<String> supported = params.getSupportedWhiteBalance();
		if (supported != null) {
			for (String wb : supported) {
				if (wb.equals(white_balance)) {
					params.setWhiteBalance(white_balance);
					camera.setParameters(params);
					break;
				}
			}
		}
	}
	
	//stop externally
	public void stopCameraPreview(){
		if(timer != null){timer.cancel();}
		timer = null;
		camera.stopPreview();
		camera.setPreviewCallback(null);
		camera_status = false;
		//camera.release();
		//camera = null;
	}
	
	//*******************************************
	// YUV420をRGBに変換する
	// データフォーマットは、最初に画面サイズ(Width*Height)分のY値が並び、
	// 以降は、横方向、縦方向共に、V,Uの順番に2画素分を示して並ぶ
	//
	// 4×3ドットがあったとすると、YUV420のデータは
	//  0 1 2 3
	// 0○○○○　Y00 Y01 Y02 Y03 Y10 Y11 Y12 Y13 Y20 Y21 Y22 Y23 V00 U00 V02 U02 V20 U20 V22 U22 となる。
	// 1○○○○　V00はY00,Y01,Y10,Y11の4ピクセルの赤色差を表し、U00はY00,Y01,Y10,Y11の4ピクセルの青色差を表す
	// 2○○○○
	//
	// width×heightの画像から (offsetX,offsetY)座標を左上座標としたgetWidth,GetHeightサイズのrgb画像を取得する
	//*******************************************
//	public void decodeYUV420SP2(int[] int_rgb, byte[] yuv420sp, int width, int height, int offsetX, int offsetY, int getWidth, int getHeight) {
//
//		//全体ピクセル数を求める
//		final int frameSize = width * height;
//
//		int uvp, y;
//		int y1164, r, g, b;
//		int i, yp;
//		int u = 0;
//		int v = 0;
//		int uvs = 0;
//
//		if(offsetY+getHeight>height){
//			getHeight = height - offsetY;
//		}
//
//		if(offsetX+getWidth>width){
//			getWidth = width - offsetX;
//		}
//
//		int qp = 0;	//rgb配列番号
//
//		for (int j = offsetY; j < offsetY + getHeight; j++) {
//			//1ライン毎の処理
//			uvp = frameSize + (j >> 1) * width;
//
//			//offsetXが奇数の場合は、1つ前のU,Vの値を取得する
//			if((offsetX & 1)!=0){
//				uvs = uvp + offsetX-1;
//				// VとUのデータは、2つに1つしか存在しない。よって、iが偶数のときに読み出す
//				v = (0xff & yuv420sp[uvs]) - 128;		//無彩色(色差0)が128なので、128を引く
//				u = (0xff & yuv420sp[uvs + 1]) - 128;		//無彩色(色差0)が128なので、128を引く
//			}
//
//			for (i = offsetX; i < offsetX + getWidth; i++) {
//
//				yp = j*width + i;
//
//				//左からピクセル単位の処理
//				y = (0xff & ((int) yuv420sp[yp])) - 16;		//Yの下限が16だから、16を引きます
//				if (y < 0){
//					y = 0;
//				}
//
//				if ((i & 1) == 0) {
//					uvs = uvp + i;
//					// VとUのデータは、2つに1つしか存在しない。よって、iが偶数のときに読み出す
//					v = (0xff & yuv420sp[uvs]) - 128;		//無彩色(色差0)が128なので、128を引く
//					u = (0xff & yuv420sp[uvs + 1]) - 128;		//無彩色(色差0)が128なので、128を引く
//				}
//
//				//変換の計算式によりR,G,Bを求める(Cb=U, Cr=V)
//				// R = 1.164(Y-16)                 + 1.596(Cr-128)
//				// G = 1.164(Y-16) - 0.391(Cb-128) - 0.813(Cr-128)
//				// B = 1.164(Y-16) + 2.018(Cb-128)
//				y1164 = 1164 * y;
//				r = (y1164 + 1596 * v);
//				g = (y1164 - 391 * u - 813 * v);
//				b = (y1164 + 2018 * u);
//
//				if (r < 0)
//					r = 0;
//				else if (r > 262143)
//					r = 262143;
//				if (g < 0)
//					g = 0;
//				else if (g > 262143)
//					g = 262143;
//				if (b < 0)
//					b = 0;
//				else if (b > 262143)
//					b = 262143;
//
//				int_rgb[qp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
//				qp++;
//			}
//		}
//	}
}
