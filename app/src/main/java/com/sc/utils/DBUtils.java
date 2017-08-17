package com.sc.utils;

import android.database.Cursor;

import com.sc.entity.IpAddress;
import com.sc.entity.NEWS;
import com.sc.entity.NetAddress;
import com.sc.entity.RfidLabels;
import com.sc.greendao.greendao.gen.IpAddressDao;
import com.sc.greendao.greendao.gen.NEWSDao;
import com.sc.greendao.greendao.gen.NetAddressDao;
import com.sc.greendao.greendao.gen.RfidLabelsDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suchun on 2017/7/24.
 */
public class DBUtils {
    private NetAddressDao netAddressDao;
    private NEWSDao newsDao;
    private IpAddressDao ipAddressDao;
    private RfidLabelsDao rfidLabelsDao;

    public  String getNetAddress(){
        netAddressDao = App.getInstance().getSession().getNetAddressDao();
        List<NetAddress> netList = netAddressDao.loadAll();
        if(netList==null){
          return null;
        }
        NetAddress netAddress = netList.get(netList.size()-1);
        return netAddress.getNetAddress();
    }
    public void update(String address) {
        netAddressDao = App.getInstance().getSession().getNetAddressDao();
        NetAddress netAddress = netAddressDao.load((long) 1);
        if(netAddress!=null){
            netAddress.setNetAddress(address);
            netAddressDao.update(netAddress);
        }
    }
    public void initNet() {
        netAddressDao = App.getInstance().getSession().getNetAddressDao();
        List<NetAddress> netList = netAddressDao.loadAll();
        if(netList==null||netList.size()<1){
            netAddressDao.deleteAll();
            NetAddress netAddress = new NetAddress(null,"NingGuo","http://172.23.0.182:8088/upload/abnormal/send.action");
            netAddressDao.insert(netAddress);
        }
    }
    public void restoreNet() {
        netAddressDao = App.getInstance().getSession().getNetAddressDao();
        NetAddress netAddress = netAddressDao.load((long) 1);
        if (netAddress != null) {
            netAddress.setNetAddress("http://172.23.0.182:8088/upload/abnormal/send.action");
            netAddressDao.update(netAddress);
        }
    }
    public void addIp(String ipName, String ipAddress){
        ipAddressDao = App.getInstance().getSession().getIpAddressDao();
        String net = getNetAddress();
        if(net!=null){
            ipAddressDao.deleteAll();
            IpAddress ip = new IpAddress(null, ipName , ipAddress);
            ipAddressDao.insert(ip);
        }
    }
    public String getIp(){
        ipAddressDao = App.getInstance().getSession().getIpAddressDao();
        String result = null;
        List<IpAddress>ipList = ipAddressDao.loadAll();
        result = ipList.get(0).getIpAddress();
        return result;
    }
    /*public String getLastMessageContent(){
        String content = null;
        String sql = "SELECT CONTENT from NEWS ORDER BY _id DESC limit 1";
        Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
        while (cursor.moveToNext()) {
            content = cursor.getString(0);
        }
        cursor.close();
        return content;
    }
    public String getLastMessageNumber(){
        String number = null;
        String sql = "SELECT NUMBER from NEWS ORDER BY _id DESC limit 1";
        Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
        while (cursor.moveToNext()){
            number = cursor.getString(0);
        }
        return number;
    }*/
    /**
     * 怎么得不到Map集合,判断取得值是否为空
     */
    public Map<String,String> getLastMessages(){
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT CONTENT, NUMBER from NEWS ORDER BY _id DESC limit 1";
        Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
        while (cursor.moveToNext()){
            map.put("content",cursor.getString(0));
            map.put("number", cursor.getString(1));
        }
        cursor.close();
        return map;
    }
    public void insertMessage(String content, String number, String date){
        newsDao = App.getInstance().getSession().getNEWSDao();
        NEWS news = new NEWS(null,number, content, date);
        newsDao.insert(news);
    }
    //判断标签是否唯一
    public String idNull(String phynum){
        String result = null;
        try {
            String sql = "select RFIDID from RFID_LABELS where PHYNUM = '"+phynum+"'";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()){
                result = cursor.getString(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    //录入标签
    public void insertRfid(String phynum,String rfidid, String type, String date){
        rfidLabelsDao = App.getInstance().getSession().getRfidLabelsDao();
        try {
            RfidLabels rfidLabels = new RfidLabels(null, phynum, rfidid, type, date);
            rfidLabelsDao.insert(rfidLabels);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //查询标签序列号
    public List<String> getPhynumList(){
        List<String> phynumList = new ArrayList<String>();
        try {
            String sql = "select PHYNUM from RFID_LABELS order by RFIDID";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()){
                phynumList.add(cursor.getString(0));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return phynumList;
    }
    //查询标签标号
    public List<String> getRfididList(){
        List<String> rfididList = new ArrayList<String>();
        try {
            String sql = "select RFIDID from RFID_LABELS order by RFIDID";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()){
                rfididList.add(cursor.getString(0));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return rfididList;
    }
    //查询标签类型
    public List<String> getTypeList(){
        List<String> typeList = new ArrayList<String>();
        try {
            String sql = "select TYPE from RFID_LABELS order by RFIDID";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()){
                typeList.add(cursor.getString(0));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return typeList;
    }
    //删除一个标签
    public void deleteRfid(String phynum){
        try {
            rfidLabelsDao = App.getInstance().getSession().getRfidLabelsDao();
            RfidLabels rfidLabels = rfidLabelsDao.queryBuilder().where(RfidLabelsDao.Properties.Phynum.eq(phynum)).build().unique();
            if(rfidLabels!=null){
                rfidLabelsDao.deleteByKey(rfidLabels.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //更新标签
    public void updateRfid(String phynum, String rfidid, String type, String date){
        try {
            rfidLabelsDao = App.getInstance().getSession().getRfidLabelsDao();
            RfidLabels rfidLabels = rfidLabelsDao.queryBuilder().where(RfidLabelsDao.Properties.Phynum.eq(phynum)).build().unique();
            if(rfidLabels!=null){
                rfidLabels.setPhynum(phynum);
                rfidLabels.setRfidid(rfidid);
                rfidLabels.setType(type);
                rfidLabels.setDate(date);
                rfidLabelsDao.update(rfidLabels);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //获得当天录的标签
    public List<Map<String,String>> getRfidList(){
        List<Map<String,String>>list = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        try {
            String sql = "select * from RFID_LABELS ";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()) {
                map = new HashMap<>();
                map.put("PHYNUM", cursor.getString(1));
                map.put("RFIDID", cursor.getString(2));
                map.put("TYPE", cursor.getString(3));
                map.put("DATE", cursor.getString(4));
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public List<RfidLabels> getRfidlist(){
        rfidLabelsDao = App.getInstance().getSession().getRfidLabelsDao();
        List<RfidLabels>list =null;
        list = rfidLabelsDao.loadAll();
        return list;
    }
}
