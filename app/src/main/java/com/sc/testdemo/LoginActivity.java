package com.sc.testdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.uhf.magic.reader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sc.utils.DBUtils;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by suchun on 2017/10/17.
 */
public class LoginActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private long exitTime = 0;
    String userName;
    String passWord;
    String msg;

    @BindView(R.id.net)
    Button net;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.upload)
    Button upload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Init();
        ButterKnife.bind(this);
        DBUtils.initNet();
    }



    @OnClick({R.id.net, R.id.upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.net:
                Intent intent = new Intent(LoginActivity.this, NetActivity.class);
                startActivity(intent);
                break;
            case R.id.upload:
                upload();
                break;
        }
    }
    public void upload(){
        progressDialog = ProgressDialog.show(LoginActivity.this, "登录中", "请耐心等待....");
        new Thread(){
            public void run(){
                try{
                    Thread.sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    progressDialog.dismiss();
                }
            }
        }.start();
        progressDialog.show();
        userName = username.getText().toString().trim();
        passWord = password.getText().toString().trim();
        try{
            String url = DBUtils.getNetAddress();
            //String url = "http://172.23.12.38:8080/MES/login/userLogin.action";
            RequestParams params = new RequestParams();
            params.put("username", userName);
            params.put("password",passWord);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, new AsyncHttpResponseHandler() {
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
                        if(msg.equals("工段长")){
                            Intent intent = new Intent(LoginActivity.this, FunctionActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "工段长登录成功", Toast.LENGTH_SHORT).show();
                        }else if(msg.equals("维修员")){
                            Intent intent = new Intent(LoginActivity.this, RepairFunctionActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "维修员登录成功", Toast.LENGTH_SHORT).show();
                        }else if(msg.equals("统计员")){
                            Intent intent = new Intent(LoginActivity.this, OutSourceActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "统计员登录成功", Toast.LENGTH_SHORT).show();
                        }else if(msg.equals("检验员")){
                            Intent intent = new Intent(LoginActivity.this, InspectActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "检验员登录成功", Toast.LENGTH_SHORT).show();
                        }else if(msg.equals("该用户无访问权限")){
                            Toast.makeText(LoginActivity.this, "该用户无访问权限", Toast.LENGTH_SHORT).show();
                        }else if(msg.equals("密码错误")){
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"网络有问题",Toast.LENGTH_SHORT).show();
                }
             });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    void Init() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                reader.init("/dev/ttyMT1");
                reader.Open("/dev/ttyMT1");
                Log.e("777777777777","11111111111111111111111111");
                if(reader.SetTransmissionPower(2100)!=0x11) {
                    if(reader.SetTransmissionPower(2100)!=0x11) {
                        reader.SetTransmissionPower(2100);
                    }
                }
            }
        });
        thread.start();
    }
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
