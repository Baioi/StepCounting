package edu.buaa.stepcounting.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czn on 11/6/2016.
 */

public class ExerciseRecord {
    private int id;
    private int year;
    private int month;
    private int day;
    private int step;

    public  static  class keys{
        public static String tableName = "exercise";
        public static  String id = "id";
        public static String year = "year";
        public static  String month = "month";
        public static String day = "day";
        public static String step = "step";
    }

    public static List<ExerciseRecord> read(Cursor cursor){
        List<ExerciseRecord> list = new ArrayList<>();
        while (cursor.moveToNext()){
            ExerciseRecord record = new ExerciseRecord();
            record.setId(cursor.getInt(cursor.getColumnIndex(keys.id)));
            record.setYear(cursor.getInt(cursor.getColumnIndex(keys.year)));
            record.setMonth(cursor.getInt(cursor.getColumnIndex(keys.month)));
            record.setDay(cursor.getInt(cursor.getColumnIndex(keys.day)));
            record.setStep(cursor.getInt(cursor.getColumnIndex(keys.step)));
            list.add(record);
        }
        return  list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        //ExerciseRecord.keys keys = ExerciseRecord.keys;
        values.put(ExerciseRecord.keys.id,getId());
        values.put(ExerciseRecord.keys.month,getMonth());
        values.put(ExerciseRecord.keys.year,getYear());
        values.put(ExerciseRecord.keys.day,getDay());
        values.put(ExerciseRecord.keys.step,getStep());
        return  values;
    }


}
