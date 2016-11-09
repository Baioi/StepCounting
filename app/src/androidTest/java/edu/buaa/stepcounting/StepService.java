package com.stepcounting;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class StepService extends Service {
	private Sensor sensor;
	private SensorManager sensorManager;
	private MyBinder mBinder = new MyBinder();
	private ICallback mCallback;
	private PowerManager.WakeLock wakeLock;
	private StepCount stepCount;
	private long stepBefore;
	private StepDetector mStepDetector;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	public void onCreate(){
		super.onCreate();
		//��û����������볣��״̬
		this.wakeLock = ((PowerManager)getSystemService("power")).newWakeLock(1, "StepCount");
		this.wakeLock.acquire();
		//this.mStepDetector = new StepDetector();
		this.sensorManager = (SensorManager)getSystemService("SENSOR_SERVICE");
		//G-Sensor������ֵΪ1
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.sensorManager.registerListener(this.mStepDetector, this.sensor, 
				SensorManager.SENSOR_DELAY_UI);
		//��ʼ���Ʋ�������ⲽ��
		stepCount = new StepCount();
		mStepDetector = new StepDetector(stepCount);
	}
	protected void onPause(){
		super.onDestroy();
		//�ͷŻ�����
		wakeLock.release();
	}
	public void onDestroy(){
		this.sensorManager.unregisterListener(this.mStepDetector);
		this.wakeLock.release();
		super.onDestroy();
		//???Ϊʲô��������
		startService(new Intent(this, StepService.class));
	}
	public int onStartCommand(Intent intent, int flags, int startId){
		return super.onStartCommand(intent, flags, startId);
	}
	public boolean onUnbind(Intent intent){
		return super.onUnbind(intent);
	}
	public void registerCallback(ICallback paramICallback){
	    this.mCallback = paramICallback;
	}

	public void resetValues(){
	    this.stepCount.setSteps(0);
	}
	public static abstract interface ICallback{
		public abstract void stepsChanged(int paramInt);
	}
}
class MyBinder extends Binder{
	public void startWork(){
		Log.d("TAG", "Work excuting");
		//Jobs to do
	}
}
