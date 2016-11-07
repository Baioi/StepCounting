package edu.buaa.stepcounting.database;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by czn on 11/6/2016.
 */

public class DatabaseTester {
    /**/
    private static Context context;
    private static DatabaseHelper helper;
    public  static  void  test(Context context){
        DatabaseTester.context = context;
        context.deleteDatabase(DatabaseHelper.databaseName);
        helper = new DatabaseHelper(context);
//        String s = " s  d ";
//        String ss[] = s.split(" ");
//        for(String s1 : ss){
//            System.out.println("["+s1+"]");
//        }
       // Log.i("sssssssss","3333333333");
        testInsert();
        testUpdate();
        testdelete();
        testSearch();
        //Log.i("sssssssss","3333333333");
    }

    private static void testInsert(){
        System.out.println("testInsert");
        helper.clear(ExerciseRecord.keys.tableName);
        ExerciseRecord record = new ExerciseRecord();
        record.setDay(2222);
        helper.insert(record);
        helper.insert(record);
        List<ExerciseRecord> list = (List<ExerciseRecord>)helper.search(ExerciseRecord.class);
        if(list.size() != 2){
            new Exception().printStackTrace();
            System.exit(-1);
        }
        if(list.get(1).getId() != 2){
            System.out.println("err id:"+list.get(1).getId());
            new Exception().printStackTrace();
            System.exit(-1);
        }
        helper.clearTable(ExerciseRecord.class);
        list = (List<ExerciseRecord>)helper.search(ExerciseRecord.class);
        if(list.size() !=0){
            new Exception().printStackTrace();
            System.exit(-1);
        }
    }

    private static  void  testUpdate(){
        System.out.println("test update");
        helper.clear(ExerciseRecord.keys.tableName);
        ExerciseRecord record = new ExerciseRecord();
        record.setDay(2222);
        helper.insert(record);
        record.setDay(333);
        helper.update(record);
        List<ExerciseRecord> list = (List<ExerciseRecord>) helper.search(ExerciseRecord.class);
        if(list.get(0).getDay() != 333){
            new Exception().printStackTrace();
            System.exit(-1);
        }
    }

    private static void  testdelete(){
        System.out.println("test delete");
        helper.clear(ExerciseRecord.keys.tableName);
        ExerciseRecord record = new ExerciseRecord();
        record.setDay(2222);
        helper.insert(record);
        helper.delete(record);
        List<ExerciseRecord> list = (List<ExerciseRecord>) helper.search(ExerciseRecord.class);
        if(list.size() != 0){
            new Exception().printStackTrace();
            System.exit(-1);
        }
    }

    private static void  testSearch(){
       System.out.println("test search");
        helper.clear(ExerciseRecord.keys.tableName);
        ExerciseRecord record = new ExerciseRecord();
        record.setDay(2222);
        helper.insert(record);
        record.setDay(333);
        helper.update(record);
        List<ExerciseRecord> list = (List<ExerciseRecord>)helper.search(ExerciseRecord.class,new String[]{ExerciseRecord.keys.day}
        ,new String[]{String.valueOf(record.getDay())}
        );
        if(list.size() != 1){
            new Exception().printStackTrace();
            System.exit(-1);
        }
    }
}
