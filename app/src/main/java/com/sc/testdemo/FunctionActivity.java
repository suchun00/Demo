package com.sc.testdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sc.utils.MyGridLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suchun on 2017/8/9.
 */
public class FunctionActivity extends AppCompatActivity {
    String titles[] = {"录卡", "领材料", "记录数量", "交接", "巡检"};
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.list)
    MyGridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        ButterKnife.bind(this);
        grid.setGridAdapter(new MyGridLayout.GridAdatper() {
            @Override
            public View getView(final int index) {
                View view = getLayoutInflater().inflate(R.layout.action_item,
                        null);
                TextView tv = (TextView) view.findViewById(R.id.tv);
                tv.setText(titles[index]);
                return view;
            }
            @Override
            public int getCount() {
                return titles.length;
            }
        });
        grid.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int index) {
                switch (index){
                    case 0:
                        Intent intent = new Intent(FunctionActivity.this, ScanActivity.class);
                        startActivity(intent);
                }
            }
        });
    }
}
