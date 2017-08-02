package com.sc.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by suchun on 2017/7/24.
 */
@Entity
public class NetAddress {
    @Id
    private Long id;
    private String netName;
    private String netAddress;
    public String getNetAddress() {
        return this.netAddress;
    }
    public void setNetAddress(String netAddress) {
        this.netAddress = netAddress;
    }
    public String getNetName() {
        return this.netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1696078706)
    public NetAddress(Long id, String netName, String netAddress) {
        this.id = id;
        this.netName = netName;
        this.netAddress = netAddress;
    }
    @Generated(hash = 528811687)
    public NetAddress() {
    }
}
