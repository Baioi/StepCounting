package edu.buaa.stepcounting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.buaa.stepcounting.stepService.MyApp;
import edu.buaa.stepcounting.stepService.StepService;

public class Main3Activity extends AppCompatActivity {
    private TextView textView;
    private int steps;
    private MyHandler myHandler = new MyHandler();
    private Intent serviceIntent;
    MyApp myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("hello", "Activity onCreate: ");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        textView = (TextView)findViewById(R.id.textView);
        serviceIntent = new Intent(Main3Activity.this, StepService.class);
        myApp = (MyApp)getApplication();
        myApp.setMyHandler(myHandler);
        startService(serviceIntent);
    }
    public class MyHandler extends  Handler{
        @Override
        public void handleMessage(Message msg){
            Log.d("MyHandler","message!");
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            steps = bundle.getInt("steps");
            textView.setText(steps + "");
        }
    }

}
