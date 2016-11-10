package edu.buaa.stepcounting;

import android.app.Service;
import android.content.Context;
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
		//获得唤醒锁，进入常亮状态
		this.wakeLock = ((PowerManager)getSystemService(Context.POWER_SERVICE)).newWakeLock(1, "StepCount");
		this.wakeLock.acquire();
		//this.mStepDetector = new StepDetector();
		this.sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		////G-Sensor，类型值为1
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.sensorManager.registerListener(this.mStepDetector, this.sensor, 
				SensorManager.SENSOR_DELAY_UI);
		//初始化计步器、检测步数
		stepCount = new StepCount();
		mStepDetector = new StepDetector(stepCount);
	}
	protected void onPause(){
		super.onDestroy();
		//释放唤醒锁
		wakeLock.release();
	}
	public void onDestroy(){
		this.sensorManager.unregisterListener(this.mStepDetector);
		this.wakeLock.release();
		super.onDestroy();
		//???为什么又启动？保持后台运行？
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
	StepService getService(){
		return
	}
}
