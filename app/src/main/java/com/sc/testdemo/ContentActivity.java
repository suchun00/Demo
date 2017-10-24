package com.sc.testdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suchun on 2017/7/18.
 */
public class ContentActivity extends Activity {

    @BindView(R.id.et_asset)
    EditText etAsset;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.et_order)
    EditText etOrder;
    @BindView(R.id.et_plant)
    EditText etPlant;
    @BindView(R.id.et_process)
    EditText etProcess;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_date1)
    EditText etDate1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            String asset = intent.getStringExtra("asset");
            String content = intent.getStringExtra("content");
            String number = intent.getStringExtra("number");
            String date = intent.getStringExtra("date");
            System.out.println("时间-----" + date);
            etAsset.setText(asset);
            etContent.setText(content);
            etName.setText(number);
            etDate1.setText(date);
        }
    }
}
