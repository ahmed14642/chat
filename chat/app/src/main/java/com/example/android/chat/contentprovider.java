package com.example.android.uni;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


/**import android.support.annotation.Nullable;

 * Created by AHMAD BUTTAR on 2/20/2016.
 */
public class contentprovider extends ContentProvider {

    sqlitehelper sqliteHelper;
    private static final int GROUPS = 10;
    private static final int GROUPS_ID = 20;
    private static final int MESSAGES=30;
    private static final int MESSAGES_ID=40;
    private static final int PHOTOS=50;
    private static final int PHOTOS_ID=60;

       private static final String AUTHORITY="com.example.android.uni";

       private static final String BASE_PATH = "groups";
       private static final String BASE_PATH1="messages";
       private static final String BASE_PATH2="photos";

       public static final Uri Content_URI=Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);
       public static final Uri Content_URI1=Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH1);
       public static final Uri Content_URI2=Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH2);
       public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/groups";
       public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/group";
    public static final String CONTENT_TYPE1=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/messages";
    public static final String CONTENT_ITEM_TYPE1 = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/message";
    public static final String CONTENT_TYPE2=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/photos";
    public static final String CONTENT_ITEM_TYPE2 = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/photo";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH,GROUPS );
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#",GROUPS_ID );
        sURIMatcher.addURI(AUTHORITY,BASE_PATH1,MESSAGES);
        sURIMatcher.addURI(AUTHORITY,BASE_PATH1 +"/#",MESSAGES_ID);
        sURIMatcher.addURI(AUTHORITY,BASE_PATH2,PHOTOS);
        sURIMatcher.addURI(AUTHORITY,BASE_PATH2 +"/#",PHOTOS_ID);
    }

    @Override
    public boolean onCreate() {
        sqliteHelper=new sqlitehelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();



        switch (sURIMatcher.match(uri)){
            case GROUPS:
                queryBuilder.setTables(sqlitehelper.TABLE_GROUPS);
                break;
            case GROUPS_ID:
                queryBuilder.setTables(sqlitehelper.TABLE_GROUPS);
               queryBuilder.appendWhere(sqlitehelper.COLUMN_ID +"="+uri.getLastPathSegment());
           break;
            case MESSAGES:
                queryBuilder.setTables(sqlitehelper.TABLE_MESSAGES);
                break;
            case MESSAGES_ID:
                queryBuilder.setTables(sqlitehelper.TABLE_MESSAGES);
                queryBuilder.appendWhere(sqlitehelper.COLUMN_MESSAGES_ID + "=" + uri.getLastPathSegment());
                break;
            case PHOTOS:
                queryBuilder.setTables(sqlitehelper.TABLE_PHOTOSURL);
                break;
            case PHOTOS_ID:
                queryBuilder.setTables(sqlitehelper.TABLE_PHOTOSURL);
                queryBuilder.appendWhere(sqlitehelper.COLUMN_PHOTOSURL_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case GROUPS:
                return CONTENT_TYPE;
            case GROUPS_ID :
                return CONTENT_ITEM_TYPE;
            case MESSAGES:
                return CONTENT_TYPE1;
            case MESSAGES_ID:
                return CONTENT_ITEM_TYPE1;
            case PHOTOS:
                return CONTENT_TYPE2;
            case PHOTOS_ID:
                return CONTENT_ITEM_TYPE2;
            default:
                throw new IllegalArgumentException("Invalid URI: "+uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = sqliteHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case GROUPS:
                id = sqlDB.insert(sqlitehelper.TABLE_GROUPS, null, values);
                break;
            case MESSAGES:
                id = sqlDB.insert(sqlitehelper.TABLE_MESSAGES,null,values);
                break;
            case PHOTOS:
                id = sqlDB.insert(sqlitehelper.TABLE_PHOTOSURL,null,values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id;
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = sqliteHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case GROUPS:
                rowsDeleted = sqlDB.delete(sqlitehelper.TABLE_GROUPS, selection,
                        selectionArgs);
                break;
            case GROUPS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(sqlitehelper.TABLE_GROUPS,
                           sqlitehelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(sqlitehelper.TABLE_GROUPS,
                            sqlitehelper.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case MESSAGES:
                rowsDeleted = sqlDB.delete(sqlitehelper.TABLE_MESSAGES, selection,
                        selectionArgs);
                break;
            case MESSAGES_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(sqlitehelper.TABLE_MESSAGES,
                            sqlitehelper.COLUMN_MESSAGES_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(sqlitehelper.TABLE_MESSAGES,
                            sqlitehelper.COLUMN_MESSAGES_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case PHOTOS:
                rowsDeleted = sqlDB.delete(sqlitehelper.TABLE_PHOTOSURL, selection,
                        selectionArgs);
                break;
            case PHOTOS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(sqlitehelper.TABLE_PHOTOSURL,
                            sqlitehelper.COLUMN_PHOTOSURL_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(sqlitehelper.TABLE_PHOTOSURL,
                            sqlitehelper.COLUMN_PHOTOSURL_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id;
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = sqliteHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case GROUPS:
                rowsUpdated = sqlDB.update(sqlitehelper.TABLE_GROUPS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case GROUPS_ID:
                 id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(sqlitehelper.TABLE_GROUPS,
                            values,
                            sqlitehelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(sqlitehelper.TABLE_GROUPS,
                            values,
                            sqlitehelper.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case MESSAGES:
                rowsUpdated = sqlDB.update(sqlitehelper.TABLE_MESSAGES,
                        values,
                        selection,
                        selectionArgs);
                break;
            case MESSAGES_ID:
                 id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(sqlitehelper.TABLE_MESSAGES,
                            values,
                            sqlitehelper.COLUMN_MESSAGES_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(sqlitehelper.TABLE_MESSAGES,
                            values,
                            sqlitehelper.COLUMN_MESSAGES_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case PHOTOS:
                rowsUpdated = sqlDB.update(sqlitehelper.TABLE_PHOTOSURL,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PHOTOS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(sqlitehelper.TABLE_PHOTOSURL,
                            values,
                            sqlitehelper.COLUMN_PHOTOSURL_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(sqlitehelper.TABLE_MESSAGES,
                            values,
                            sqlitehelper.COLUMN_PHOTOSURL_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
