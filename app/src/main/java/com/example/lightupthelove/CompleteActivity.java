package com.example.lightupthelove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CompleteActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView planname;
    private TextView totaltarget;
    private Button bt_newplan;
    private User user;


    int plantarget;
    //Page of plan report

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        //Instantiate mDBOpenHelper
        DatabaseHelper mDBOpenHelper = new DatabaseHelper(this);
        Intent intent = this.getIntent();
        //Receive data from Login and Register interfaces
        user=(User)intent.getSerializableExtra("user");
        initView();
    }
    private void initView() {
        // Setting the click event listener
        planname=findViewById(R.id.planname);
        totaltarget = findViewById(R.id.totaltarget);
        bt_newplan=findViewById(R.id.bt_newplan);

        // Setting the click event listener
        bt_newplan.setOnClickListener(this);

        // Initialize fixed data
        if(user.getPlantype()==0){//Calculate total amount according to the plan type
            plantarget=365*user.getSatrtmoney()+365*364*user.getMoneyinterval()/2;
        }
        else{
            plantarget=52*user.getSatrtmoney()+52*51*user.getMoneyinterval()/2;
        }
        //Set text content
        String value=String.valueOf(plantarget);
        planname.setText(user.getPlanname());
        totaltarget.setText(value);


    }
    @Override
    public void onClick(View view) {//User clicks on the button
        Intent intent = new Intent(this,PlanSettingActivity.class);//Jump to plan setting page
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putSerializable("newuser", null);//A new empty user was created for plan setting
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
