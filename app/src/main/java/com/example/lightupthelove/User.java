package com.example.lightupthelove;


import java.io.Serializable;

//    The basic framework of login and registration using database is learned from CSDN website and the book "The First Line of Code".
//    Reference URL:https://blog.csdn.net/qq_42001163/article/details/109207003
//    I borrowed two method classes from the forum: DataHelper and User.

//    Based on the tutorial, I made major modifications.
//    For example, the tutorial uses threads and SharedPreferences for page jumping and data passing,
//    while my method inherits Serializable and Cloneable in the User class to serialize, pass and clone objects

public class User implements Serializable,Cloneable {//Implementing the Serializable interface and implementing cloning by serializing and deserializing objects

    //Basic variables in User class

    //Making attributes private
    private String name;
    private String password;
    private String planname;
    private int planningtype;   //365-day plan or 52-week plan
    private String plannningsituation;//Program status-Whether clocked in or out.Not covered in alpha
    private int color;
    private String startdate;
    private int satrtmoney;
    private int moneyinterval;
    private int isremind;            //提醒
    private String remindtime;        //提醒时间
    private String remindcontent;            //提醒内容
    private int remindsound;        //提醒音效


    //Constructor
    public User(String name, String password, String planname, int planningtype, String plannningsituation, int color,String startdate, int satrtmoney, int moneyinterval,int isremind, String remindtime, String remindcontent, int remindsound) {
        this.name = name;
        this.password = password;
        this.planname = planname;
        this.planningtype=planningtype;
        this.plannningsituation=plannningsituation;
        this.color=color;
        this.startdate=startdate;
        this.satrtmoney=satrtmoney;
        this.moneyinterval=moneyinterval;
        this.isremind=isremind;
        this.remindtime=remindtime;
        this.remindcontent=remindcontent;
        this.remindsound=remindsound;

    }
    //Provide get method to get property values

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPlanname() {
        return planname;
    }

    public int getPlantype() {
        return planningtype;
    }

    public String getPlannningsituation() {
        return plannningsituation;
    }

    public int getColor() {
        return color;
    }

    public String getStartdate() {
        return startdate;
    }

    public int getSatrtmoney() {
        return satrtmoney;
    }

    public int getMoneyinterval() {
        return moneyinterval;
    }

    public int getIsremind() {
        return isremind;
    }

    public String getRemindtime() {
        return remindtime;
    }

    public String getRemindcontent() {
        return remindcontent;
    }

    public int getRemindsound() {
        return remindsound;
    }


    //Provides the set methods for determining the value to be assigned to an attribute

    public void setName(String name) {
        this.name = name;
    }
    public void setPassword() {
        this.password=password;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public void setPlanningtype(int planningtype) {
        this.planningtype = planningtype;
    }

    public void setPlannningsituation(String plannningsituation) {
        this.plannningsituation = plannningsituation;
    }

    public void setColor(int color) {
        this.color=color;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setSatrtmoney(int satrtmoney) {
        this.satrtmoney = satrtmoney;
    }

    public void setMoneyinterval(int moneyinterval) {
        this.moneyinterval = moneyinterval;
    }

    public void setIsremind(int isremind) {
        this.isremind = isremind;
    }

    public void setRemindtime(String remindtime) {
        this.remindtime = remindtime;
    }

    public void setRemindcontent(String remindcontent) {
        this.remindcontent = remindcontent;
    }

    public void setRemindsound(int remindsound) {
        this.remindsound = remindsound;
    }



    //Because the clone method in the Object class is protected,
    // it has to be overridden to be called in the main method.
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    //Override toString method
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

