package com.example.lightupthelove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper Database;
    private Button mBtRegisteractivityRegister;
    private ImageView mIvRegisteractivityBack;
    private EditText mEtRegisteractivityUsername;
    private EditText mEtRegisteractivityPassword1;
    private EditText mEtRegisteractivityPassword2;
    private User user;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialising controls
        mBtRegisteractivityRegister = findViewById(R.id.bt_registeractivity_register);
        mIvRegisteractivityBack = findViewById(R.id.iv_registeractivity_back);
        mEtRegisteractivityUsername = findViewById(R.id.et_registeractivity_username);
        mEtRegisteractivityPassword1 = findViewById(R.id.et_registeractivity_password1);
        mEtRegisteractivityPassword2 = findViewById(R.id.et_registeractivity_password2);

        //Instantiate mDBOpenHelper
        Database = new DatabaseHelper(this);

        // Setting the click event listener
        mIvRegisteractivityBack.setOnClickListener(this);
        mBtRegisteractivityRegister.setOnClickListener(this);
    }

    //The verification of the content at login registration,
    // e.g. whether the password entered twice is the same and whether the information is filled in fully was inspired by the CSDN website,
    // Reference URL:https://blog.csdn.net/qq_42001163/article/details/109207003

    // I have improved the logic at registration on the basis of the tutorial.
    // The logic of my code is to include the login in the registration function.
    // The information entered by the user is first added to the database,
    // then all the data is traversed and the logic in the login is followed to find the row of data corresponding to the entered username and the username in the table,
    // compare the passwords and then go to the main screen.

    // In addition, the tutorial uses threads and SharedPreferences for page jumping and data passing,
    // while my method inherits Serializable and Cloneable in the User class to serialize, pass and clone objects
 @Override
public void onClick(View view) {
     //The login page has two buttons: back and register.
     // Use switch-case to match the code executed in different cases
        switch(view.getId()){
            case (R.id.iv_registeractivity_back)://When user clicks on back button
                Intent intent = new Intent(this, LoginActivity.class);//Interface jumps will take place on this page and in the LoginActivity
                startActivity(intent);//Jump to LoginActivity
                finish();
                break;
            case(R.id.bt_registeractivity_register)://When user clicks on submit button to finish registering

                String username=mEtRegisteractivityUsername.getText().toString();//Get the username in the input box and convert it to String type
                String password1=mEtRegisteractivityPassword1.getText().toString();//Get the password in the input box and convert it to String type
                String password=mEtRegisteractivityPassword2.getText().toString();//Get the re-entered password in the input box and convert it to String type


                    if (username.equals("")|password.equals("")|password1.equals("")) {//Determine if two input boxes are empty
                        //When the user does not enter all input boxes, system indicates an error message
                        Toast.makeText(this, "Registration failed due to incomplete information", Toast.LENGTH_SHORT).show();
                        } else {
                        if(!password.equals(password1)){//If the two passwords entered do not match
                            Toast.makeText(this, "Inconsistent passwords", Toast.LENGTH_SHORT).show();//System indicates an error message
                        }
                        else {//If the two passwords entered do match

                            ArrayList<User> data = Database.getAllData();//The list of the ArrayList class acts as a container for the data
                            for (int i = 0; i < data.size(); i++) {//Checking whether a username has been registered
                                user = data.get(i);
                                if (username.equals(user.getName()) ) {
                                    Toast.makeText(this, "Username has been registered, please re-enter", Toast.LENGTH_SHORT).show();
                                    mEtRegisteractivityUsername.setText("");
                                    return ;
                                }
                            }

                            Database.add(username, password);// Add the username and password to the database;
                            data = Database.getAllData();
                            for (int i = 0; i < data.size(); i++) {//Iterate through all the data in the table
                                user = data.get(i);//user represents the complete data of a user
                                if (username.equals(user.getName()) && password.equals(user.getPassword())) {
                                    Intent intent2 = new Intent(RegisterActivity.this, MainActivity.class);//Interface jumps and data transfer will take place on this page and in the MainActivity
                                    Bundle bundle = new Bundle();//Create a new Bundle class to carry data
                                    bundle.putSerializable("user", user);//Putting object named user into the Bundle to to pass data to MainActivity
                                    intent2.putExtras(bundle);//Pass data to MainActivity
                                    startActivity(intent2);//Jump to MainActivity
                                    finish();//Destroy this Activity
                                    //When register successfully, system indicates a message
                                    Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                }
                    break;
        }

}
}