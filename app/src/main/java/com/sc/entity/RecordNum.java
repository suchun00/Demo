package com.sc.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by suchun on 2017/10/20.
 */
@Entity
public class RecordNum {
    @Id
    private Long id;
    private String staff;
    private String partloc;
    private String result;
    public String getResult() {
        return this.result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getPartloc() {
        return this.partloc;
    }
    public void setPartloc(String partloc) {
        this.partloc = partloc;
    }
    public String getStaff() {
        return this.staff;
    }
    public void setStaff(String staff) {
        this.staff = staff;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 675884586)
    public RecordNum(Long id, String staff, String partloc, String result) {
        this.id = id;
        this.staff = staff;
        this.partloc = partloc;
        this.result = result;
    }
    @Generated(hash = 1976430452)
    public RecordNum() {
    }

}
