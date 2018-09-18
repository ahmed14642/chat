package com.example.android.uni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    public void loginscreen(View view) {

        Intent i = new Intent(this, login.class);
        startActivity(i);
    }

    public void signupscreen(View view) {

        Intent i = new Intent(this, signup.class);
        startActivity(i);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "EMPTY");
        Log.i("", "onCreate: "+username);
        String password = sharedPreferences.getString("password","");
        Log.i("", "onCreate: "+password);
        if (username.equals("EMPTY")){
            return;
        }
        else{
            Intent i = new Intent(this,userlist.class);
            startActivity(i);
        }


    }
}
