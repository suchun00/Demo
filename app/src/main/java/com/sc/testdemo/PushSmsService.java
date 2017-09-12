package com.sc.testdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sc.utils.DBUtils;
import com.sc.utils.Mess;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by suchun on 2017/7/18.
 */
public class PushSmsService extends Service {
    MyThread myThread;
    Handler m_handler;
    private int messageNotificationID = 1000;
    NotificationManager manager;
    Notification notification1;
    PendingIntent pi;
    private  Mess message;
    private AsyncHttpClient client;
    private boolean flag = true;
    public static final int SET = 1;
    DBUtils dbUtils = new DBUtils();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //number = intent.getStringExtra("number");
        return null;
    }
    @Override
    public void onCreate() {
        System.out.println("oncreate()");
        this.client = new AsyncHttpClient();
        this.myThread = new MyThread();
        this.myThread.start();
        m_handler = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what == 1){
                    funHttp();
                }
            }
        };
        super.onCreate();
    }
    @Override
    public  void onDestroy(){
        System.out.println("Service被销毁");
        flag = false;
        super.onDestroy();
    }
    private class MyThread extends Thread {
       public void run() {
          Looper.prepare();
          while (flag) {
               System.out.println("发送请求");
               try {
                   //Thread.sleep(600000);
                   Thread.sleep(1000*60*10);
                   Message msg = m_handler.obtainMessage();
                   msg.what=SET;
                   m_handler.sendMessage(msg);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
           Looper.loop();
       }}

    public void funHttp() {
        //String url = "http://172.23.0.182:8088/upload/abnormal/send.action";
        String url = dbUtils.getNetAddress();
        RequestParams params = new RequestParams();
        params.put("number" , "123456");
        client.get(url, params,new AsyncHttpResponseHandler(Looper.getMainLooper()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj =null;
                    String jsonData = new String(responseBody, "utf-8");
                    System.out.println("数据----"+jsonData);
                    JSONObject result = new JSONObject(jsonData);
                    if(result!=null) {
                        obj = result.getJSONObject("Mess");
                        String asset = obj.getString("asset");
                        String content = obj.getString("content");
                        String date = obj.getString("date");
                        String number = obj.getString("number");

                        /*if((!content.equals(dbUtils.getLastMessageContent()))||
                                (!number.equals(dbUtils.getLastMessageNumber()))){*/
                        if((!content.equals(dbUtils.getLastMessages().get("content")))
                                ||(!number.equals(dbUtils.getLastMessages().get("number")))
                                ||(dbUtils.getLastMessages()==null)){

                        /*message = GsonUtil.parseJsonWithGson(jsonData,Mess.class);
                        String content = message.getContent();
                        String number = message.getNumber();
                        String date = message.getDate();
                        System.out.println("时间-----"+date);*/
                            notification(asset, content, number, date);
                            dbUtils.insertMessage(content, number, date);
                            System.out.println("11111111111111111");
                        }
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notification(String asset, String content, String number, String date) {
        // 获取系统的通知管理器
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder1  = new Notification.Builder(getApplicationContext());
        builder1 .setSmallIcon(R.drawable.message);
        builder1 .setWhen(System.currentTimeMillis());
        builder1 .setContentTitle("通知");
        builder1 .setContentText(content);
        builder1 .setDefaults(Notification.DEFAULT_ALL);
        builder1 .setAutoCancel(true);
        Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
        intent.putExtra("asset", asset);
        intent.putExtra("content", content);
        intent.putExtra("number", number);
        intent.putExtra("date", date);
        pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder1 .setContentIntent(pi);
        notification1 = builder1.getNotification();
        manager.notify(messageNotificationID, notification1);
        messageNotificationID++;
    }
}
