package edu.buaa.stepcounting.stepService;

import android.app.Application;

import edu.buaa.stepcounting.Main3Activity;

/**
 * Created by Administrator on 2016/11/13.
 */

public class MyApp extends Application {
    private Main3Activity.MyHandler myHandler = null;
    public void setMyHandler(Main3Activity.MyHandler handler){
        myHandler = handler;
    }
    public Main3Activity.MyHandler getMyHandler(){
        return this.myHandler;
    }
}
