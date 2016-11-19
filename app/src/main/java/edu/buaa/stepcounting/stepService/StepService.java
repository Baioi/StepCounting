package edu.buaa.stepcounting.stepService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import edu.buaa.stepcounting.Main3Activity;
import edu.buaa.stepcounting.database.DatabaseHelper;
import edu.buaa.stepcounting.database.ExerciseRecord;

public class StepService extends Service {
	private Sensor sensor;
	private SensorManager sensorManager;
	private final MyBinder mBinder = new MyBinder();
	private ICallback mCallback;
	private PowerManager.WakeLock wakeLock;
	private StepCount stepCount;
	private int mStep;
	private int stepBefore = 0;
	private StepDetector mStepDetector;
	private ExerciseRecord exerciseRecord;
	private DatabaseHelper dbhelper = new DatabaseHelper(StepService.this);
	private Calendar c = Calendar.getInstance();
	//private Main3Activity.MyHandler mHandler;
	private StepValuePassListener mStepValuePassListener = new StepValuePassListener() {
		@Override
		public void stepsChanged(int n) {
			mStep = n;
			//发送信息
			//Bundle bundle = new Bundle();
			//bundle.putInt("steps",mStep+stepBefore);
			Intent intent = new Intent("edu.buaa.stepcounting");
			intent.putExtra("data", mStep+stepBefore);
			System.out.println("");
			System.out.println("update!");
			sendBroadcast(intent);

			//Message msg = new Message();
			//msg.setData(bundle);
			//mHandler.sendMessage(msg);

			exerciseRecord.setStep(mStep+stepBefore);
			System.err.println("starttttt insert");
			System.err.println("size:"+dbhelper.search(ExerciseRecord.class).size());
			System.err.println(((ExerciseRecord)dbhelper.search(ExerciseRecord.class).get(0)).getStep());
			System.err.println(exerciseRecord.getStep());
			ExerciseRecord r = ((ExerciseRecord)dbhelper.search(ExerciseRecord.class).get(0));
			System.err.println("insertID"+exerciseRecord.getId()+" DBiD"+r.getId());
			dbhelper.update(exerciseRecord);

		}

		@Override
		public void passValue() {
			//调用Activity中的stepsChanged()，更新UI
		}
	};

	//以下为启动service需要用到的，startService或者boundService都可以
	public class MyBinder extends Binder{
		StepService getService(){
			return StepService.this;
		}
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	public boolean onUnbind(Intent intent){
		return super.onUnbind(intent);
	}

	public int onStartCommand(Intent intent, int flags, int startId){
		return super.onStartCommand(intent, flags, startId);
	}

	//需要调用registerHandler传入handler以传出数据，传出数据的代码在mStepValuePassListener
	public static abstract interface ICallback{
		public abstract void stepsChanged(int paramInt);
	}
	public void registerCallback(ICallback iCallback){
		this.mCallback = iCallback;
	}
	/*public void startStepDetector(){
		if(sensorManager != null && mStepDetector !=null){
			sensorManager.unregisterListener(mStepDetector);
			sensorManager = null;
			mStepDetector = null;
		}
		//getLock(this);
		//获取传感器管理器的实例
		sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
	}*/
	public void onCreate(){
		super.onCreate();
		//this.deleteDatabase(DatabaseHelper.databaseName);
		//获得唤醒锁，进入常亮状态
		//从数据库中读取当天已行走的步数，若无数据，添加计步量为0
		Log.d("hello", "Service onCreate: ");
		exerciseRecord = new ExerciseRecord();
		exerciseRecord.setDay(c.get(Calendar.DAY_OF_MONTH));
		exerciseRecord.setMonth(c.get(Calendar.MONTH)+1);
		exerciseRecord.setYear(c.get(Calendar.YEAR));
		List<ExerciseRecord> list = (List<ExerciseRecord>)dbhelper.search(ExerciseRecord.class,
				new String[]{ExerciseRecord.keys.year, ExerciseRecord.keys.month, ExerciseRecord.keys.day} ,
				new String[]{String.valueOf(exerciseRecord.getYear()), String.valueOf(exerciseRecord.getMonth()), String.valueOf(exerciseRecord.getDay())});
		for(int i = 0; i < list.size(); i++) {
			stepBefore += list.get(i).getStep();
		}
		if(list.size() == 0){
			exerciseRecord.setStep(0);
			dbhelper.insert(exerciseRecord);
		}
		else {
			exerciseRecord = list.get(0);
		}
		//this.mHandler = ((MyApp)getApplication()).getMyHandler();
		mStepValuePassListener.stepsChanged(0);
		//this.wakeLock = ((PowerManager)getSystemService(Context.POWER_SERVICE)).newWakeLock(1, "StepCount");
		//this.wakeLock.acquire();
		//this.mStepDetector = new StepDetector();
		this.sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		////G-Sensor，类型值为1
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		stepCount = new StepCount();
		stepCount.registerListener(mStepValuePassListener);
		mStepDetector = new StepDetector(stepCount);
		this.sensorManager.registerListener(this.mStepDetector, this.sensor, 
				SensorManager.SENSOR_DELAY_UI);
				//SensorManager.SENSOR_DELAY_FASTEST);
		//this.mHandler = ((MyApp)getApplication()).getMyHandler();
		//初始化计步器、检测步数


		/*new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//startStepDetector();
			}
		}).start();*/
	}
	protected void onPause(){
		onDestroy();
		//释放唤醒锁
		wakeLock.release();
	}


	@Override
	public void onDestroy(){
		//将mStep加入数据库
		//this.sensorManager.unregisterListener(this.mStepDetector);
		this.wakeLock.release();
		super.onDestroy();
		startService(new Intent(this, StepService.class));

	}
	public void resetValues(){
	    this.stepCount.setSteps(0);
	}


}

