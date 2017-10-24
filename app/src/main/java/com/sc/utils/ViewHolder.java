package com.sc.utils;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sc.testdemo.R;

/**
 * Created by suchun on 2017/10/18.
 */
public class ViewHolder {
    public TextView tv;
    public EditText et;

    public ViewHolder(View convertView){
        tv = ((TextView)convertView.findViewById(R.id.tv));
        et = ((EditText) convertView.findViewById(R.id.et));
    }
}
