package com.example.android.uni;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class userlist extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView listview;
    SimpleCursorAdapter adapter;
    public void makegroup(View view) {
        Intent i = new Intent(this, makegroup.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar3);
        setSupportActionBar(toolbar);
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.joingroup1);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        new showusers().execute();
  showuserslist();
}


    public void showuserslist(){

        listview = (ListView) findViewById(R.id.list);

        String[] from = new String[]{sqlitehelper.COLUMN_GROUP_NAME};
        int[] to= new int[]{R.id.groupname};
        getLoaderManager().initLoader(0,null,this);
        adapter = new SimpleCursorAdapter(this,R.layout.singlerow,null,from,to,0);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), mainactivityfragment.class);
                Uri todoUri = Uri.parse(contentprovider.Content_URI+ "/" + id);
                i.putExtra(contentprovider.CONTENT_ITEM_TYPE, todoUri);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.joingroup) {
            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();

            int count = getContentResolver().delete(contentprovider.Content_URI, null, null);
            Log.i("", "onOptionsItemSelected: "+count);
            int count1=getContentResolver().delete(contentprovider.Content_URI1,null,null);
            Log.i("", "onOptionsItemSelected: "+count1);
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { sqlitehelper.COLUMN_ID, sqlitehelper.COLUMN_GROUP_ID,sqlitehelper.COLUMN_GROUP_NAME };
        CursorLoader cursorLoader = new CursorLoader(this,contentprovider.Content_URI,projection,null,null,null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public class showusers extends AsyncTask<String,String,String> {
        JSONparserUserlist jsonparser = new JSONparserUserlist();
        HashMap<String, String> param;
        String url = "http://192.168.10.110:8080/showusers";

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            try {
                param = new HashMap<>();
                param.put("username", username);


                String jobj = jsonparser.makeHttpRequest(url, param);
                if (jobj != null) {
                    return jobj;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {

            String projection[]={sqlitehelper.COLUMN_GROUP_ID};
            String selection=sqlitehelper.COLUMN_GROUP_ID+" = ?";
            String[] selectionArgs = null;
                    Log.i("", "onPostExecute: " + json);
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONObject jsonObject = jsonObject1.getJSONObject("data");
                    String groupid = jsonObject.getString("groupid");
                    String groupname = jsonObject.getString("groupname");
                    Log.i("", "onPostExecute: " + groupid +"  "+ groupname);
                    selectionArgs = new String[]{ groupid };
                    Cursor cursor = getContentResolver().query(contentprovider.Content_URI, projection, selection, selectionArgs, null);
                    if (cursor!=null&&cursor.getCount()>0){
                        Log.i("", "onPostExecute: " + "return   " + cursor);
                        }
                    else{

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(sqlitehelper.COLUMN_GROUP_ID,groupid);
                                contentValues.put(sqlitehelper.COLUMN_GROUP_NAME, groupname);
                                Uri uri =getContentResolver().insert(contentprovider.Content_URI,contentValues);
                                Log.i("", "onPostExecute: " + uri);

                    }
                    if(cursor!=null){
                        cursor.close();}
                }}
            catch(Exception e){
                Log.i("", "onPostExecute: "+"err");
            }
        }
        }
}
