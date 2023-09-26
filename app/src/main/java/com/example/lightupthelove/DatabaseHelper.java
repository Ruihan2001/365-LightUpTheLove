package com.example.lightupthelove;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private  SQLiteDatabase db;

//    The basic framework of login and registration using database is referenced from CSDN website and the book "The First Line of Code".
//    Reference URL of CSDN website:https://blog.csdn.net/qq_42001163/article/details/109207003
//    I borrowed two method classes from the forum: DataHelper and User.

//    Based on the tutorial, I made major modifications and extensions.
//    For example, the tutorial uses threads and SharedPreferences for page jumping and data passing,
//    while my method inherits Serializable and Cloneable in the User class to serialize, pass and clone objects


    //Statements to create a database
    //Table name is user
    public static final String CREATE_TABLE="CREATE TABLE IF NOT EXISTS user("
            +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            +"name TEXT,"
            +"password TEXT,"
            +"planname TEXT default 'Unname',"
            +"planningtype INT default '0'," // 0 in planningtype presents 365-day plan
            + "plannningsituation TEXT default '',"
            + "color INT default '1',"
            + "startdate TEXT default '2021-10-23',"
            + "satrtmoney INT default '1',"
            + "moneyinterval INT default '1',"
            + "isremind INT default '0',"
            +"remindtime TEXT default '18:00',"
            +"remindcontent TEXT default 'It is your time to save, so come and record your savings',"
            +"remindsound INT default '1')";



    //Constructor for the User class with the path to the package where the class is located
    public DatabaseHelper(Context context) {
        super(context,"UserData",null,1);//Version number from 1
        db = getReadableDatabase();//Set the database to a writable state
    }

    //Override the OnCreate method to execute the table build statement
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    //Override OnUpgrade method to adapt to version
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }
    //The add method inserts data into the table.
    // Inserting name and password data into the table at registration time.
    public void add(String name,String password){
        db.execSQL("INSERT INTO user (name,password,startdate) VALUES(?,?,?)",new Object[]{name,password, new SimpleDateFormat("yyyy-MM-dd").format(new Date())});
    }

    //The updatePlanning method updates the data in the table. After the user has finished setting up the plan,
    // the data in the table about the plan is updated by looking up the row according to the user name
    public void updatePlannning(String name, String planname, int color,int plantype,String startdate, int satrtmoney, int moneyinterval) {
        db.execSQL("UPDATE user SET planname= ? ,color=?,planningtype=?,startdate= ? ,satrtmoney= ? ,moneyinterval= ? ,plannningsituation ='' where name=?",new Object[]{planname,color,plantype,startdate,satrtmoney,moneyinterval,name});
    }

    public void updateColor(String name,  int color) {
        db.execSQL("UPDATE user SET color=? where name=?",new Object[]{color,name});
    }

    public void updateReminder(String name,  int isremind, String remindtime,String remindcontent,int remindsound) {
        db.execSQL("UPDATE user SET isremind=?,remindtime=?,remindcontent=?,remindsound=? where name=?",new Object[]{isremind,remindtime,remindcontent,remindsound,name});
    }

    public void updataPlannningsituation(String name,String plannningsituation){
        db.execSQL("UPDATE user SET plannningsituation = ? where name=?",new Object[]{plannningsituation,name});
    }

    public ArrayList<User> getAllData(){
        ArrayList<User> list = new ArrayList<User>();//The list of the ArrayList class acts as a container for the data
        //Execute a query for all information on the database table named user, as the values are all null
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");//DESCENDING
        while(cursor.moveToNext()){//Traversing the Cursor by moving cursor to the next entry until the latest data
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));//Get all the name value of the name column
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));//Get all the value of the password column
            @SuppressLint("Range") String planname = cursor.getString(cursor.getColumnIndex("planname"));//Get all the value of the planname column
            @SuppressLint("Range") int planningtype = cursor.getInt(cursor.getColumnIndex("planningtype"));//Get all the value of the planningtype column
            @SuppressLint("Range") String plannningsituation = cursor.getString(cursor.getColumnIndex("plannningsituation"));//Get all the value of the planningsituation column
            @SuppressLint("Range") int color = cursor.getInt(cursor.getColumnIndex("color"));
            @SuppressLint("Range") String startdate = cursor.getString(cursor.getColumnIndex("startdate"));//Get all the value of the startname column
            @SuppressLint("Range") int satrtmoney = cursor.getInt(cursor.getColumnIndex("satrtmoney"));//Get all the value of the startmoney column
            @SuppressLint("Range") int moneyinterval = cursor.getInt(cursor.getColumnIndex("moneyinterval"));//Get all the value of the moneyinterval column
            @SuppressLint("Range") int isremind = cursor.getInt(cursor.getColumnIndex("isremind"));
            @SuppressLint("Range") String remindtime = cursor.getString(cursor.getColumnIndex("remindtime"));
            @SuppressLint("Range") String remindcontent = cursor.getString(cursor.getColumnIndex("remindcontent"));
            @SuppressLint("Range") int remindsound = cursor.getInt(cursor.getColumnIndex("remindsound"));
            //For each query performed, all results constitute all data for one user. Packing a user's data into a list.
            list.add(new User(name,password,planname,planningtype,plannningsituation,color,startdate,satrtmoney,moneyinterval,isremind,remindtime,remindcontent,remindsound));
        }
        return list;
    }
}
