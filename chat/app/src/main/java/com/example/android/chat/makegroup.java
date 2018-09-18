package com.example.android.uni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class makegroup extends AppCompatActivity {
    String groupid;
    public void makegroupfinal(View view) {
        EditText n = ((EditText) findViewById(R.id.editText4));
        EditText k = ((EditText) findViewById(R.id.editText5));
        groupid = n.getText().toString();
        String groupname = k.getText().toString();
        SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);
        String username=preferences.getString("username","");
        new makegroup1().execute(groupname,groupid,username,username);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makegroup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private class makegroup1 extends AsyncTask<String,String,JSONObject> {
        JSONparser jsonparser = new JSONparser();
        HashMap<String, String> params;
        String url = "http://192.168.10.196:8080/makegroup";
        @Override
        protected JSONObject doInBackground(String... param) {
            try {
                params = new HashMap<>();
                params.put("groupname",param[0]);
                params.put("groupid", param[1]);
                params.put("username",param[2]);
                params.put("admin",param[3]);

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
                if(res.equals("successful")){
                    Log.i("n", "onPostExecute: "+res);
                    Intent o = new Intent(getApplicationContext(),mainactivityfragment.class);
                    o.putExtra("Groupid",groupid);
                    startActivity(o);
                    finish();
                }else if(res.equals("password incorrect")){
                    Log.i("p", res);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
