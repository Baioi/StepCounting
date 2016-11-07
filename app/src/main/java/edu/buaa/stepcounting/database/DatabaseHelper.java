package edu.buaa.stepcounting.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by czn on 11/6/2016.
 */

public class DatabaseHelper {
    /**/
    public  static final String databaseName = "stepCounting";
    public  static  final int  databaseVersion = 1;

    private DatabaseOpenHelper helper;
    public  DatabaseHelper(Context context){
        helper = new DatabaseOpenHelper(context,databaseName,databaseVersion);
    }

    public  void clear(String tableName){
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(tableName,null,null);
        database.close();
    }

    public void insert(Object entity){
        if(entity instanceof  ExerciseRecord){
            ExerciseRecord record = (ExerciseRecord)entity;
            SQLiteDatabase database = helper.getWritableDatabase();
            ContentValues values = record.getContentValues();
            values.remove(ExerciseRecord.keys.id);
            long r = database.insert(ExerciseRecord.keys.tableName,null,values);
            database.close();
            record.setId((int)r);
           // database.execSQL("insert into "+record.keys.tableName+" ");
        }
    }

    public void update(Object obj){
        if(obj instanceof  ExerciseRecord){
            ExerciseRecord record = (ExerciseRecord)obj;
            SQLiteDatabase database = helper.getWritableDatabase();
            ContentValues values = record.getContentValues();
            int r = database.update(ExerciseRecord.keys.tableName,values,ExerciseRecord.keys.id+"=?",new String[]{String.valueOf(record.getId())});
            database.close();
        }
    }

    public void delete(Object obj){
        if(obj instanceof  ExerciseRecord){
            ExerciseRecord record = (ExerciseRecord)obj;
            SQLiteDatabase database = helper.getWritableDatabase();
            int r = database.delete(ExerciseRecord.keys.tableName,ExerciseRecord.keys.id+"=?",new String[]{String.valueOf(record.getId())});
            database.close();
        }
    }

    public List<?> search(Class<?> c){
        List<?> list = null;
        if(c.getName().contains("ExerciseRecord")){
            SQLiteDatabase database = helper.getReadableDatabase();
            Cursor cursor = database.query(ExerciseRecord.keys.tableName,null,null,null,null,null,null);
            list = ExerciseRecord.read(cursor);
            cursor.close();
            database.close();
        }
        return  list;
    }

    public List<?> search(Class<?> c,String selection[],
                          String[] selectionArgs){
        List<?> list = null;
        if(c.getName().contains("ExerciseRecord")){
            SQLiteDatabase database = helper.getReadableDatabase();
            String selectionStr = "";
            for(int i=0;i < selection.length;i++){
                selectionStr += " "+selection[i]+"=? ";
                if(i != selection.length-1)
                    selectionStr += " and ";
            }
            Cursor cursor = database.query(ExerciseRecord.keys.tableName,null,selectionStr,selectionArgs,null,null,null);
            list = ExerciseRecord.read(cursor);
            cursor.close();
            database.close();
        }
        return  list;
    }

    public void clearTable(Class c){
        if(c.equals(ExerciseRecord.class)){
            SQLiteDatabase database =helper.getWritableDatabase();
            database.execSQL("delete from  "+ExerciseRecord.keys.tableName);
            database.close();

        }
    }
}
