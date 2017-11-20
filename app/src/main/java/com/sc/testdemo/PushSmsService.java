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
import com.sc.utils.GsonUtil;
import com.sc.utils.Mess;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

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
    private AsyncHttpClient client;
    private boolean flag = true;
    public static final int SET = 1;

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
                   Thread.sleep(1000*10);
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
        String url = DBUtils.getIp()+"MES/mess/getMess.action";
        RequestParams params = new RequestParams();
        params.put("position" , 1);
        client.get(url, params, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray jsonArray;
                    String jsonData = new String(responseBody, "utf-8");
                    System.out.println("数据----"+jsonData);
                    JSONObject result = new JSONObject(jsonData);
                    jsonArray = result.getJSONArray("message");
                    System.out.println("数据++++++"+jsonArray);
                    /*JSONObject obj = jsonArray.getJSONObject(0);
                    String patch = obj.getString("plan_no");

                    System.out.println("数据++++++"+patch);
                    String asset = obj.getString("asset_no");
                    String plant = obj.getString("shop_name");
                    String process = obj.getString("process_name");
                    String username = obj.getString("operator");
                    String content = obj.getString("content");
                    String sender = obj.getString("sender");
                    String date = obj.getString("date");*/
                    //Mess mess = GsonUtil.parseJsonWithGson(jsonArray.toString(), Mess.class);
                    List<Map<String, Object>> mess = GsonUtil.listKeyMaps(jsonArray.toString());
                    String patch = ((String) mess.get(0).get("plan_no"));
                    System.out.println("数据++++++"+patch);
                    String asset = ((String) mess.get(0).get("asset_no"));
                    String plant = ((String) mess.get(0).get("shop_name"));
                    String process = ((String) mess.get(0).get("process_name"));
                    String username = ((String) mess.get(0).get("operator"));
                    String content = ((String) mess.get(0).get("content"));
                    String sender = ((String) mess.get(0).get("sender"));
                    String date = ((String) mess.get(0).get("date"));
                        /*if((!content.equals(dbUtils.getLastMessageContent()))||
                                (!number.equals(dbUtils.getLastMessageNumber()))){*/
                    if((!asset.equals(DBUtils.getLastMessages().get("asset")))
                            || (!content.equals(DBUtils.getLastMessages().get("content")))
                            ||(!sender.equals(DBUtils.getLastMessages().get("sender")))
                            ||(DBUtils.getLastMessages()==null)){
                            notification(patch, asset, plant, process, username, content, sender, date);
                            DBUtils.insertMessage(patch, asset, plant, process, username, content, sender, date);
                            System.out.println("11111111111111111");
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

    private void notification(String patch, String asset, String plant, String process, String username, String content, String sender, String date) {
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
        intent.putExtra("patch", patch);
        intent.putExtra("asset", asset);
        intent.putExtra("plant", plant);
        intent.putExtra("process", process);
        intent.putExtra("username", username);
        intent.putExtra("content", content);
        intent.putExtra("sender", sender);
        intent.putExtra("date", date);
        pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder1 .setContentIntent(pi);
        notification1 = builder1.getNotification();
        manager.notify(messageNotificationID, notification1);
        messageNotificationID++;
    }
}
