package com.example.lightupthelove;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import top.defaults.colorpicker.ColorPickerView;

public class PlanSettingActivity extends AppCompatActivity implements View.OnClickListener  {
    private Button bt_plansettingactivity_back;
    private EditText et_plansettingactivity_planname;
    private LinearLayout bt_backgroundcolor;
    private TextView tv_backgroundcolor;
    private Button bt_plansettingactivity_plantype1;
    private Button bt_plansettingactivity_plantype2;
    private EditText et_plansettingactivity_startdate;
    private EditText et_plansettingactivity_satrtmoney;
    private EditText et_plansettingactivity_moneyinterval;
    private Button bt_plansettingactivity_isremind;
    private Button bt_plansettingactivity_complete;
    private static final String SAVED_STATE_KEY_COLOR = "saved_state_key_color";
    private static int INITIAL_COLOR = 0xFFFF8000;


    private DatabaseHelper Database;
    private User user;
    private User newuser;
    private String newplanname;
    private int newcolor;
    private int newplantype;
    private String newstartdate;
    private int newsatrtmoney;
    private int newmoneyinterval;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plansetting);
        Database = new DatabaseHelper(this);
        Intent intent = this.getIntent();
        user=(User)intent.getSerializableExtra("user");
        //Receive user and newuser objects from Login and Register interfaces
        newuser=(User)intent.getSerializableExtra("newuser");
        if(newuser==null){//no data in newuser
            try {//Become a copy of the user object, copying the default properties of user
                newuser=(User)user.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        //Initialising controls
        bt_plansettingactivity_back=findViewById(R.id.bt_plansettingactivity_back);
        et_plansettingactivity_planname = findViewById(R.id.et_plansettingactivity_planname);
        bt_plansettingactivity_plantype1=findViewById(R.id.bt_plansettingactivity_plantype1);
        bt_plansettingactivity_plantype2=findViewById(R.id.bt_plansettingactivity_plantype2);
        bt_backgroundcolor=findViewById(R.id.bt_backgroundcolor);
        tv_backgroundcolor=findViewById(R.id.tv_backgroundcolor);
        et_plansettingactivity_startdate = findViewById(R.id.et_plansettingactivity_startdate);
        et_plansettingactivity_satrtmoney = findViewById(R.id.et_plansettingactivity_satrtmoney);
        et_plansettingactivity_moneyinterval = findViewById(R.id.et_plansettingactivity_moneyinterval);
        bt_plansettingactivity_isremind = findViewById(R.id.bt_plansettingactivity_isremind);
        bt_plansettingactivity_complete = findViewById(R.id.bt_plansettingactivity_complete);
        ColorPickerView colorPickerView=findViewById(R.id.colorPicker);



        // Setting the click event listener
        bt_plansettingactivity_back.setOnClickListener(this);
        bt_plansettingactivity_plantype1.setOnClickListener(this);
        bt_plansettingactivity_plantype2.setOnClickListener(this);
        bt_plansettingactivity_isremind.setOnClickListener(this);
        bt_plansettingactivity_complete.setOnClickListener(this);


        //Set default value of plan name, start date, start money and money interval
        et_plansettingactivity_planname.setText(newuser.getPlanname());
        et_plansettingactivity_startdate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        et_plansettingactivity_satrtmoney.setText(newuser.getSatrtmoney()+"");
        et_plansettingactivity_moneyinterval.setText(newuser.getMoneyinterval()+"");

        if(user.getIsremind()==1){
            bt_plansettingactivity_isremind.setText("Daily Reminder");
        }else{
            bt_plansettingactivity_isremind.setText("No Reminder");
        }


        if(user.getPlantype()==0){
            bt_plansettingactivity_plantype1.setBackgroundResource(R.drawable.shape_buttonselect);
        }
        else{
            bt_plansettingactivity_plantype2.setBackgroundResource(R.drawable.shape_buttonselect);
        }

        /* The colourPicker is a dependency library I added
         * It is implemented in build.gradle
         * URL: https://github.com/duanhong169/ColorPicker
         *
         * The ColorObserver is the project's observer interface
         * and subscribes to colour update events from the ColorPickerView
         */
        colorPickerView.subscribe((color, fromUser) -> {
            bt_backgroundcolor.setBackgroundColor(color);
            tv_backgroundcolor.setText(tv_backgroundcolor(color));//Set background color of demonstration button
            newuser.setColor(color);
            user.setColor(color);

        });
        INITIAL_COLOR=user.getColor();
        int color = INITIAL_COLOR;
        if (savedInstanceState != null) {
            color = savedInstanceState.getInt(SAVED_STATE_KEY_COLOR, INITIAL_COLOR);
        }
        colorPickerView.setInitialColor(color);


    }
    //The color setting method given to help the interface
    private String tv_backgroundcolor(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", a, r, g, b);
    }




    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_plansettingactivity_back://When user clicks on the back button
                if(user.getPlantype()==0) {//According to plan type return to different main pages
                    Intent intent = new Intent(this, MainActivity.class);//Back to main page of 365-day
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                if(user.getPlantype()==1){
                    Intent intent = new Intent(this, MainActivity2.class);//Back to main page of 52-week
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.bt_plansettingactivity_plantype1: //When user selects 365-day plan
                newplantype=0;
                newuser.setPlanningtype(0);//0 means 365-day type
                bt_plansettingactivity_plantype1.setBackgroundResource(R.drawable.shape_buttonselect);//button changes color
                bt_plansettingactivity_plantype2.setBackgroundResource(R.drawable.shape_edittext);//another button back to origin color
                break;
            case R.id.bt_plansettingactivity_plantype2://When user selects 52-week plan
                newplantype=1;
                newuser.setPlanningtype(1);//1 means 52-week type
                bt_plansettingactivity_plantype2.setBackgroundResource(R.drawable.shape_buttonselect);//button changes color
                bt_plansettingactivity_plantype1.setBackgroundResource(R.drawable.shape_edittext);//another button back to origin color
                break;

            case R.id.bt_plansettingactivity_isremind://When user clicks on reminder setting button
                newuser.setPlanname(et_plansettingactivity_planname.getText().toString());
                newuser.setStartdate(newstartdate);//
                newuser.setSatrtmoney(Integer.parseInt(et_plansettingactivity_satrtmoney.getText().toString()));
                newuser.setMoneyinterval(Integer.parseInt(et_plansettingactivity_moneyinterval.getText().toString()));

                Intent intent = new Intent(this, RemindSettingActivity.class);//Jump to remind setting page
                Bundle bundle = new Bundle();
                bundle.putSerializable("newuser", newuser);
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;

            case R.id.bt_plansettingactivity_complete://When user clicks on submit
               if(checkInput()){//Check the format of input date
                final AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Note");
                builder.setMessage("Are you sure you want to clear the original punch record and reset it?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                    }
                });
                builder.setPositiveButton("Yes",
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int which) {//Assign user input to newuser

                                   if(bt_plansettingactivity_plantype2.getBackground().getCurrent().getConstantState()==getResources().getDrawable(R.drawable.shape_buttonselect).getConstantState()){
                                       newuser.setPlanningtype(1);
                                   }else{
                                       newuser.setPlanningtype(0);
                                   }
                                       newuser.setPlanname(et_plansettingactivity_planname.getText().toString());
                                       newuser.setStartdate(newstartdate);
                                       newuser.setSatrtmoney(Integer.parseInt(et_plansettingactivity_satrtmoney.getText().toString()));
                                       newuser.setMoneyinterval(Integer.parseInt(et_plansettingactivity_moneyinterval.getText().toString()));
                                       newuser.setPlannningsituation("");

                                       //If nothing else has changed except the colour and the reminder
                                   if(newuser.getPlanname().equals(user.getPlanname()) &&newuser.getStartdate().equals(user.getStartdate())&&newuser.getPlantype()==user.getPlantype()&&newuser.getSatrtmoney()==user.getSatrtmoney()&&newuser.getMoneyinterval()==user.getMoneyinterval()){
                                       //Punch card records will be kept
                                       try {
                                           newuser=(User)user.clone();
                                       } catch (CloneNotSupportedException e) {
                                           e.printStackTrace();
                                       }
                                       Database.updateColor(newuser.getName(),newuser.getColor());
                                       Database.updateReminder(newuser.getName(),newuser.getIsremind(),newuser.getRemindtime(), newuser.getRemindcontent(),newuser.getRemindsound());
                                       if(newuser.getPlantype()==0) {//User selects 365-day plan
                                           bt_plansettingactivity_plantype1.setBackgroundResource(R.drawable.shape_buttonselect);//button changes color
                                           bt_plansettingactivity_plantype2.setBackgroundResource(R.drawable.shape_edittext);//another button back to origin color
                                           Intent intent = new Intent(PlanSettingActivity.this, MainActivity.class);
                                           Bundle bundle = new Bundle();
                                           bundle.putSerializable("user", user);//Pass the user information to the next activity
                                           intent.putExtras(bundle);
                                           startActivity(intent);
                                       }else{//User selects 52-week plan
                                           bt_plansettingactivity_plantype2.setBackgroundResource(R.drawable.shape_buttonselect);//button changes color
                                           bt_plansettingactivity_plantype1.setBackgroundResource(R.drawable.shape_edittext);//another button back to origin color
                                           Intent intent2 = new Intent(PlanSettingActivity.this, MainActivity2.class);
                                           Bundle bundle2 = new Bundle();
                                           bundle2.putSerializable("user", user);//Pass the user information to the next activity
                                           intent2.putExtras(bundle2);
                                           startActivity(intent2);
                                       }
                                   }
                                   else {
                                       // Add to database
                                       Database.updatePlannning(newuser.getName(), newuser.getPlanname(), newuser.getColor(), newuser.getPlantype(), newuser.getStartdate(), newuser.getSatrtmoney(), newuser.getMoneyinterval());
                                       if (newuser.getPlantype() == 0) {//User selects 365-day plan
                                           Intent intent = new Intent(PlanSettingActivity.this, MainActivity.class);
                                           Bundle bundle = new Bundle();
                                           bundle.putSerializable("user", newuser);//Pass the newuser information to the next activity
                                           intent.putExtras(bundle);
                                           startActivity(intent);
                                       } else {//User selects 52-week plan
                                           Intent intent2 = new Intent(PlanSettingActivity.this, MainActivity2.class);
                                           Bundle bundle2 = new Bundle();
                                           bundle2.putSerializable("user", newuser);//Pass the newuser information to the next activity
                                           intent2.putExtras(bundle2);
                                           startActivity(intent2);
                                       }
                                   }
                                   finish();
                               }
                           });
                   AlertDialog dialog=builder.create();
                dialog.show();

            }
                break;

        }
    }


    //setLeninet method was inspired by a blog from Internet.
    //Reference: https://blog.csdn.net/xuerong_zhu/article/details/103773166
    //This blog is about the specific meaning and usage of setLeninet in JAVA
    public boolean checkInput() {
        Date satredate;
        String startdatestr=et_plansettingactivity_startdate.getText().toString();
        SimpleDateFormat f =  new SimpleDateFormat("yyyy-MM-dd");
        f.setLenient(false);// Throw an exception if the date does not pass
        try {
            satredate=new Date(f.parse(startdatestr).getTime());
        } catch (ParseException e) {
            //error message
            Toast.makeText(PlanSettingActivity.this, "Date："+startdatestr+" does not exist, please enter the start date in the format XXXX XX XX", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
        if(new Date().getTime()-satredate.getTime()>24 * 60 * 60 * 1000){//Algorithm to check if the inout date is earlier than current time.
            //error message
            Toast.makeText(PlanSettingActivity.this, "The start date has passed!", Toast.LENGTH_SHORT).show();
            return false;
        }

        SimpleDateFormat f2 =  new SimpleDateFormat("yyyy-MM-dd");
        newstartdate=f2.format(satredate);

        if(et_plansettingactivity_satrtmoney.getText().toString().equals("")){//If user does not enter the start money
            //error message
            Toast.makeText(PlanSettingActivity.this, "The starting amount cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(Integer.parseInt(et_plansettingactivity_satrtmoney.getText().toString())==0){// If user set the start money as zero.
            //error message
            Toast.makeText(PlanSettingActivity.this, "The starting amount cannot be zero", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(et_plansettingactivity_moneyinterval.getText().toString().equals("")){//If user set the money interval as zero
            //error message
            Toast.makeText(PlanSettingActivity.this, "Amount interval cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        //None of the above, the input format is correct
        return true;
    }


}
