package com.sc.testdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by suchun on 2017/10/17.
 */
public class LoginActivity extends AppCompatActivity {
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
        ButterKnife.bind(this);
    }

    @OnClick({R.id.net, R.id.upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.net:
                Intent intent = new Intent(LoginActivity.this, NetActivity.class);
                startActivity(intent);
                break;
            case R.id.upload:
                break;
        }
    }
}
