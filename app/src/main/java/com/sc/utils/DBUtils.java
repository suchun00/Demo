package com.sc.utils;

import android.database.Cursor;

import com.sc.entity.IpAddress;
import com.sc.entity.NEWS;
import com.sc.entity.NetAddress;
import com.sc.entity.RecordNum;
import com.sc.entity.RfidLabels;
import com.sc.greendao.greendao.gen.IpAddressDao;
import com.sc.greendao.greendao.gen.NEWSDao;
import com.sc.greendao.greendao.gen.NetAddressDao;
import com.sc.greendao.greendao.gen.RecordNumDao;
import com.sc.greendao.greendao.gen.RfidLabelsDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suchun on 2017/7/24.
 */
public class DBUtils {
    private static NetAddressDao netAddressDao;
    private static NEWSDao newsDao;
    private static IpAddressDao ipAddressDao;
    private static RfidLabelsDao rfidLabelsDao;
    //private RecordCountDao recordCountDao;
    private static RecordNumDao recordNumDao;

    public static String getNetAddress(){
        netAddressDao = App.getInstance().getSession().getNetAddressDao();
        List<NetAddress> netList = netAddressDao.loadAll();
        if(netList==null){
          return null;
        }
        NetAddress netAddress = netList.get(netList.size()-1);
        return netAddress.getNetAddress();
    }
    public static void update(String address) {
        netAddressDao = App.getInstance().getSession().getNetAddressDao();
        NetAddress netAddress = netAddressDao.load((long) 1);
        if(netAddress!=null){
            netAddress.setNetAddress(address);
            netAddressDao.update(netAddress);
        }
    }
    public static void initNet() {
        netAddressDao = App.getInstance().getSession().getNetAddressDao();
        ipAddressDao = App.getInstance().getSession().getIpAddressDao();
        List<NetAddress> netList = netAddressDao.loadAll();
        if(netList==null||netList.size()<1){
            netAddressDao.deleteAll();
            NetAddress netAddress = new NetAddress(null,"NingGuo","http://172.23.12.225:8080/MES/login/userLogin.action");
            netAddressDao.insert(netAddress);
            IpAddress ip = new IpAddress(null, "ningguo" , "http://172.23.12.225:8080/");
            ipAddressDao.insert(ip);
        }
    }
    public static void restoreNet() {
        netAddressDao = App.getInstance().getSession().getNetAddressDao();
        NetAddress netAddress = netAddressDao.load((long) 1);
        if (netAddress != null) {
            netAddress.setNetAddress("http://172.23.0.182:8088/upload/abnormal/send.action");
            netAddressDao.update(netAddress);
        }
    }
    public static void addIp(String ipName, String ipAddress){
        ipAddressDao = App.getInstance().getSession().getIpAddressDao();
        //List<IpAddress>ipList = ipAddressDao.loadAll();
        IpAddress ip = ipAddressDao.load((long)1);
        if(ip!=null){
            ip.setIpAddress(ipAddress);
            ipAddressDao.update(ip);
        }else {
            ip = new IpAddress(null, ipName , ipAddress);
            ipAddressDao.insert(ip);
        }
    }
    public static String getIp(){
        ipAddressDao = App.getInstance().getSession().getIpAddressDao();
        String result;
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
    public static Map<String,String> getLastMessages(){
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT ASSET, CONTENT, SENDER from NEWS ORDER BY _id DESC limit 1";
        Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
        while (cursor.moveToNext()){
            map.put("asset",cursor.getString(0));
            map.put("content",cursor.getString(1));
            map.put("sender", cursor.getString(2));
        }
        cursor.close();
        return map;
    }
    public static void insertMessage(String patch, String asset, String plant, String process,
                                     String username, String content, String sender, String date){
        newsDao = App.getInstance().getSession().getNEWSDao();
        NEWS news = new NEWS(null, patch, asset, plant, process, username, content, sender, date);
        newsDao.insert(news);
    }
    //插入员工生产数量(旧)
   /* public void insertName(String staff, String qualified, String unqualified, String material){
        recordCountDao = App.getInstance().getSession().getRecordCountDao();
        RecordCount recordCount = new RecordCount(null, staff, qualified, unqualified, material);
        recordCountDao.insert(recordCount);
    }*/
    //记录员工生产数量
    public static void insertName(String staff, String partloc){
        recordNumDao = App.getInstance().getSession().getRecordNumDao();
        RecordNum recordNum = new RecordNum(null, staff, partloc, null);
        recordNumDao.insert(recordNum);
    }
    //判断标签是否唯一
    public static String idNull(String phynum){
        String result = null;
        try {
            String sql = "select RFIDID from RFID_LABELS where PHYNUM = '"+phynum+"'";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()){
                result = cursor.getString(0);
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    //录入标签
    public static void insertRfid(String phynum,String rfidid, String type, String date, String flag){
        rfidLabelsDao = App.getInstance().getSession().getRfidLabelsDao();
        try {
            RfidLabels rfidLabels = new RfidLabels(null, phynum, rfidid, type, date, flag);
            rfidLabelsDao.insert(rfidLabels);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //查询当天没有上传的标签序列号
    public static List<String> getPhynumList(String date){
        List<String> phynumList = new ArrayList<String>();
        try {
            //String sql = "select PHYNUM from RFID_LABELS order by RFIDID ";
            String sql = "select PHYNUM from RFID_LABELS where date = '"+date+"' and flag = '0' ";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()){
                phynumList.add(cursor.getString(0));
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return phynumList;
    }
    //查询当天没有上传的标签标号
    public static List<String> getRfididList(String date){
        List<String> rfididList = new ArrayList<String>();
        try {
            //String sql = "select RFIDID from RFID_LABELS order by RFIDID";
            String sql = "select RFIDID from RFID_LABELS where date = '"+date+"' and flag = '0'";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()){
                rfididList.add(cursor.getString(0));
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return rfididList;
    }
    //查询当天没有上传的标签类型
    public static List<String> getTypeList(String date){
        List<String> typeList = new ArrayList<String>();
        try {
            //String sql = "select TYPE from RFID_LABELS order by RFIDID";
            String sql = "select TYPE from RFID_LABELS where date = '"+date+"' and flag = '0'";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()){
                typeList.add(cursor.getString(0));
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return typeList;
    }
    //获取某个标签对应的编号

    public static String getRfid(String phynum, String type){
        String rfid = null;
        String sql = "select RFIDID from RFID_LABELS where PHYNUM = '"+phynum+"'and TYPE = '"+type+"'";
        Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()){
            rfid = cursor.getString(0);
        }
        return rfid;
    }
    //删除一个标签
    public static void deleteRfid(String phynum){
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
    //删除员工生产表
    public static void deleteRecordNum(){
        try {
            recordNumDao = App.getInstance().getSession().getRecordNumDao();
            List<RecordNum> recordNumList = recordNumDao.loadAll();
            if(recordNumList!=null){
                recordNumDao.deleteAll();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //更新标签
    public static void updateRfid(String phynum, String rfidid, String type, String date){
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
    //更新标签的标记位
    public static void updateFlag(List<Map<String, String>> list){
        rfidLabelsDao = App.getInstance().getSession().getRfidLabelsDao();
        for(int i=0; i<list.size(); i++){
            String phynum = list.get(i).get("phynum");
            RfidLabels rfidLabels = rfidLabelsDao.queryBuilder().where(RfidLabelsDao.Properties.Phynum.eq(phynum)).build().unique();
            if(rfidLabels!=null){
                rfidLabels.setFlag("1");
                rfidLabelsDao.update(rfidLabels);
            }
        }
    }
    //更新员工产品数量
    public static void updateCount(String staff, String partloc, String result){
        try {
            recordNumDao = App.getInstance().getSession().getRecordNumDao();
            RecordNum recordNum = recordNumDao.queryBuilder()
                                        .where(RecordNumDao.Properties.Staff.eq(staff),
                                        RecordNumDao.Properties.Partloc.eq(partloc)).build().unique();
            if(recordNum!=null){
                recordNum.setStaff(staff);
                recordNum.setPartloc(partloc);
                recordNum.setResult(result);
                recordNumDao.update(recordNum);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //获得当天录的标签
    public static List<Map<String,String>> getRfidList(String date){
        List<Map<String,String>>list = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        try {
            String sql = "select PHYNUM, RFIDID, TYPE from RFID_LABELS where date = '"+date+"' and flag = '0'";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()) {
                map = new HashMap<>();
                map.put("phynum", cursor.getString(0));
                map.put("rfidid", cursor.getString(1));
                map.put("type", cursor.getString(2));
                list.add(map);
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    //获得员工生产数量
    public static List<Map<String,String>> getNumList(){
        List<Map<String,String>>list = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        try {
            String sql = "select * from RECORD_NUM ";
            Cursor cursor = App.getInstance().getSession().getDatabase().rawQuery(sql,null);
            while (cursor.moveToNext()) {
                map = new HashMap<>();
                map.put("Staff", cursor.getString(1));
                map.put("Partloc", cursor.getString(2));
                map.put("Result", cursor.getString(3));
                list.add(map);
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
