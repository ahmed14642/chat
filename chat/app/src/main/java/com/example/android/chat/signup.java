package com.example.android.uni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by AHMAD BUTTAR on 2/4/2016.
 */
public class signup extends AppCompatActivity {
    EditText username, email, password, Firstname, Lastname;
    String username1,email1, password1, firstname1, lastname1;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    public void signupon(View view) {
        username = (EditText) findViewById(R.id.username1);
        email = (EditText) findViewById(R.id.email1);
        password = (EditText) findViewById(R.id.password1);
        Firstname = (EditText) findViewById(R.id.firstname1);
        Lastname = (EditText) findViewById(R.id.lastname1);

        username1= username.getText().toString();
        email1= email.getText().toString();
        password1 = password.getText().toString();
        firstname1= Firstname.getText().toString();
        lastname1= Lastname.getText().toString();

        new signUp().execute(username1,email1,password1,firstname1,lastname1);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }


    private class signUp extends AsyncTask<String, String, JSONObject> {
        JSONparser jsonparser = new JSONparser();
        HashMap<String, String> params;
        String url = "http://10.0.2.2:8080/signup";


        @Override
        protected JSONObject doInBackground(String... args) {


            try {
                params = new HashMap<>();
                params.put("username", args[0]);
                params.put("email", args[1]);
                params.put("password", args[2]);
                params.put("firstname", args[3]);
                params.put("lastname", args[4]);

                JSONObject jobj = jsonparser.makeHttpRequest(url, params);
                if (jobj != null) {
                    return jobj;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                String res = json.getString("response");
                Log.i("", "onPostExecute: ");
                if (res.equals("successful")) {
                    Log.i("", "onPostExecute: " + res);
                    SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username1);
                    editor.putString("password", password1);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), userlist.class);
                    startActivity(i);
                } else if (res.equals("err")) {
                    Log.i("", "onPostExecute: " + "username already registered");
                } else if (res.equals("failed")) {
                    Log.i("", "onPostExecute: " + "did not save");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}



