package edu.buaa.stepcounting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.buaa.stepcounting.database.DatabaseTester;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DatabaseTester.test(MainActivity.this);
    }
}
