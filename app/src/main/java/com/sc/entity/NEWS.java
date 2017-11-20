package com.sc.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by suchun on 2017/7/25.
 */
@Entity
public class NEWS {
    @Id
    private Long id;
    private String patch;
    private String asset;
    private String plant;
    private String process;
    private String username;
    private String content;
    private String sender;
    private String date;
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getProcess() {
        return this.process;
    }
    public void setProcess(String process) {
        this.process = process;
    }
    public String getPlant() {
        return this.plant;
    }
    public void setPlant(String plant) {
        this.plant = plant;
    }
    public String getAsset() {
        return this.asset;
    }
    public void setAsset(String asset) {
        this.asset = asset;
    }
    public String getPatch() {
        return this.patch;
    }
    public void setPatch(String patch) {
        this.patch = patch;
    }
    public String getSender() {
        return this.sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    @Generated(hash = 356067905)
    public NEWS(Long id, String patch, String asset, String plant, String process,
            String username, String content, String sender, String date) {
        this.id = id;
        this.patch = patch;
        this.asset = asset;
        this.plant = plant;
        this.process = process;
        this.username = username;
        this.content = content;
        this.sender = sender;
        this.date = date;
    }
    @Generated(hash = 1607105437)
    public NEWS() {
    }
}
