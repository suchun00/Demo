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
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_date)
    EditText etDate;

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
            etNumber.setText(number);
            etDate.setText(date);
        }
    }
}
