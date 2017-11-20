package com.sc.testdemo;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sc.utils.DBUtils;
import com.sc.utils.Scan;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

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

    /*private NotificationManager manager;
    private int messageNotificationID = 1000;
    private Notification notification1;
    private PendingIntent pi;*/
    private AsyncHttpClient client;
    private List<Map<String, String>> list;
    String workshop;
    String process;
    String m_strresult = "";
    public Handler mHandler = new MainHandler();
    String jsonString="";
    String rfid;
    String msg;
    Scan scans = new Scan();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();


        /*
        * 扫描批次标签
        * */
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scans.startScan(MainActivity.this, mHandler);
            }
        });
        /**
         * 消息推送
         */
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new AsyncHttpClient();
                list = initData();
                //数据库需重新设计
                String url = DBUtils.getIp()+"MES/mess/receiveMess.action";
                try {
                    Gson gson = new Gson();
                    jsonString = gson.toJson(list);
                    StringEntity stringEntity;
                    stringEntity = new StringEntity(jsonString, "utf-8");
                    client.put(MainActivity.this, url, stringEntity, "application/json;charset=utf-8", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if(responseBody!=null){
                                String result = new String(responseBody);
                                try {
                                    JSONObject obj = new JSONObject(result);
                                    msg = (String) obj.get("msg");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(msg.equals("推送成功"))
                                Toast.makeText(MainActivity.this, "推送成功", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(MainActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                }
                /*client.get(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            *//*JSONObject obj =null;
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
                                notification(content, number, date);*//*
                            Toast.makeText(getApplicationContext(), "推送成功", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "数据请求失败", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });
        /**
         * 开启服务
         */
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PushSmsService.class);
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

    private List<Map<String, String>> initData() {
        List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<>();
        //map.put("plan_no", rfid);
        map.put("plan_no", order.getText().toString().trim());
        map.put("shop_name", workshop);
        map.put("process_name", process);
        map.put("operator", username.getText().toString().trim());
        map.put("asset_no", asset.getText().toString().trim());
        map.put("content", content.getText().toString().trim());
        map.put("sender", name.getText().toString().trim());
        list1.add(map);
        return list1;
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
            if (ws.equals("冲压车间")) {
                processadapter = ArrayAdapter.createFromResource(MainActivity.this,
                        R.array.冲压车间, android.R.layout.simple_spinner_item);
                processadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            } else if (ws.equals("仪表车间")) {
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
    public class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 0) {
                if (m_strresult.indexOf((String) msg.obj) < 0) {
                    m_strresult = "";
                    m_strresult += (String) msg.obj;
                    Log.e("222", m_strresult);
                    rfid = DBUtils.getRfid(m_strresult, "批次标签");
                    order.setText(rfid);
                    Toast.makeText(MainActivity.this, "RFID标签信息获取成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "RFID标签信息获取不成功，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}


