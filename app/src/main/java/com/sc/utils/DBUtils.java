package com.sc.utils;

import android.database.Cursor;

import com.sc.entity.IpAddress;
import com.sc.entity.NEWS;
import com.sc.entity.NetAddress;
import com.sc.greendao.greendao.gen.IpAddressDao;
import com.sc.greendao.greendao.gen.NEWSDao;
import com.sc.greendao.greendao.gen.NetAddressDao;

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
}
