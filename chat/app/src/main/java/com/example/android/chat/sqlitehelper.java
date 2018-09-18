package com.example.android.uni;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by AHMAD BUTTAR on 2/20/2016.
 */
public class sqlitehelper extends SQLiteOpenHelper {

    public static final String TABLE_GROUPS= "groups";
    public static final String COLUMN_GROUP_NAME="groupname";
    public static final String COLUMN_GROUP_ID="groupid";
    public static final String COLUMN_ID="_id";
    private static final String DATABASE_NAME="groups.db";
    private static final int DATABASE_VERSION=3;


    public static final String TABLE_MESSAGES="messages";
    public static final String COLUMN_MESSAGES_ID="_id";
    public static final String COLUMN_MESSAGES="messages";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_UNIQUE_ID="uniqueid";

    public static final String TABLE_PHOTOSURL="photos";
    public static final String COLUMN_PHOTOSURL_ID="_id";
    public static final String COLUMN_PHOTOSURL="photosurl";
    public static final String COLUMN_UNIQUE_ID1="uniqueid";
    public static final String COLUMN_GROUPID="groupid";


private static final String DATABASE_CREATE="CREATE TABLE IF NOT EXISTS "+ TABLE_GROUPS+" ("+COLUMN_ID+" integer primary key autoincrement, "+COLUMN_GROUP_NAME+" TEXT NOT NULL, "+COLUMN_GROUP_ID+" TEXT NOT NULL  ); ";
private static final String DATABASE_CREATE1="CREATE TABLE IF NOT EXISTS "+ TABLE_MESSAGES+" ("+COLUMN_MESSAGES_ID+" integer primary key autoincrement, "+COLUMN_UNIQUE_ID+" TEXT NOT NULL, "+COLUMN_MESSAGES+" TEXT NOT NULL, "+COLUMN_USERNAME+" TEXT NOT NULL ); ";
    private static final String DATABASE_CREATE2="CREATE TABLE IF NOT EXISTS "+ TABLE_PHOTOSURL+" ("+COLUMN_PHOTOSURL_ID+" integer primary key autoincrement, "+COLUMN_PHOTOSURL+" TEXT NOT NULL, "+COLUMN_UNIQUE_ID1+" TEXT NOT NULL, "+COLUMN_GROUPID+" TEXT NOT NULL ); ";


    public sqlitehelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE1);
         db.execSQL(DATABASE_CREATE2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(sqlitehelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_MESSAGES);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PHOTOSURL);
        onCreate(db);
    }
}
