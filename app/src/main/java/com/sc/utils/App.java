package com.sc.utils;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.sc.greendao.greendao.gen.DaoMaster;
import com.sc.greendao.greendao.gen.DaoSession;
import com.sc.testdemo.BuildConfig;

/**
 * Created by suchun on 2017/7/24.
 */
public class App extends Application {
    private DaoMaster.DevOpenHelper dbHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    public static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initDatabass();
        if(BuildConfig.DEBUG){
            Logger.init("sunshin").setLogLevel(LogLevel.FULL);
        }else{
            Logger.init("sunshin").setLogLevel(LogLevel.NONE);
        }

    }

    public static App getInstance(){
        return instance;
    }
    private void initDatabass() {
        dbHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = dbHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoSession getSession(){
        return mDaoSession;
    }
    public SQLiteDatabase getDatabase(){
        return db;
    }

    private void init() {
        instance = this;
    }
}
