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
    private String number;
    private String content;
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
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 2143004476)
    public NEWS(Long id, String number, String content, String date) {
        this.id = id;
        this.number = number;
        this.content = content;
        this.date = date;
    }
    @Generated(hash = 1607105437)
    public NEWS() {
    }
}
