package edu.buaa.stepcounting;

public class StepCount {
	private int mCount = 0;
	private int count = 0;
	long TimeofThisPeak = System.currentTimeMillis();
	long TimeofLastPeak = System.currentTimeMillis();
	public void setSteps(int s){
		this.mCount = s;
	}
	public void countStep(long t){
		this.TimeofLastPeak = this.TimeofThisPeak;
		this.TimeofThisPeak = t;
		if(this.count == 10){
			count++;
			mCount += count;
		}
		else if(this.count < 10){
			if(TimeofThisPeak - TimeofLastPeak >= 3000)
				count = 1;
			count++;
		}
		else
			mCount++;
	}
}
