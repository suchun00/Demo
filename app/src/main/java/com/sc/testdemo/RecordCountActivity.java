package com.sc.testdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.orhanobut.logger.Logger;
import com.sc.adapter.ContentAdapter;
import com.sc.utils.DBUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by suchun on 2017/10/18.
 */
public class RecordCountActivity extends AppCompatActivity {
    List<Map<String, Object>> infolist;
    private List<String> message;
    private List<String> name;
    private ContentAdapter contentAdapter;
    private List<String> valueList;
    DBUtils dbUtils = new DBUtils();
    public String[] arrTemp;

    @BindView(R.id.scan1)
    Button scan1;
    @BindView(R.id.order)
    EditText order;
    @BindView(R.id.spinner1)
    Spinner spinner1;
    @BindView(R.id.spinner2)
    Spinner spinner2;
    @BindView(R.id.query)
    Button query;
    @BindView(R.id.content)
    ListView content;
    @BindView(R.id.upload)
    Button upload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordcount);
        ButterKnife.bind(this);
        initRecord();

    }
    @OnClick({R.id.upload, R.id.query})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.upload :
                getList();
                break;
            case R.id.query :
                loadData();
                break;
        }
    }

    public void initRecord(){
        dbUtils.deleteRecordNum();
    }
    public void loadData() {
//        final ViewHolder viewHoder = new ViewHolder();
        message = new ArrayList<>();
        name = new ArrayList<>();
        message.add(0, "合格品数量");
        message.add(1, "不合格品数量");
        message.add(2, "料废次品数");
        message.add(3, "不合格原因");
        name.add(0, "zhangsan");
        name.add(1, "lisi");
        infolist = this.initData();
        contentAdapter = new ContentAdapter(this, infolist);
        content.setAdapter(contentAdapter);
        contentAdapter.notifyDataSetChanged();
        /*content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Logger.i(((String) infolist.get(2).get("value")));
            }
        });*/
        content.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = ((EditText)view).getText().toString();
                Logger.i(value);
                System.out.println("结果================"+value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public List<Map<String, Object>> initData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 0; i < name.size(); i++) {
            map = new HashMap<>();
            map.put("text", name.get(i));
            map.put("type", 1);
            list.add(map);
            //dbUtils.insertName(name.get(i), null, null);
            for (int j = 0; j < message.size(); j++) {
                map = new HashMap<>();
                if(j == message.size()-1){
                    map.put("text", message.get(j));
                    map.put("type", 3);
                }else {
                    map.put("text", message.get(j));
                    map.put("type", 2);
                }
                list.add(map);
                dbUtils.insertName(name.get(i),  message.get(j));
            }
        }
        return list;
    }
   public void getList(){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        dataList = dbUtils.getNumList();
        for (int i=0; i<dataList.size();i++){
            String staff = dataList.get(i).get("Staff");
            String result = dataList.get(i).get("Result");
            System.out.println("员工====="+staff+"生产数量======="+result);
        }
    }
}
