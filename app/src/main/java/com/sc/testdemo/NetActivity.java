package com.sc.testdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sc.greendao.greendao.gen.NetAddressDao;
import com.sc.utils.DBUtils;

/**
 * Created by suchun on 2017/7/24.
 */
public class NetActivity extends AppCompatActivity {
    private NetAddressDao netAddressDao;
    private EditText net;
    private Button bt1;
    private Button bt2;
    String address = null;
    DBUtils dbUtils = new DBUtils();

    protected void onResume(){
        super.onResume();
        address = dbUtils.getNetAddress();
        if(address!=null){
            net.setText(address);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        net = (EditText) findViewById(R.id.net);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = net.getText().toString().trim();
                dbUtils.update(address);
                Toast.makeText(getApplicationContext(),"更改网址成功",Toast.LENGTH_SHORT).show();
                NetActivity.this.finish();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUtils.restoreNet();
                Toast.makeText(getApplicationContext(),"恢复网址成功",Toast.LENGTH_SHORT).show();
                NetActivity.this.finish();
            }
        });
    }
}
