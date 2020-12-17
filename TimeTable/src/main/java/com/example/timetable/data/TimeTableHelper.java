package com.example.timetable.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class TimeTableHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER="create table USER("+
            "user_id text primary key,"+
            "phone_number text,"+
            "user_name text,"+
            "gender blob,"+
            "user_password text,"+
            "register_time timestamp,"+
            "profile_picture longblob)";
    private Context mContext;
    public TimeTableHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        Toast.makeText(mContext,"Creat succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists USER");
        onCreate(db);
    }
}
