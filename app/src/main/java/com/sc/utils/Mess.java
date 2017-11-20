package com.sc.utils;

import java.util.List;

/**
 * Created by suchun on 2017/7/20.
 */
public class Mess {
    private List<Message> message;

    public class Message{
        private String plan_no;
        private String asset_no;
        private String shop_name;
        private String process_name;
        private String operator;
        private String content;
        private String sender;
        private String date;

        public String getPlan_no() {
            return plan_no;
        }

        public void setPlan_no(String plan_no) {
            this.plan_no = plan_no;
        }

        public String getAsset_no() {
            return asset_no;
        }

        public void setAsset_no(String asset_no) {
            this.asset_no = asset_no;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getProcess_name() {
            return process_name;
        }

        public void setProcess_name(String process_name) {
            this.process_name = process_name;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public List<Message> getMessages() {
        return message;
    }

    public void setMessages(List<Message> message) {
        this.message = message;
    }
}

