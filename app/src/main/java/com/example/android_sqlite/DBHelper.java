package com.example.android_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";

    public DBHelper(@Nullable Context context) {
        super(context,"myDB",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG,"----- onCreate database");
        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                +"email text"+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

