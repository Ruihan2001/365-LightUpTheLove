package com.example.lightupthelove;

import static com.example.lightupthelove.R.drawable.heart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_miactivity_background2;
    private Button bt_miactivity_planSetting2;
    private TextView tv_miactivity_planname2;
    private TextView tv_miactivity_startdate2;
    private Button bt_miactivity_complete2;
    private TextView tv_miactivity_enddate2;
    private TextView tv_miactivity_plantarget2;
    private Button bt_miactivity_download2;
    private GridLayout GridLayout_miactivity_heart2;
    private ImageView iv_miactivity_finalheart2;
    private DatabaseHelper Database;
    private ProgressBar pb_miactivity_plannningsituation2;
    private TextView tv_miactivity_plannningsituation3;
    private TextView tv_miactivity_plannningsituation4;
    private Button bt_miactivity_share2;
    private String choosePath2 = "";
    private String savePath2 = "";

    Button[] heartArr = new Button[54];
    Button[] heartBtn = new Button[2];
    int[] heartCompleteArr = new int[54];//used to store punch status. 0 means not punched; Non-zero value for punch amount
    int plantarget;
    private User user;
    public Handler mHandler = new Handler();
    Runnable r;
    Time t = new Time(); // or Time t
    public int year, month, date, hour, minute, second;

    public static final int REQUEST_CHOOSE_SAVE_PATH = 1000;
    //MainPage of 52-week plan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Database = new DatabaseHelper(this);
        Intent intent = this.getIntent();
        //Receive data from Login and Register interfaces
        user = (User) intent.getSerializableExtra("user");
        initView2();
        initHeart2();//Draw default heart
        updateProgress();//Update progress bar
        startremind(user.getIsremind());// Set up daily system notifications
    }

    private void initView2() {
        // Setting the click event listener
        ll_miactivity_background2 = findViewById(R.id.ll_miactivity_background2);
        bt_miactivity_planSetting2 = findViewById(R.id.bt_miactivity_planSetting2);
        bt_miactivity_complete2 = findViewById(R.id.bt_miactivity_complete2);
        tv_miactivity_planname2 = findViewById(R.id.tv_miactivity_planname2);
        tv_miactivity_startdate2 = findViewById(R.id.tv_miactivity_startdate2);
        tv_miactivity_enddate2 = findViewById(R.id.tv_miactivity_enddate2);
        bt_miactivity_download2 = findViewById(R.id.bt_miactivity_download2);
        tv_miactivity_plantarget2 = findViewById(R.id.tv_miactivity_plantarget2);
        GridLayout_miactivity_heart2 = findViewById(R.id.GridLayout_miactivity_heart2);
        iv_miactivity_finalheart2 = findViewById(R.id.iv_miactivity_finalheart2);
        pb_miactivity_plannningsituation2 = findViewById(R.id.pb_miactivity_plannningsituation2);
        tv_miactivity_plannningsituation3 = findViewById(R.id.tv_miactivity_plannningsituation3);
        tv_miactivity_plannningsituation4 = findViewById(R.id.tv_miactivity_plannningsituation4);
        bt_miactivity_share2 = findViewById(R.id.bt_miactivity_share2);

        // Setting the click event listener
        bt_miactivity_planSetting2.setOnClickListener(this);
        bt_miactivity_complete2.setOnClickListener(this);
        bt_miactivity_download2.setOnClickListener(this);
        bt_miactivity_share2.setOnClickListener(this);

        // Initialize fixed data
        tv_miactivity_planname2.setText(user.getPlanname());
        tv_miactivity_startdate2.setText("Start Time  " + user.getStartdate());

        //Set format for time
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        // Set default start time
        try {
            cal.setTime(new Date(f.parse(user.getStartdate()).getTime()));//Translate String type to date type
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Add 365 days
        cal.add(Calendar.DATE, 364);//Plan includes the day
        tv_miactivity_enddate2.setText("End Time  " + f.format(cal.getTime()));//Translate date type to String type
        plantarget = 52 * user.getSatrtmoney() + 52 * 51 * user.getMoneyinterval() / 2;//Algorithm of calculating total amount
        tv_miactivity_plantarget2.setText("Target  ￡" + plantarget);

        if (user.getColor() == 1) {
        } else {
            ll_miactivity_background2.setBackgroundColor(user.getColor());
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_miactivity_planSetting2:// When user clicks on setting button
                Intent intent = new Intent(this, PlanSettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putSerializable("newuser", null);//A new empty user was created for plan setting
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_miactivity_complete2:
                Intent intent2 = new Intent(this, CompleteActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("user", user);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                finish();
                break;
            case R.id.bt_miactivity_download2:
                chooseSavePath();
                break;
            case R.id.bt_miactivity_share2:
                sharePhoto();
                break;
        }
    }
    private void sharePhoto() {
        Uri uri;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setAction(Intent.ACTION_SEND);
        try {
            uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(), savePath2, new File(savePath2).getName(), null));
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(Intent.createChooser(intent, "Share screenshot"));
    }

    private void showDialog(View v) { //Locate each clicked cell, set the click event and dialog

        final Button b = (Button) v;
        String buttonText = b.getText().toString();
        final int buttonId = b.getId();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Punch amount: ￡" + buttonText);
        if (heartCompleteArr[buttonId] == 0) {//When clicking on the grid is unpunched status
            builder.setMessage("Are you sure about clocking in?");
        } else {//When clicking on a grid it is clocked in
            builder.setMessage("Are you sure you want to cancel your punch card?");
        }
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {//Set the cancel button
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {//Set the submit button
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //Details of the update plan
                        GradientDrawable drawable = new GradientDrawable();
                        drawable.setShape(GradientDrawable.RECTANGLE); //Set style of grids in heart area
                        drawable.setStroke(1, Color.BLACK);
                        if (heartCompleteArr[buttonId] == 0) {//The currently selected grid is not clocked in
                            drawable.setColor(Color.rgb(255, 26, 81));//Change the background colour to indicate that the card is punched
                            heartCompleteArr[buttonId] = Integer.parseInt(b.getText().toString());//Replace the 0 in the array with the corresponding punch card amount
                        } else {//The currently selected grid is already clocked in
                            drawable.setColor(Color.WHITE); //Change the background colour to indicate that the grid has returned to an unpunched state
                            heartCompleteArr[buttonId] = 0;//Replacing the values in the array with 0
                        }

                        heartArr[buttonId].setBackground(drawable);
                        Database.updataPlannningsituation(user.getName(), heartCompleteArrToString(heartCompleteArr));
                        //Convert the heartCompleteArr array(punchcards for all grids) to a String via the method, and add this String message to the database
                        user.setPlannningsituation(heartCompleteArrToString(heartCompleteArr));
                        updateProgress();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    //The method is to convert the heartCompleteArr array to a String data so that it can be added to the database as a single data
    private String heartCompleteArrToString(int[] heartCompleteArr) {
        StringBuffer heartStringBuffer = new StringBuffer();//Because the string is modified, the StringBuffer is used here.
        for (int i : heartCompleteArr) {// Iterate through the heartCompleteArr array, using the object i to receive each iteration.
            heartStringBuffer.append(i + ",");//The elements of the array are separated by commas and added to the StringBuffer as a whole
        }
        heartStringBuffer.deleteCharAt(heartStringBuffer.length() - 1);//Delete the comma after the last element
        return heartStringBuffer.toString();
    }

    //Draw the heart of 52-day and initialise it
    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceType"})
    private void initHeart2() {
        if (user.getPlannningsituation() == "") {//Data is empty, indicating no card has been punched
            for (int i = 0; i < heartCompleteArr.length; i++)
                heartCompleteArr[i] = 0;//Therefore setting all elements in the array are 0

        } else {
            String[] temArr = user.getPlannningsituation().split(",");//because of the heartCompleteArrToString method, the arrays are separated by commas like "xx,tt,dd" Extract them out
            for (int i = 0; i < heartCompleteArr.length; i++)
                heartCompleteArr[i] = Integer.parseInt(temArr[i]);//Returns the equivalent integer value to the number in the string
        }
        int heartCell = 1;//Specific number of grids in heart area

        for (int i = 1; i <= 8; i++) {//The area occupied is a rectangle of 8*9
            for (int j = 1; j <= 9; j++) {

                Button myButton2 = new Button(MainActivity2.this);
                //Set style of grids in heart area
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(1, Color.BLACK);
                //Set different colours for punched and unpunched squares
                if (heartCompleteArr[heartCell] == 0) {
                    drawable.setColor(Color.WHITE);
                } else {
                    drawable.setColor(Color.rgb(255, 26, 81));
                }

                myButton2.setBackground(drawable);
                //The width and height of the heart area is 0 when the heart drawing algorithm is not implemented
                myButton2.setMinWidth(0);
                myButton2.setMinHeight(0);
                myButton2.setMinimumWidth(0);
                myButton2.setMinimumHeight(0);
                myButton2.setWidth(80);
                myButton2.setHeight(80);
                myButton2.setPadding(0, 0, 0, 0);
                myButton2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                myButton2.setTextSize(15);
                //Each grid in heart area has its ID
                //Prepare for implementing punching card function in Beta.
                myButton2.setId(heartCell);

                //Each grid is a square of width and height 1
                GridLayout.Spec rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.UNDEFINED);
                GridLayout.Spec columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.UNDEFINED);

                //Algorithm for drawing 52-week heart
                if (i == 1) {//For each column of the first row, draw a grid by situation
                    if (j == 1) {
                        //Indicates a starting position of 1, occupying 1 column
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.UNDEFINED);
                        myButton2.setBackground(null);
                        j = 1;//Start from j=1 later
                    }
                    if (j == 4) {
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 3, GridLayout.UNDEFINED);
                        myButton2.setBackground(null);
                        j = 6;
                    }

                    if (j == 9) {
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.UNDEFINED);
                        myButton2.setBackground(null);
                        j = 9;
                    }
                }

                if ((i == 2) && (j == 5)) {//Blank grid in row 2
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.UNDEFINED);
                    myButton2.setBackground(null);
                }
                if ((i >= 6) && (i <= 7)) {//Draw blank grids in row 6&7
                    if (j == 1) {
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, i - 5, GridLayout.UNDEFINED);
                        myButton2.setBackground(null);
                        j = i - 5;
                    }
                    if (j == 15 - i) {
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, i - 5, GridLayout.UNDEFINED);
                        myButton2.setBackground(null);
                        j = 9;
                    }
                }
                if (i == 8) {
                    if ((j == 1)) {
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 3, GridLayout.UNDEFINED);
                        myButton2.setBackground(null);
                        j = 3;
                    }
                    if ((j == 4)) {//One placeholder heart in row 8
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.UNDEFINED);
                        myButton2.setBackground(getDrawable(R.drawable.heartbutton));
                        heartBtn[0] = myButton2;
                        j = 4;
                    }
                    if ((j == 6)) {//Another placeholder heart in row 8
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.UNDEFINED);
                        myButton2.setBackground(getDrawable(R.drawable.heartbutton));
                        heartBtn[1] = myButton2;
                        //myButton2.setBackgroundColor(Color.BLACK);
                        j = 6;
                    }
                    if ((j == 7)) {
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 3, GridLayout.UNDEFINED);
                        myButton2.setBackground(null);
                        j = 9;
                    }

                }

                if (myButton2.getBackground() != null && myButton2.getBackground().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.heartbutton).getConstantState() && myButton2.getBackground().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.heartbuttonfull).getConstantState()) {
                    myButton2.setText(String.valueOf(user.getSatrtmoney() + (heartCell - 1) * user.getMoneyinterval()));//Algorithm for calculating punch card amount of each day
                    myButton2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            showDialog(v);
                        }
                    });
                    heartArr[heartCell] = myButton2;
                    heartCell++;
                }

                GridLayout.LayoutParams params2 = new GridLayout.LayoutParams(rowSpec, columnSpec);
                GridLayout_miactivity_heart2.addView(myButton2, params2);//Add the whole grid to grid layout to display it


            }
        }

    }


    private void updateProgress() {
        int progress = 0;
        for (int i : heartCompleteArr) {//The heartComleteArr array is traversed, with each traversed object being received by the object i.
            progress += i;//Calculate the amount of punched cards
        }
        double percentage = progress * 100.0 / plantarget; //Calculation of percentage completion
        @SuppressLint("DefaultLocale") String result = String.format("%.2f", percentage);//Retain two decimal places
        pb_miactivity_plannningsituation2.setProgress((int) percentage);
        tv_miactivity_plannningsituation3.setText("Completed: " + result + "%");
        tv_miactivity_plannningsituation4.setText("￡" + progress + "/" + "￡" + plantarget);
        if (progress == plantarget) {//The heart changes colour after completing the full clocking amount
            iv_miactivity_finalheart2.setBackgroundResource(R.drawable.heartfull);
            heartBtn[0].setBackgroundResource(R.drawable.heartbuttonfull);
            heartBtn[1].setBackgroundResource(R.drawable.heartbuttonfull);
            bt_miactivity_complete2.setVisibility(View.VISIBLE);//The report button appears to click
        } else {
            iv_miactivity_finalheart2.setBackgroundResource(R.drawable.heart);
            heartBtn[0].setBackgroundResource(R.drawable.heartbutton);
            heartBtn[1].setBackgroundResource(R.drawable.heartbutton);
            bt_miactivity_complete2.setVisibility(View.INVISIBLE);//The button is not visible if you have not completed all your punches
        }

        //Synchronise widget updates when data is changed in the application interface

        //Some relative knowledge was inspired from tutorials.
        //On the base of tutorials, I set the widget according to my own application.
        //Reference URL: https://blog.csdn.net/weixin_43871500/article/details/101376841 (The use of ComponentName in Android)
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(),NewAppWidget.class);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.new_app_widget);
        remoteViews.setTextViewText(R.id.widget_plantype,"52-week plan");
        remoteViews.setTextViewText(R.id.widget_planname,user.getPlanname());
        remoteViews.setTextViewText(R.id.widget_startdate,user.getStartdate());

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        // Set default start time
        try {
            cal.setTime(new Date(f.parse(user.getStartdate()).getTime()));//Translate String type to date type
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DATE, 364);//Plan includes the day
        remoteViews.setTextViewText(R.id.widget_enddate,f.format(cal.getTime()));
        remoteViews.setProgressBar(R.id.pb_miactivity_plannningsituation,plantarget,progress,false);
        remoteViews.setTextViewText(R.id.tv_miactivity_plannningsituation1,"Completed: " + result + "%");
        remoteViews.setTextViewText(R.id.tv_miactivity_plannningsituation2,"￡" + progress + "/" + "￡" + plantarget);
        manager.updateAppWidget(componentName,remoteViews);

    }
    private void chooseSavePath() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        builder.setTitle("Tip:");
        builder.setMessage("Please select a save directory");
        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent(MainActivity2.this, DirectoryPathChooseActivity.class);
            startActivityForResult(intent, REQUEST_CHOOSE_SAVE_PATH);
        });
        builder.create();
        builder.show();
    }

    //The scrennshot() and saveToLocal() methods are quoted from a CSDN tutorial
    //Reference URL: https://blog.csdn.net/weixin_38380115/article/details/104162182
    private void screenshot() {
        View dView = getWindow().getDecorView();// Get the top view of the screen
        dView.setDrawingCacheEnabled(true);//Create a bitmap
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null) {
            saveToLocal(bmp, choosePath2);
            //Toast.makeText(MainActivity2.this, "截图成功！", Toast.LENGTH_SHORT).show();
//            saveToLocal(bmp);//Call the method to save the screenshot locally
        }
        dView.setDrawingCacheEnabled(false);
    }

    //Save the screenshot locally
    public void saveToLocal(Bitmap bitmap, String choosePath2) {

        //Set the file name to the current time
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String sd = sdf.format(new Date(timeStamp));
        String fileName = sd + ".jpg";

        //Get the file
        File file = new File(choosePath2, fileName);
        savePath2 = file.getAbsolutePath();
        System.out.printf("\n\nFile path:" + choosePath2);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //Notification system album refresh
            MainActivity2.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            Toast.makeText(MainActivity2.this, "Image saved to album", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //This method is to set up daily system notifications
    private void startremind(int isremind) {
        if (isremind == 0) return;
        final int remindhour = Integer.parseInt(user.getRemindtime().split(":|：")[0]);//Extracting the hour from reminder time
        final int remindminute = Integer.parseInt(user.getRemindtime().split(":|：")[1]);//Extracting the minute from reminder time

        r = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                t.setToNow();// Get system time
                hour = t.hour;
                minute = t.minute;
                second = t.second;

                //If the current time has reached the user-set reminder time
                if (hour == remindhour && minute == remindminute && second == 0) {

                    /*The knowledge to use notification is learnt from a CSDN tutorial
                     * Reference URL: https://blog.csdn.net/daniel80110_1020/article/details/53087162
                     * The tutorial describes the basic usage of Notification and NotificationManager
                     */
                    NotificationManager notificationManager = (NotificationManager) MainActivity2.this.getSystemService(NOTIFICATION_SERVICE);
                    Notification.Builder builder1 = new Notification.Builder(MainActivity2.this);
                    builder1.setSmallIcon(R.drawable.logo2); //Set icon
                    builder1.setContentTitle(user.getPlanname()); //Set content title
                    builder1.setContentText(user.getRemindcontent());  //Set content text
                    //Since the current time is the time set by the user to send the message, as specified in the if statement, the time to send the message is set to now
                    builder1.setWhen(System.currentTimeMillis()); //Get the current system time to send the message
                    builder1.setSound(Uri.parse("android.resource://com.example.lightupthelove/raw/remindsound" + user.getRemindsound()));
                    builder1.setAutoCancel(true);//Icons disappear after opening a program
                    // Intent intent2 =new Intent (MainActivity.this,LoginActivity.class);
                    //  PendingIntent pendingIntent =PendingIntent.getActivity(MainActivity.this, 0, intent2, 0);
                    // builder1.setContentIntent(pendingIntent);
                    Notification notification1 = builder1.build();
                    notificationManager.notify(1, notification1); // Send notifications via Notification Manager
                }
                mHandler.postDelayed(this, 1000);
            }
        };

        mHandler.post(r);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CHOOSE_SAVE_PATH) {
            if (resultCode == RESULT_OK && data != null) {
                choosePath2 = data.getStringExtra("CHOOSE_PATH");
                if (!choosePath2.isEmpty()) {
                    screenshot();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(r);
        super.onDestroy();
    }

}
