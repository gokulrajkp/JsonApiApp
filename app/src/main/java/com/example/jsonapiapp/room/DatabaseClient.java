package com.example.jsonapiapp.room;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

    //database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx){
        this.mCtx = mCtx;
        //creating app database with room datavase builder
        //all data is in name of database
        appDatabase = Room.databaseBuilder(mCtx,AppDatabase.class,"alldata").build();

    }
    public static synchronized DatabaseClient getInstance(Context mCtx){
        if (mInstance == null){
            mInstance = new DatabaseClient(mCtx);

        }
        return mInstance;
    }
    public AppDatabase getAppDatabase(){
        return appDatabase;
    }
}
