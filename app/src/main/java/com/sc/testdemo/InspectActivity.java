package com.sc.testdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suchun on 2017/10/16.
 */
public class InspectActivity extends AppCompatActivity {
    @BindView(R.id.scan1)
    Button scan1;
    @BindView(R.id.order)
    EditText order;
    @BindView(R.id.spinner1)
    Spinner spinner1;
    @BindView(R.id.spinner2)
    Spinner spinner2;
    @BindView(R.id.checkcount)
    EditText checkcount;
    @BindView(R.id.qualified)
    EditText qualified;
    @BindView(R.id.count)
    EditText count;
    @BindView(R.id.status)
    EditText status;
    @BindView(R.id.checkname)
    EditText checkname;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.upload)
    Button upload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect1);
        ButterKnife.bind(this);

    }
}
