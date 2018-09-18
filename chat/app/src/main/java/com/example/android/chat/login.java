package com.example.android.uni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by AHMAD BUTTAR on 2/4/2016.
 */
public class login extends AppCompatActivity {
    EditText usernamelogin, passwordlogin;
    String username1,password1;

    public void loginon(View view) {
        usernamelogin = (EditText) findViewById(R.id.editText2);
        passwordlogin = (EditText) findViewById(R.id.editText3);
        username1 = usernamelogin.getText().toString();
        password1 =passwordlogin.getText().toString();

        new login1().execute(username1,password1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Toolbar appbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        usernamelogin = (EditText) findViewById(R.id.editText2);
        passwordlogin = (EditText) findViewById(R.id.editText3);

    }
   private class login1 extends AsyncTask<String,String,JSONObject>{
       JSONparser jsonparser = new JSONparser();
       HashMap<String, String> params;
       String url = "http://192.168.10.110:8080/login";



       @Override
       protected JSONObject doInBackground(String... param) {
           try {
               params = new HashMap<>();
               params.put("username",param[0]);
               params.put("password", param[1]);


               JSONObject jobj = jsonparser.makeHttpRequest(url, params);
               if(jobj!=null){
                   return jobj;}
           }catch(Exception e){
               e.printStackTrace();
           }
           return null;
       }
       @Override
       protected void onPostExecute(JSONObject json) {
           try {
               String res = json.getString("response");

               if(res.equals("password correct")){
                   SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.putString("username",username1);
                   editor.putString("password",password1);
                   editor.apply();
                   Intent in = new Intent(getApplicationContext(), userlist.class);
                   startActivity(in);
               }else if(res.equals("password incorrect")){
                   Log.i("lol", "onPostExecute: password incorrect");
               }
           } catch (Exception e) {
               e.printStackTrace();
           }


       }
   }
}
