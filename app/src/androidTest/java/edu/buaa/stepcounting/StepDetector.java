package com.stepcounting;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class StepDetector implements SensorEventListener{
	private StepCount stepCount;
	float[] oriValues = new float[3];  
	final int valueNum = 4;  
	//用于存放计算阈值的波峰波谷差值  
	float[] tempValue = new float[valueNum];  
	int tempCount = 0;  
	
	//是否上升的标志位  
	boolean isDirectionUp = false;  
	//持续上升次数  
	int continueUpCount = 0;  
	//上一点的持续上升的次数，为了记录波峰的上升次数  
	int continueUpFormerCount = 0;  
	//上一点的状态，上升还是下降  
	boolean lastStatus = false;  
	//波峰值  
	float peakOfWave = 0;  
	//波谷值  
	float valleyOfWave = 0;  
	//此次波峰的时间  
	long timeOfThisPeak = 0;  
	//上次波峰的时间  
	long timeOfLastPeak = 0;  
	//当前的时间  
	long timeOfNow = 0;  
	//当前传感器的值  
	float gravityNew = 0;  
	//上次传感器的值  
	float gravityOld = 0;  
	//动态阈值需要动态的数据，这个值用于这些动态数据的阈值  
	final float initialValue = (float) 1.3;  
	//初始阈值  
	float ThreadValue = (float) 2.0;  
	public StepDetector(StepCount sc){
		this.stepCount = sc;
		for(int i = 0; i < tempValue.length; i++)
			tempValue[i] = 0;
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		for(int i = 0; i < 3; i++){
			oriValues[i] = event.values[i];
		}
		gravityNew = (float)Math.sqrt(oriValues[0]*oriValues[0]+oriValues[1]*oriValues[1]
				+oriValues[2]*oriValues[2]);
		DetectorNewStep(gravityNew);		
	}
	public void DetectorNewStep(float values){
		if(gravityOld == 0)
			gravityOld = values;
		else{
			if(DetectorPeak(values, gravityOld)){
				timeOfLastPeak = timeOfThisPeak;
				timeOfNow = System.currentTimeMillis();
				if (timeOfNow - timeOfLastPeak >= 250  
                        && (peakOfWave - valleyOfWave >= ThreadValue)) {  
                    timeOfThisPeak = timeOfNow; 
                    stepCount.countStep(timeOfThisPeak);
				}
				if (timeOfNow - timeOfLastPeak >= 250  
                        && (peakOfWave - valleyOfWave >= initialValue)) {  
                    timeOfThisPeak = timeOfNow;  
                    ThreadValue = Peak_Valley_Thread(peakOfWave - valleyOfWave);  
                }  
			}
		}
		
	}
	 /*

	* 阈值的计算

	* 1.通过波峰波谷的差值计算阈值

	* 2.记录4个值，存入tempValue[]数组中

	* 3.在将数组传入函数averageValue中计算阈值

	* */ 
	private float Peak_Valley_Thread(float value) {
		// TODO Auto-generated method stub
		float tempThread = ThreadValue;  
	    if (tempCount < valueNum) {  
	        tempValue[tempCount] = value;  
	        tempCount++;  
	    } else {  
	        tempThread = averageValue(tempValue, valueNum);  
	        for (int i = 1; i < valueNum; i++) {  
	            tempValue[i - 1] = tempValue[i];  
	        }  
	        tempValue[valueNum - 1] = value;  
	    }  
	    return tempThread;  
	}
	private float averageValue(float[] value, int n) {
		// TODO Auto-generated method stub
		float ave = 0;  
        for (int i = 0; i < n; i++) {  
            ave += value[i];  
        }  
        ave = ave / valueNum; 
        if (ave >= 8)
        	ave = (float) 4.3;
        else if (ave >= 7 && ave < 8)
        	ave = (float) 3.3;
       	else if (ave >= 4 && ave < 7)
       		ave = (float) 2.3;
        else if (ave >= 3 && ave < 4)
        	ave = (float) 2.0;
       	else {
        	ave = (float) 1.3;
       	} 
		return ave;
	}
	private boolean DetectorPeak(float newValue, float oldValue) {
		// TODO Auto-generated method stub
		lastStatus = isDirectionUp;
		this.continueUpCount++;
		if(newValue > oldValue){
			isDirectionUp = true;
			return true;
		} else{
			this.continueUpFormerCount = this.continueUpCount;
			continueUpCount = 0;
			isDirectionUp = false;
		}
		
		if(!isDirectionUp && lastStatus && (continueUpFormerCount >= 2 || oldValue >= 20)) {
			this.peakOfWave = oldValue;
			return true;
		} else if(isDirectionUp && !lastStatus) {
			valleyOfWave = oldValue;
			return false;
		} else
			return false;
			
	}
	

}
