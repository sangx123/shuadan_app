package com.xinfu.qianxiaozhuang.api.model;

import java.util.ArrayList;

public class HomeModel {

    private double creditLimit;//信用额度
    private boolean circuit;//是否申请,如果申请了的话就提示剩余多长时间
    private boolean idCard;//身份证认证
    private int creditMin;//最低借款额度
    private int creditMax;// 最搞借款额度
    private boolean onlineStatus;//在线状态
    private int remainDate;//剩余多少天
    private ArrayList<NoticeItemlModel> list;
    private ArrayList<String> loanRecord;


    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }

    public boolean isCircuit() {
        return circuit;
    }

    public void setCircuit(boolean circuit) {
        this.circuit = circuit;
    }

    public boolean isIdCard() {
        return idCard;
    }

    public void setIdCard(boolean idCard) {
        this.idCard = idCard;
    }

    public ArrayList<HomeModel.NoticeItemlModel> getList() {
        return list;
    }

    public void setList(ArrayList<HomeModel.NoticeItemlModel> list) {
        this.list = list;
    }

    public int getCreditMin() {
        return creditMin;
    }

    public void setCreditMin(int creditMin) {
        this.creditMin = creditMin;
    }

    public int getCreditMax() {
        return creditMax;
    }

    public void setCreditMax(int creditMax) {
        this.creditMax = creditMax;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public ArrayList<String> getLoanRecord() {
        return loanRecord;
    }

    public void setLoanRecord(ArrayList<String> loanRecord) {
        this.loanRecord = loanRecord;
    }

    public int getRemainDate() {
        return remainDate;
    }

    public void setRemainDate(int remainDate) {
        this.remainDate = remainDate;
    }

    public class NoticeItemlModel {

        private String title;//标题
        private String description;//描述
        private String link;//网址链接
        private String logo;//图片链接


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }


}
