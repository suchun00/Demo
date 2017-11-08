package com.sc.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by suchun on 2017/8/14.
 */
@Entity
public class RfidLabels {
    @Id
    private Long id;
    private String phynum;
    private String rfidid;
    private String type;
    private String date;
    private String flag;
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getRfidid() {
        return this.rfidid;
    }
    public void setRfidid(String rfidid) {
        this.rfidid = rfidid;
    }
    public String getPhynum() {
        return this.phynum;
    }
    public void setPhynum(String phynum) {
        this.phynum = phynum;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFlag() {
        return this.flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    @Generated(hash = 1692540163)
    public RfidLabels(Long id, String phynum, String rfidid, String type,
            String date, String flag) {
        this.id = id;
        this.phynum = phynum;
        this.rfidid = rfidid;
        this.type = type;
        this.date = date;
        this.flag = flag;
    }
    @Generated(hash = 942618534)
    public RfidLabels() {
    }
}
