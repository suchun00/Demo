package com.sc.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by suchun on 2017/8/10.
 */
@Entity
public class IpAddress {
    @Id
    private Long id;
    private String ipName;
    private String ipAddress;
    public String getIpAddress() {
        return this.ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public String getIpName() {
        return this.ipName;
    }
    public void setIpName(String ipName) {
        this.ipName = ipName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 2018488328)
    public IpAddress(Long id, String ipName, String ipAddress) {
        this.id = id;
        this.ipName = ipName;
        this.ipAddress = ipAddress;
    }
    @Generated(hash = 179288771)
    public IpAddress() {
    }
}
