package com.example.lightupthelove;

import android.Manifest;
import android.appwidget.AppWidgetProvider;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper Database;
    private Button mTvLoginactivityRegister;
    private EditText mEtLoginactivityUsername;
    private EditText mEtLoginactivityPassword;
    private Button mBtLoginactivityLogin;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkPermission();

        // Initialising controls
        mBtLoginactivityLogin = findViewById(R.id.bt_loginactivity_login);
        mTvLoginactivityRegister = findViewById(R.id.bt_loginactivity_register);
        mEtLoginactivityUsername = findViewById(R.id.et_loginactivity_username);
        mEtLoginactivityPassword = findViewById(R.id.et_loginactivity_password);

        //Instantiate mDBOpenHelper
        Database = new DatabaseHelper(this);

        // Setting the click event listener
        mBtLoginactivityLogin.setOnClickListener(this);
        mTvLoginactivityRegister.setOnClickListener(this);

    }

    //The code to get read and write access to Android external storage is referenced in a tutorial
    //Reference URL:https://www.cnblogs.com/zanzg/p/9129375.html
    public void checkPermission() {
        int targetSdkVersion = 0;
        String[] PermissionString={
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
        try {
            final PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                boolean isAllGranted = checkPermissionAllGranted(PermissionString);
                if (isAllGranted) {
                    return;
                }
                ActivityCompat.requestPermissions(this,
                        PermissionString, 1);
            }
        }
    }

    //Permission dynamic permission acquisition is leaned from a tutorial
    //Reference URL:https://blog.csdn.net/generallizhong/article/details/99716283

    //Check if you have all the permissions specified
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            boolean isAllGranted = true;
            // Determine if all permissions have been granted
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {

            } else {
               //Not fully authorised

            }
        }
    }


    //The verification of the content at login registration,
    // e.g. whether the password entered twice is the same and whether the information is filled in fully was inspired by the CSDN website,
    // Reference URL:https://blog.csdn.net/qq_42001163/article/details/109207003

    // I have improved it.
    // In my app, all the data is traversed and the logic in the login is followed to find the row of data corresponding to the entered username and the username in the table,
    // compare the passwords and then go to the main screen.
    // The tutorial uses threads and SharedPreferences for page jumping and data passing,
    // while my method inherits Serializable and Cloneable in the User class to serialize, pass and clone objects
    @Override
    public void onClick(View view) {
        //The login page has two buttons: login and register.
        // Use switch-case to match the code executed in different cases
        switch (view.getId()) {
            case (R.id.bt_loginactivity_login)://When user clicks on login button
                String name = mEtLoginactivityUsername.getText().toString();//Get the username in the input box and convert it to String type
                String password = mEtLoginactivityPassword.getText().toString();//Get the username in the input box and convert it to String type
                if (name.equals("") | password.equals("")) {//When the user does not enter both of input boxes, system indicates an error message.
                    Toast.makeText(this, "Please enter your username or password", Toast.LENGTH_SHORT).show();
                } else { ///Determine if two input boxes are not empty
                    ArrayList<User> data = Database.getAllData();//The list of the ArrayList class acts as a container for the data
                    boolean match = false;//Default username and password do not match
                    for (int i = 0; i < data.size(); i++) {//Iterate through all the data in the table
                        user = data.get(i);//user represents the complete data of a user

                        //When the user name entered by the user is equal to the data already in the table
                        // and the password entered by the user is the same as the password recorded in the table
                        if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                            match = true;//username and password match, change to True
                            break;// Exit traversal
                        } else {
                            match = false;//As long as one of the conditions is not met, it is judged as a mismatch
                        }
                    }
                    //When match is true, the login is successful.
                    // The next step is to jump to the main page for the different plan types, depending on the user's previous selection
                    if (match) {
                        if (user.getPlantype() == 0) {//User's previous choice was 365-day plan
                            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();//The system indicates a successful login
                            Intent intent = new Intent(this, MainActivity.class);//Interface jumps and data transfer will take place on this page and in the MainActivity
                            Bundle bundle = new Bundle();//Create a new Bundle class to carry data
                            bundle.putSerializable("user", user);//Putting object named user into the Bundle to to pass data to MainActivity
                            intent.putExtras(bundle);//Pass data to MainActivity
                            startActivity(intent);//Jump to MainActivity
                            finish();//Destroy this Activity
                        }
                        if (user.getPlantype() == 1) {//User's previous choice was 52-week plan
                            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();//The system indicates a successful login
                            Intent intent = new Intent(this, MainActivity2.class);//Interface jumps and data transfer will take place on this page and in the MainActivity2
                            Bundle bundle = new Bundle();//Create a new Bundle class to carry data
                            bundle.putSerializable("user", user);//Putting object named user into the Bundle to to pass data to MainActivity2
                            intent.putExtras(bundle);//Pass data to MainActivity2
                            startActivity(intent);//Jump to MainActivity2
                            finish();//Destroy this Activity
                        }
                    } else { //When username does not match input password, system indicates an error message
                        Toast.makeText(this, "Incorrect username or password, please re-enter", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case (R.id.bt_loginactivity_register): //When user clicks on register button
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);//Interface jumps will take place on this page and in the RegisterActivity
                startActivity(intent);//Jump to RegisterActivity
                finish();//Destroy this Activity
                break;
        }

    }
}
