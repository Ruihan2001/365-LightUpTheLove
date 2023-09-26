package com.example.lightupthelove;


import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class RemindSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_remindsettingactivity_back;
    private Button bt_remindsettingactivity_no_remind;
    private Button bt_remindsettingactivity_remind;
    private EditText et_remindsettingactivity_remindtime;
    private EditText et_remindsettingactivity_remindcontent;
    private Button[] remindsoundArr = new Button[9];
    private Button bt_remindsettingactivity_complete;

    private User user;
    private User newuser;
    private int isremind;
    private int remindsound;
    MediaPlayer MediaPlayer;
    AssetManager asset;

    //Page to set reminder information
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remindsetting);
        Intent intent = this.getIntent();
        user=(User)intent.getSerializableExtra("user");
        newuser=(User)intent.getSerializableExtra("newuser");

        MediaPlayer=new MediaPlayer();
        asset = getAssets();
        initView();
    }


    private void initView() {
        // Initialising controls
        bt_remindsettingactivity_back = findViewById(R.id.bt_remindsettingactivity_back);
        bt_remindsettingactivity_no_remind = findViewById(R.id.bt_remindsettingactivity_no_remind);
        bt_remindsettingactivity_remind = findViewById(R.id.bt_remindsettingactivity_remind);
        et_remindsettingactivity_remindtime = findViewById(R.id.et_remindsettingactivity_remindtime);
        et_remindsettingactivity_remindcontent = findViewById(R.id.et_remindsettingactivity_remindcontent);
        remindsoundArr[1] = findViewById(R.id.bt_remindsettingactivity_remindsound1);
        remindsoundArr[2] = findViewById(R.id.bt_remindsettingactivity_remindsound2);
        remindsoundArr[3] = findViewById(R.id.bt_remindsettingactivity_remindsound3);
        remindsoundArr[4] = findViewById(R.id.bt_remindsettingactivity_remindsound4);
        remindsoundArr[5] = findViewById(R.id.bt_remindsettingactivity_remindsound5);
        remindsoundArr[6] = findViewById(R.id.bt_remindsettingactivity_remindsound6);
        remindsoundArr[7] = findViewById(R.id.bt_remindsettingactivity_remindsound7);
        remindsoundArr[8] = findViewById(R.id.bt_remindsettingactivity_remindsound8);
        bt_remindsettingactivity_complete = findViewById(R.id.bt_remindsettingactivity_complete);

        // Setting the click event listener
        bt_remindsettingactivity_back.setOnClickListener(this);
        bt_remindsettingactivity_no_remind.setOnClickListener(this);
        bt_remindsettingactivity_remind.setOnClickListener(this);
        bt_remindsettingactivity_complete.setOnClickListener(this);



        for (int i = 1; i < remindsoundArr.length; i++) {//Set the ID for each beep
            remindsoundArr[i].setId(i);
            remindsoundArr[i].setOnClickListener(new View.OnClickListener() {//Set click event on each sound button
                public void onClick(View v) {
                    final Button b = (Button)v;
                    final int buttonId=b.getId();
                    remindsound=buttonId;
                    remindsoundplayer(remindsound); //Play the sound
                    for (int i = 1; i < remindsoundArr.length; i++) {
                        if (i == buttonId){
                            remindsoundArr[i].setSelected(true);//The current button is selected
                        }
                        else {
                            remindsoundArr[i].setSelected(false);//Other buttons are not selected
                        }
                    }
                }
            });

        }
    if(newuser.getIsremind()==1){ //Switch button status
        bt_remindsettingactivity_remind.setBackgroundResource(R.drawable.selected);
        bt_remindsettingactivity_no_remind.setBackgroundResource(R.drawable.nonseleted);
        isremind=1;
    }else {
        bt_remindsettingactivity_remind.setBackgroundResource(R.drawable.nonseleted);
        bt_remindsettingactivity_no_remind.setBackgroundResource(R.drawable.selected);
        isremind=0;
    }

        changeisremind(isremind);

        et_remindsettingactivity_remindtime.setText(newuser.getRemindtime());
        et_remindsettingactivity_remindcontent.setText(newuser.getRemindcontent());

        //Initialize the user data
        remindsound=newuser.getRemindsound();
        remindsoundArr[remindsound].setSelected(true);

    }


    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_remindsettingactivity_back: //User clicks on back button to back to plan setting page
                Intent intent = new Intent(this, PlanSettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);//remain the user data
                bundle.putSerializable("newuser", newuser);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;

            case R.id.bt_remindsettingactivity_no_remind://User clicks on no remind button
                bt_remindsettingactivity_remind.setBackgroundResource(R.drawable.nonseleted);
                bt_remindsettingactivity_no_remind.setBackgroundResource(R.drawable.selected);
                isremind=0;//0 means no reminder
                changeisremind(isremind);//Set the edit texts below to non-editable
                break;

            case R.id.bt_remindsettingactivity_remind://User clicks on daily remind button
                bt_remindsettingactivity_remind.setBackgroundResource(R.drawable.selected);
                bt_remindsettingactivity_no_remind.setBackgroundResource(R.drawable.nonseleted);
                isremind=1;//1 means daily reminder
                changeisremind(isremind);//Set the edit texts below to editable
                break;

            case R.id.bt_remindsettingactivity_complete://User clicks on submit button to submit input texts
                String remindtime=et_remindsettingactivity_remindtime.getText().toString();
                if(remindtime.split(":|：",-1).length!=2){ //Split the content several times if there are several colons.If the split is not 2, the user has entered the wrong format
                    Toast.makeText(RemindSettingActivity.this, "Wrong time for reminder", Toast.LENGTH_SHORT).show();
                    break;
                }
                int hour=-1;
                int minute=-1;
                try {//Hours and minutes can be converted to int types if the format is correct
                hour=Integer.parseInt(remindtime.split(":|：")[0]);
                minute=Integer.parseInt(remindtime.split(":|：")[1]);
                } catch (NumberFormatException e) {
                    Toast.makeText(RemindSettingActivity.this, "Wrong time for reminder", Toast.LENGTH_SHORT).show();
                }

                if(hour<0||hour>23||minute<0||minute>59){//input time is out of range
                    Toast.makeText(RemindSettingActivity.this, "Wrong time for reminder", Toast.LENGTH_SHORT).show();
                    break;
                }
                //Assigning values to newuser
                newuser.setIsremind(isremind);
                newuser.setRemindtime(et_remindsettingactivity_remindtime.getText().toString());
                newuser.setRemindcontent(et_remindsettingactivity_remindcontent.getText().toString());
                newuser.setRemindsound(remindsound);

                user.setIsremind(isremind);
                user.setRemindtime(et_remindsettingactivity_remindtime.getText().toString());
                user.setRemindcontent(et_remindsettingactivity_remindcontent.getText().toString());
                user.setRemindsound(remindsound);



                intent = new Intent(this, PlanSettingActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putSerializable("newuser", newuser);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

                break;
        }

    }
    public void changeisremind(int isremind) {
        if(isremind==0){ //User selects no reminder
            et_remindsettingactivity_remindtime.setFocusable(false);//User can not set the remind time, content and sound
            et_remindsettingactivity_remindcontent.setFocusable(false);
            for (int i = 1; i < remindsoundArr.length; i++) {
                    remindsoundArr[i].setClickable(false);
            }
        }else if(isremind==1) { //User selects daily reminder
            et_remindsettingactivity_remindtime.setFocusableInTouchMode(true);//User can set the remind time, content and sound
            et_remindsettingactivity_remindcontent.setFocusableInTouchMode(true);

            for (int i = 1; i < remindsoundArr.length; i++) {
                remindsoundArr[i].setClickable(true);
            }
        }
    }

    /*The method to use AssetFileDescriptor to read and play music from the app's raw folder is tutorials I learned from CSDN.
     * I referenced two tutorials.
     * One is an introduction on how to use AssetFileDescription to read data from raw folder.
     * Another one teaches how to play music files from the Assets directory.
     *
     * Reference URL: https://blog.csdn.net/yangqinjiang/article/details/8841390 & https://blog.csdn.net/peachs885090/article/details/82985458
     *
     * I get inspired from these two tutorials and mix them together to implement music playing
     */

    public void remindsoundplayer(int remindsound) {
        MediaPlayer.reset();
        AssetFileDescriptor afd = null;
        try {
            afd = asset.openFd("remindsound"+remindsound+".mp3");//open the file
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            MediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());//set data source for media player
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            MediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer.start();//Play the sound

    }


    @Override
    protected void onDestroy() {
        MediaPlayer.reset();
        super.onDestroy();
    }

}