package com.example.android.uni;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;




import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

public class filterpic extends AppCompatActivity {
    String groupid;
    File filePath;
    String photoPath;
    FileInputStream fileInputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_page);


        Bundle extras = getIntent().getExtras();
        photoPath = extras.getString("photoPath");
        Log.i("lol", "onCreate: "+photoPath);
        groupid = extras.getString("groupid");


    }
    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, false);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        ImageView mImageView = (ImageView) findViewById(R.id.imagebyte);

        // Get the dimensions of the View


        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);

        mImageView.setImageBitmap(bitmap);

    }


    public void upload(View view){
        new HttpUpload(this,photoPath,groupid).execute();


    }
}