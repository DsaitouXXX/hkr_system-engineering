package com.example.a3.service;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.a3.model.message;
import com.example.a3.model.trashbin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqliteMessage{
    //一些宏定义和声明
    private static final String TAG = "TrashDataManager";
    private static final String DB_NAME = "data0";
    private static final String TABLE_NAME = "Messagedata";
    public static final String ID = "_id";
    public static final String TIME= "time";
    public static final String NAME = "name";
    public static final String LOCATION= "location";
    public static final String PER= "per";
    public static final String VID= "id";
    public static final String FLAG= "flag";
    private static final int DB_VERSION = 1;
    private Context mContext = null;

    //创建用户book表
    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key,"
            + TIME + " text,"
            + NAME + " text,"
            + LOCATION + " text,"
            + VID + " text,"
            + FLAG + " text,"
            + PER + " text" + ");";

    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;

    //DataBaseManagementHelper继承自SQLiteOpenHelper
    private static class DataBaseManagementHelper extends SQLiteOpenHelper {

        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG,"db.getVersion()="+db.getVersion());
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            db.execSQL(DB_CREATE);
            Log.i(TAG, "db.execSQL(DB_CREATE)");
            Log.e(TAG, DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "DataBaseManagementHelper onUpgrade");
            onCreate(db);
        }
    }

    public SqliteMessage(Context context) {
        mContext = context;
        Toast.makeText(mContext,"Create succeed",Toast.LENGTH_SHORT).show();
        Log.i(TAG, "UserDataManager construction!");
    }
    //打开数据库
    public void openDataBase() throws SQLException {
        Log.i(TAG, "create");
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }
    //关闭数据库
    public void closeDataBase() throws SQLException {
        Log.i(TAG, "close");
        mDatabaseHelper.close();
    }

    private String getTIME(){
        Date dt = new Date();
        //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String temp_str=sdf.format(dt);
        return temp_str;
    }

    public void UpdateFlag(String id){
        openDataBase();
        ContentValues cv = new ContentValues();
        cv.put(FLAG,"true");
        mSQLiteDatabase.update(TABLE_NAME,cv,VID+"= ?",new String[]{id});
        closeDataBase();
        return;
    }
    public void add(List<trashbin> trashlbins,String endString) {
        openDataBase();
        //    Log.e("add=",name+words+querys+dbname);
        String time=getTIME();
        Log.i(TAG, "delete start");
        mSQLiteDatabase.delete(TABLE_NAME,TIME+"="+time,null);
        Log.i(TAG, "delete end");
        for (trashbin trash:trashlbins){
            String per=trash.getPer();
            String loc=trash.getLocation();
            int x=Integer.parseInt(per);
            if (x>=50 && loc.equals(endString))
            {
                ContentValues values = new ContentValues();
                values.put(NAME, trash.getName());
                values.put(LOCATION, loc);
                values.put(PER, per);
                values.put(TIME, time);
                values.put(VID, trash.getId());
                values.put(FLAG, "false");
                mSQLiteDatabase.insert(TABLE_NAME, ID, values);
            }
        }
        Log.i(TAG, "add yep");
        // Log.e("add=","yep");
        closeDataBase();
        return;
    }

    //返回垃圾桶列表
    public List<message> get_all() {
        openDataBase();
        List<message> trashlbins=new ArrayList<>();
        String time=getTIME();
        Log.i(TAG,"time"+time);

        Cursor mCursor=mSQLiteDatabase.rawQuery("select * from Messagedata where time=?",new String[] {time});
        Log.i(TAG,"getCount()"+mCursor.getCount());
        if (mCursor.getCount()==0) return null;
        while (mCursor.moveToNext()) {
            String per=mCursor.getString(mCursor.getColumnIndex(PER));
            String loc=mCursor.getString(mCursor.getColumnIndex(LOCATION));
            message message=new message();
            message.setName(mCursor.getString(mCursor.getColumnIndex(NAME)));
            message.setLocation(loc);
            message.setPer(per);
            message.setId(mCursor.getString(mCursor.getColumnIndex(VID)));
            if (mCursor.getString(mCursor.getColumnIndex(FLAG)).equals("false"))
                message.setFlag(false);//false 表示未读
                else message.setFlag(true);
            //Log.i(TAG,"names "+mCursor.getString(mCursor.getColumnIndex(NAME)));
            //Log.i(TAG,"LOCATIONs "+mCursor.getString(mCursor.getColumnIndex(LOCATION)));
            //Log.i(TAG,"PERs "+mCursor.getString(mCursor.getColumnIndex(PER)));
            trashlbins.add(message);
        }
        Log.i(TAG,"add yep!");
        closeDataBase();
        return trashlbins;
    }

}