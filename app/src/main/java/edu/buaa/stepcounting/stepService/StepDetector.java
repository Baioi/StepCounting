package edu.buaa.stepcounting.stepService;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import edu.buaa.stepcounting.Main3Activity;
import edu.buaa.stepcounting.stepService.StepCount;

public class StepDetector implements SensorEventListener{
	private StepCount stepCount;
	private float[] oriValues = new float[3];
	private final int valueNum = 4;

	private float[] tempValue = new float[valueNum];
	private int tempCount = 0;


	private boolean isDirectionUp = false;

	private int continueUpCount = 0;

	private int continueUpFormerCount = 0;

	private boolean lastStatus = false;

	private float peakOfWave = 0;

	private float valleyOfWave = 0;

	private long timeOfThisPeak = 0;

	private long timeOfLastPeak = 0;

	private long timeOfNow = 0;

	private float gravityNew = 0;

	private float gravityOld = 0;

	private final float initialValue = (float) 1.7;

	private float ThreadValue = (float) 2.0;
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
		for(int i = 0; i < 3; i++){
			oriValues[i] = event.values[i];
		}
		gravityNew = (float)Math.sqrt(oriValues[0]*oriValues[0]+oriValues[1]*oriValues[1]
				+oriValues[2]*oriValues[2]);
		DetectorNewStep(gravityNew);
	}
	public void DetectorNewStep(float values){
		Log.d("gravityNew:", gravityNew+"");
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
		gravityOld = values;
	}

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

		if(newValue >= oldValue){
			isDirectionUp = true;
			this.continueUpCount++;
		} else{
			this.continueUpFormerCount = this.continueUpCount;
			continueUpCount = 0;
			isDirectionUp = false;
		}
		
		if(!isDirectionUp && lastStatus && (continueUpFormerCount >= 2
			&&  (oldValue >= 11.9 && oldValue < 40.0)) ){
			this.peakOfWave = oldValue;
			return true;
		} else if(isDirectionUp && !lastStatus) {
			valleyOfWave = oldValue;
			return false;
		} else
			return false;
			
	}
	

}
