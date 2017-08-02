package com.sc.testdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by suchun on 2017/7/18.
 */
public class ContentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Intent intent = getIntent();
        TextView tv_content = (TextView) this.findViewById(R.id.tv_content);
        TextView tv_number = (TextView) this.findViewById(R.id.tv_number);
        TextView tv_date = (TextView) this.findViewById(R.id.tv_date);
        if (intent != null) {
            String content = intent.getStringExtra("content");
            String number = intent.getStringExtra("number");
            String date = intent.getStringExtra("date");
            System.out.println("时间-----"+date);
            tv_content.setText("内容："+content);
            tv_number.setText("号码："+number);
            tv_date.setText("日期："+date);
        }
    }
}
