package com.sc.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.uhf.magic.reader;
import android.os.Handler;

import com.sc.testdemo.MainActivity;

/**
 * Created by Administrator on 2017/11/20.
 */
public class Scan {
    public  ProgressDialog progressDialog;
    //public  Handler mainHandler= new MainActivity.MainHandler();
    public  void startScan(Context context, Handler handler){
        progressDialog = ProgressDialog.show(context, "正在扫卡", "请稍后...");
        new Thread(){
            public void run(){
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }finally{
                    progressDialog.dismiss();
                }
            }
        }.start();
        progressDialog.show();
        //scanFlag = true;
//		scanToastFlag=true;
        reader.m_handler = handler;
        reader.InventoryLables();
    }
}
