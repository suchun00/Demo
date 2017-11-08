package com.sc.testdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sc.utils.MyGridLayout;
import com.sc.utils.Num;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/30.
 */
public class RepairFunctionActivity extends AppCompatActivity {
    String titles[] = {"设备列表", "设备维修状态", "设备模具绑定", "电子标签绑定"};
    @BindView(R.id.list)
    MyGridLayout grid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairfunction);
        ButterKnife.bind(this);
        grid.setGridAdapter(new MyGridLayout.GridAdatper() {
            @Override
            public View getView(final int index) {
                View view = getLayoutInflater().inflate(R.layout.action_item, null);
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
                    case Num.zero:{
                        Intent intent = new Intent(RepairFunctionActivity.this, AssetListActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case Num.first:{
                        Intent intent = new Intent(RepairFunctionActivity.this, AssetRepairActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case Num.second:{
                        Intent intent = new Intent(RepairFunctionActivity.this, AssetMoldActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case Num.third:{
                        Intent intent = new Intent(RepairFunctionActivity.this, ScanAmActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }
}
