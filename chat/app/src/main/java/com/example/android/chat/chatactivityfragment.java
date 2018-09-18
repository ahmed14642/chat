package com.example.android.uni;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class chatactivityfragment extends Fragment {
    String groupid;
    EditText writemessage;
    String username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatactivityfragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();
        groupid=extras.getString("Groupid");
        Uri to = extras.getParcelable(contentprovider.CONTENT_ITEM_TYPE);
        String[] projection = {sqlitehelper.COLUMN_GROUP_ID,
                sqlitehelper.COLUMN_GROUP_NAME};
        Cursor cursor = getActivity().getContentResolver().query(to, projection, null, null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            groupid = cursor.getString(cursor
                    .getColumnIndexOrThrow(sqlitehelper.COLUMN_GROUP_ID));
            String category = cursor.getString(cursor
                    .getColumnIndexOrThrow(sqlitehelper.COLUMN_GROUP_NAME));

            cursor.close();
            Log.i("", "onCreate: " + category + "  " + groupid);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", getContext().MODE_PRIVATE);
            username = sharedPreferences.getString("username", "");
            new getmessage().execute(groupid);
        }

    }

    public void sendmessage(View view) {

        writemessage = (EditText) view.findViewById(R.id.writemessage);
        String message = writemessage.getText().toString();


        new sendmessage().execute(username, message, groupid);

        writemessage.setText("");

    }


    class sendmessage extends AsyncTask<String, String, JSONObject> {
        JSONparser jsonparser = new JSONparser();
        HashMap<String, String> param;
        String url = "http://192.168.10.196:8080/sendmessage";

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                param = new HashMap<>();
                param.put("groupid", params[0]);

                JSONObject json = jsonparser.makeHttpRequest(url, param);
                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

        }


    }

    class getmessage extends AsyncTask<String, String, String> {
        JSONparserUserlist jsonparser = new JSONparserUserlist();
        HashMap<String, String> param;
        String url = "http://192.168.10.196:8080/getmessage";

        @Override
        protected String doInBackground(String... params) {
            try {
                param = new HashMap<>();

                param.put("groupid", params[0]);

                String json = jsonparser.makeHttpRequest(url, param);
                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            String projection[] = {sqlitehelper.COLUMN_UNIQUE_ID};
            String selection = sqlitehelper.COLUMN_UNIQUE_ID + " = ?";
            String[] selectionArgs = null;
            try {
                JSONArray jsonArray = new JSONArray(json);
                Log.i("", "onPostExecute: " + jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONObject jsonObject = jsonObject1.getJSONObject("data");
                    String message = jsonObject.getString("message");
                    String username = jsonObject.getString("username");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("key");
                    String id = jsonObject2.getString("id");
                    selectionArgs = new String[]{ id };
                    Cursor cursor = getActivity().getContentResolver().query(contentprovider.Content_URI1, projection, selection, selectionArgs, null);
                    Log.i("n", "onPostExecute: " + message + "  " + username + "  " + id);
                    if (cursor!=null&&cursor.getCount()>0){
                        Log.i("p", "onPostExecute: " + "return   " + cursor);
                    }
                    else{

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(sqlitehelper.COLUMN_MESSAGES, message);
                        contentValues.put(sqlitehelper.COLUMN_UNIQUE_ID, id);
                        contentValues.put(sqlitehelper.COLUMN_USERNAME, username);
                        Uri uri = getActivity().getContentResolver().insert(contentprovider.Content_URI1, contentValues);
                        Log.i("p", "onPostExecute: " + uri);
                    }
                    if(cursor!=null){
                        cursor.close();}
                }
            } catch (Exception e) {
                Log.i("", "onPostExecute: " + "err");
            }


        }
    }
    class messageadapter extends CursorAdapter{


        public messageadapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }
}






