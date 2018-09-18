package com.example.android.uni;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;

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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class photosfragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    Button button;
    String groupid;
    messageadapter photosadapter;
    GridView gridView;
    DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_photos, container, false);
        gridView = (GridView)v.findViewById(R.id.gridView);
        button = (Button) v.findViewById(R.id.gotopic);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), cameraactivity.class);
                i.putExtra("groupid", groupid);
                startActivity(i);

            }
        });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getActivity().getIntent().getExtras();
        Uri to = extras.getParcelable(contentprovider.CONTENT_ITEM_TYPE);
        String[] projection = {sqlitehelper.COLUMN_GROUP_ID,
                sqlitehelper.COLUMN_GROUP_NAME};
        Cursor cursor = getActivity().getContentResolver().query(to, projection, null, null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            groupid = cursor.getString(cursor
                    .getColumnIndexOrThrow(sqlitehelper.COLUMN_GROUP_ID));
        }
        if(cursor!=null){
            cursor.close();}
        Log.i("noway", "onCreate: " + groupid);
        BitmapFactory.Options resizeOptions = new BitmapFactory.Options();
        resizeOptions.inSampleSize = 3; // decrease size 3 times
        resizeOptions.inScaled = true;
        defaultOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.camera)
                .showImageForEmptyUri(R.drawable.camera)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .build();

        config = new ImageLoaderConfiguration.Builder(
                getActivity())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);






        new geturl().execute(groupid);
        getLoaderManager().initLoader(1000,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { sqlitehelper.COLUMN_PHOTOSURL_ID, sqlitehelper.COLUMN_PHOTOSURL };
        CursorLoader cursorLoader = new CursorLoader(getActivity(),contentprovider.Content_URI2,projection,null,null,null);

        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
     photosadapter=new messageadapter(getActivity(),data);
        gridView.setAdapter(photosadapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    class geturl extends AsyncTask<String, String, String> {
        JSONparserUserlist jsonparser = new JSONparserUserlist();
        HashMap<String, String> param;
        String url = "http://192.168.10.121:8080/signedurl";

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
            String projection[] = {sqlitehelper.COLUMN_UNIQUE_ID1};
            String selection = sqlitehelper.COLUMN_UNIQUE_ID1 + " = ?";
            String[] selectionArgs = null;
            try {
                JSONArray jsonArray = new JSONArray(json);
                Log.i("lol", "onPostExecute: " + jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONObject jsonObject = jsonObject1.getJSONObject("data");
                    String photosurl = jsonObject.getString("url");
                    String groupid = jsonObject.getString("groupid");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("key");
                    String id = jsonObject2.getString("id");
                    selectionArgs = new String[]{id};
                    Cursor cursor = getActivity().getContentResolver().query(contentprovider.Content_URI2, projection, selection, selectionArgs, null);
                    Log.i("n", "onPostExecute: " + photosurl + "  " + groupid + "  " + id);
                    if (cursor != null && cursor.getCount() > 0) {
                        Log.i("p", "onPostExecute: " + "return   " + cursor);
                    } else {

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(sqlitehelper.COLUMN_PHOTOSURL, photosurl);
                        contentValues.put(sqlitehelper.COLUMN_UNIQUE_ID1, id);
                        contentValues.put(sqlitehelper.COLUMN_GROUPID,groupid );
                        Uri uri = getActivity().getContentResolver().insert(contentprovider.Content_URI2, contentValues);
                        Log.i("p", "onPostExecute: " + uri);
                    }
                    if(cursor!=null){
                    cursor.close();}
                }
            } catch (Exception e) {
                Log.i("n", "onPostExecute: " + "err");
            }


        }
    }
    class messageadapter extends CursorAdapter {
        ImageLoader imageLoader =ImageLoader.getInstance();



       LayoutInflater mInflater;
        public messageadapter(Context context, Cursor c) {
            super(context, c);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = mInflater.inflate(R.layout.photos, parent, false);
            return v;

        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView iv = (ImageView) view.findViewById(R.id.imageView);
            imageLoader.displayImage(cursor.getString(cursor.getColumnIndex(sqlitehelper.COLUMN_PHOTOSURL)),
                    iv, defaultOptions);

        }
    }
}
