package com.sc.testdemo;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sc.utils.DBUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.bt2)
    Button bt2;
    @BindView(R.id.bt3)
    Button bt3;
    @BindView(R.id.netAddress)
    Button netAddress;
    @BindView(R.id.asset)
    EditText asset;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.scan)
    Button scan;
    @BindView(R.id.order)
    EditText order;
    @BindView(R.id.spinner1)
    Spinner spinner1;
    @BindView(R.id.spinner2)
    Spinner spinner2;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.name)
    EditText name;
    private NotificationManager manager;
    private int messageNotificationID = 1000;
    private Notification notification1;
    private PendingIntent pi;
    private AsyncHttpClient client;
    String workshop;
    String process;
    DBUtils dbUtils = new DBUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        /**
         * 消息推送
         */
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("asset", asset.getText().toString().trim());
                params.put("content", content.getText().toString().trim());
                params.put("number", name.getText().toString().trim());//数据库需重新设计
                //String url = "http://172.23.0.182:8088/upload/abnormal/send.action";
                String url = dbUtils.getNetAddress();
                client.get(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            /*JSONObject obj =null;
                            String jsonData = new String(responseBody, "utf-8");
                            JSONObject result = new JSONObject(jsonData);
                            if(result!=null) {
                                obj = result.getJSONObject("Mess");
                                String date = obj.getString("date");
                                String content = obj.getString("content");
                                String number = obj.getString("number");
                                System.out.println("neirong----" + content);
                                System.out.println("neirong----" + date);
                                System.out.println("neirong----" + number);
                                notification(content, number, date);*/
                            Toast.makeText(getApplicationContext(), "推送成功", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "数据请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        /**
         * 开启服务
         */
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PushSmsService.class);
                //intent.putExtra("number" , et1.getText().toString().trim());
                startService(intent);
            }
        });
        /**
         * 关闭服务
         */
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, PushSmsService.class));
            }
        });
        /**
         * 更改网址
         */
        netAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        ArrayAdapter<CharSequence> workadapter = ArrayAdapter.createFromResource(this,
                R.array.workshop, android.R.layout.simple_spinner_item);
        workadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner1.setAdapter(workadapter);
        spinner1.setOnItemSelectedListener(new spinnerItemSelected());
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                process = spinner2.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void notification(String content, String number, String date) {
        // 获取系统的通知管理器
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder1 = new Notification.Builder(getApplicationContext());
        builder1.setSmallIcon(R.drawable.message);
        builder1.setWhen(System.currentTimeMillis());
        builder1.setContentTitle("通知");
        builder1.setContentText(content);
        builder1.setDefaults(Notification.DEFAULT_ALL);
        builder1.setAutoCancel(true);
        Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("number", number);
        intent.putExtra("date", date);
        pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder1.setContentIntent(pi);
        notification1 = builder1.getNotification();
        manager.notify(messageNotificationID, notification1);
        messageNotificationID++;
    }
    class spinnerItemSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner = ((Spinner) parent);
            String ws = ((String) spinner.getItemAtPosition(position));
            workshop = spinner1.getSelectedItem().toString();
            ArrayAdapter<CharSequence> processadapter = ArrayAdapter.createFromResource(MainActivity.this,
                    R.array.def, android.R.layout.simple_spinner_item);
            processadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //处理车间工序联动显示
            if(ws.equals("冲压车间")) {
                 processadapter = ArrayAdapter.createFromResource(MainActivity.this,
                        R.array.冲压车间, android.R.layout.simple_spinner_item);
                processadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }else if(ws.equals("仪表车间")){
                 processadapter = ArrayAdapter.createFromResource(MainActivity.this,
                        R.array.仪表车间, android.R.layout.simple_spinner_item);
                processadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
            spinner2.setAdapter(processadapter);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

}


