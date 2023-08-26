package com.example.todolist.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todolist.model.ToDo;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase sqLiteDatabase;

    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTask(ToDo toDo) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, toDo.getTaskToDo());
        values.put(COL_3, 0);
        sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    public void updateTask(int id, String task) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, task);
        sqLiteDatabase.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id, int status) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3, status);
        sqLiteDatabase.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
    }

    public List<ToDo> getAllTasks() {

        sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDo> modelList = new ArrayList<>();

        sqLiteDatabase.beginTransaction();
        try {
            cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int taskIdColumnIndex = cursor.getColumnIndex(COL_1);
                int taskToDoColumnIndex = cursor.getColumnIndex(COL_2);
                int taskStatusColumnIndex = cursor.getColumnIndex(COL_3);
                if (taskIdColumnIndex >= 0 && taskToDoColumnIndex >= 0 && taskStatusColumnIndex >= 0) {
                    do {
                        ToDo toDo = new ToDo();
                        toDo.setTaskId(cursor.getInt(taskIdColumnIndex));
                        toDo.setTaskToDo(cursor.getString(taskToDoColumnIndex));
                        toDo.setTaskStatus(cursor.getInt(taskStatusColumnIndex));
                        modelList.add(toDo);

                    } while (cursor.moveToNext());
                } else {
                    Log.i("TAG", "MMM");
                }
            }
        } finally {
            sqLiteDatabase.endTransaction();
            cursor.close();
        }
        return modelList;
    }
}







