package edu.buaa.stepcounting;

/**
 * Created by LiangBaoyu on 2016/11/10.
 */

public abstract interface StepValuePassListener {
    public abstract void stepsChanged(int n);
    public abstract void passValue();

}
