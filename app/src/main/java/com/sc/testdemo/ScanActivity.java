package com.sc.testdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.hardware.uhf.magic.reader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orhanobut.logger.Logger;
import com.sc.entity.RfidLabels;
import com.sc.utils.DBUtils;
import com.sc.utils.GsonUtil;
import com.sc.utils.Rfidlabel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by suchun on 2017/8/10.
 */
public class ScanActivity extends AppCompatActivity {
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.phynum)
    TextView phynum;
    @BindView(R.id.rfidid)
    EditText rfidid;
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    @BindView(R.id.partlist)
    ListView partlist;
    private List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    private Handler mHandler = new MainHandler();
    SimpleAdapter simpleAdapter;
    String m_strresult = "";
    String liststream="";
    String jsonString="";
    String type;
    String date;
    String msg;
    String flag = "0";
    DateFormat df = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        df = DateFormat.getDateInstance();
        date = df.format(new Date());
        ArrayAdapter<CharSequence> adapterLabel = ArrayAdapter.createFromResource(this,
                R.array.labellist,
                android.R.layout.simple_spinner_item);
        adapterLabel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapterLabel);
        reader.m_handler = mHandler;
        this.getList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reader.StopLoop();
    }

    @OnClick({R.id.btn_scan, R.id.btn_upload, R.id.btn_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                reader.InventoryLables();
                break;
            case R.id.btn_upload:
                upLoad();
                break;
            case R.id.btn_record:
                record();
                break;
        }
    }

    private void upLoad() {
        JSONObject jsonObject = new JSONObject();
        final List<Map<String, String>>list1;
       //List<RfidLabels> list1;

        List<Rfidlabel> rfidlabel;
        list1 = DBUtils.getRfidList(date);
        String type1 = list1.get(0).get("type");
        System.out.println("标签信息------" + type1);
        Logger.i(type1);
        if (list1.size() == 0) {
            Toast.makeText(ScanActivity.this, "请先扫描录入数据", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Gson gson = new Gson();
                jsonString = gson.toJson(list1);
                /*rfidlabel = GsonUtil.parseJsonArrayWithGson(jsonString, Rfidlabel.class);
                System.out.println(rfidlabel);
                liststream = rfidlabel.toString();*/
                //liststream = jsonObject.put("label",  rfidlabel.toString()).toString();
                Logger.json(jsonString);
                Logger.i(jsonString);
            }catch (Exception e){
                e.printStackTrace();
            }
            AsyncHttpClient client = new AsyncHttpClient();
            String url = DBUtils.getIp()+"MES/function/scanLabel.action";
            Logger.i(url);
            StringEntity stringEntity;
            try {
                stringEntity = new StringEntity(jsonString,"utf-8");
                client.post(ScanActivity.this, url, stringEntity, "application/json;charset=utf-8", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if(responseBody!=null){
                            String result = new String(responseBody);
                            try {
                                JSONObject obj = new JSONObject(result);
                                msg = (String) obj.get("msg");
                                if(msg.equals("录入成功")){
                                    Toast.makeText(ScanActivity.this,"上传成功", Toast.LENGTH_SHORT).show();
                                    DBUtils.updateFlag(list1);
                                    clearList();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(ScanActivity.this,"上传失败！超时或者网络不稳定", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void clearList() {
        ScanActivity.this.phynum.setText("");
        ScanActivity.this.rfidid.setText("");
        list.clear();
        this.simpleAdapter = new SimpleAdapter(this, this.list, R.layout.partdata_list,
                new String[]{"phynum","rfidid"}, new int[]{R.id.phynum1 , R.id.rfidid1});
        partlist.setAdapter(this.simpleAdapter);
    }

    private void record() {
        /*df = DateFormat.getDateInstance();
        date = df.format(new Date());*/
        if (("".equals(phynum.getText().toString().trim())) || ("".equals(rfidid.getText().toString().trim()))) {
            Toast.makeText(ScanActivity.this, "请先扫描标签或输入编号", Toast.LENGTH_SHORT).show();
        } else {
            type = spinner.getSelectedItem().toString();
            if ("订单标签".equals(type)) {
                type = "订单标签";
            } else if ("人员标签".equals(type)) {
                type = "人员标签";
            }
            if(DBUtils.idNull(phynum.getText().toString().trim()) == null){
                DBUtils.insertRfid(phynum.getText().toString().trim(), rfidid.getText().toString().trim(), type, date, flag);
                getList();
                Toast.makeText(ScanActivity.this, "录入标签信息成功", Toast.LENGTH_SHORT).show();
            }else {
                Dialog dialog = new AlertDialog.Builder(ScanActivity.this)
                        .setTitle("确认替换？")
                        .setMessage("该部位已录入标签，确定替换吗？")
                        .setPositiveButton("替换", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBUtils.updateRfid(phynum.getText().toString().trim(), rfidid.getText().toString().trim(), type, date);
                                getList();
                                Toast.makeText(ScanActivity.this,"标签替换成功!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                             @Override
                            public void onClick(DialogInterface dialog, int which) {

                             }
                        }).create();
                dialog.show();
            }
        }
    }
    public void getList(){
        list.clear();
        List<String> rfididList = DBUtils.getRfididList(date);
        List<String> phynumList = DBUtils.getPhynumList(date);
        List<String> typelist = DBUtils.getTypeList(date);
        for(int i = 0; i< phynumList.size(); i++){
            if(!(phynumList.get(i) == null || phynumList.get(i).length() == 0)){
                String Drfidid = rfididList.get(i);
                String Dphynum = phynumList.get(i);
                String Dtype = typelist.get(i);
                Map<String, String> map = new HashMap<>();
                map.put("phynum", Dphynum);
                map.put("rfidid", Drfidid + "-" + Dtype);
                list.add(map);
            }
        }
        this.simpleAdapter = new SimpleAdapter(this, this.list, R.layout.partdata_list,
                new String[]{"phynum","rfidid"}, new int[]{R.id.phynum1 , R.id.rfidid1});
        partlist.setAdapter(this.simpleAdapter);
        this.partlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map1 = (Map<String, String>) ScanActivity.this.simpleAdapter.getItem(position);
                final String Phynum = map1.get("phynum");
                Dialog dialog = new AlertDialog.Builder(ScanActivity.this)
                        .setTitle("确认删除？")
                        .setMessage("该部位已录入RFID标签，确定删除吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBUtils.deleteRfid(Phynum);
                                Toast.makeText(ScanActivity.this,"删除标签信息成功", Toast.LENGTH_SHORT).show();
                                getList();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
                return false;
            }
        });
    }

    /*void Init() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                reader.init("/dev/ttyMT1");
                reader.Open("/dev/ttyMT1");
                Log.e("7777777777", "111111111111111111111111111111111111");
                if (reader.SetTransmissionPower(2100) != 0x11) {
                    if (reader.SetTransmissionPower(2100) != 0x11) {
                        reader.SetTransmissionPower(2100);
                    }
                }
            }
        });
        thread.start();
    }*/

    public class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 0) {
                if (m_strresult.indexOf((String) msg.obj) < 0) {
                    m_strresult = "";
                    m_strresult += (String) msg.obj;
                    Log.e("222", m_strresult);
                    ScanActivity.this.phynum.setText(m_strresult);
                    Toast.makeText(ScanActivity.this, "RFID标签信息获取成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScanActivity.this, "RFID标签信息获取不成功，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
