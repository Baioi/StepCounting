package edu.buaa.stepcounting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by czn on 11/6/2016.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public DatabaseOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("create table");
        db.execSQL("create table exercise(" +
                "id INTEGER  PRIMARY KEY ," +
                " year int," +
                " month int," +
                " day int,"+
                " hour int,"+
                " step int" +
                " "+
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("up");
    }
}
