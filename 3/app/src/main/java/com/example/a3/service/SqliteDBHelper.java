package com.example.a3.service;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.a3.model.trashbin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqliteDBHelper{
        //一些宏定义和声明
        private static final String TAG = "TrashDataManager";
        private static final String DB_NAME = "data";
        private static final String TABLE_NAME = "Trashdata";
        public static final String ID = "_id";
        public static final String TIME= "time";
        public static final String NAME = "name";
        public static final String LOCATION= "location";
        public static final String PER= "per";
        public static final String VID= "id";
        private static final int DB_VERSION = 1;
        private Context mContext = null;

        //创建用户book表
        private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " integer primary key,"
                + TIME + " text,"
                + NAME + " text,"
                + LOCATION + " text,"
                + VID + " text,"
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

        public SqliteDBHelper(Context context) {
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
        //添加一条数据集
   /*     public long add(String name,String words) {
            ContentValues values = new ContentValues();
            values.put(NAME, name);
            values.put(WORDS, words);
            return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
        }*/
    public void add(List<trashbin> trashlbins) {
        openDataBase();
    //    Log.e("add=",name+words+querys+dbname);
        String time=getTIME();
       // Log.i(TAG, "delete start");
        mSQLiteDatabase.delete(TABLE_NAME,TIME+"="+time,null);
     //   Log.i(TAG, "delete end");
        for (trashbin trash:trashlbins){
            ContentValues values = new ContentValues();
            values.put(NAME, trash.getName());
            values.put(LOCATION, trash.getLocation());
            values.put(PER, trash.getPer());
            values.put(TIME, time);
            values.put(VID, trash.getId());
            mSQLiteDatabase.insert(TABLE_NAME, ID, values);
        }
       // Log.i(TAG, "add yep");
       // Log.e("add=","yep");
        closeDataBase();
        return;
    }

    //返回垃圾桶列表
    public List<trashbin> get_all() {
        openDataBase();
        List<trashbin> trashlbins=new ArrayList<>();
        String time=getTIME();
        Log.i(TAG,"time"+time);

        Cursor mCursor=mSQLiteDatabase.rawQuery("select * from Trashdata where time=?",new String[] {time});
        Log.i(TAG,"getCount()"+mCursor.getCount());
        if (mCursor.getCount()==0) return null;
        while (mCursor.moveToNext()) {
            trashbin trashbin=new trashbin();
            trashbin.setName(mCursor.getString(mCursor.getColumnIndex(NAME)));
            trashbin.setLocation(mCursor.getString(mCursor.getColumnIndex(LOCATION)));
            trashbin.setPer(mCursor.getString(mCursor.getColumnIndex(PER)));
            trashbin.setId(mCursor.getString(mCursor.getColumnIndex(VID)));
            //Log.i(TAG,"names "+mCursor.getString(mCursor.getColumnIndex(NAME)));
            //Log.i(TAG,"LOCATIONs "+mCursor.getString(mCursor.getColumnIndex(LOCATION)));
            //Log.i(TAG,"PERs "+mCursor.getString(mCursor.getColumnIndex(PER)));
            trashlbins.add(trashbin);
        }
         Log.i(TAG,"add yep!");
        closeDataBase();
        return trashlbins;
    }
        //得到数据集的字段名
        public String[] getWords(String name) {
            String names=null;
            String sql="select words from shujuji where name='"+name+"'";
            Cursor mCursor=mSQLiteDatabase.rawQuery("select words from shujuji where name=?",new String[] {name});
            Log.i(TAG,"num"+mCursor.getCount());
            while (mCursor.moveToNext()) {
                names=mCursor.getString(0);
                Log.i(TAG,"names"+mCursor.getString(0));
                break;
            }
            String[] result=names.split("-");
            Log.i(TAG,"names"+result[0]);
            mCursor.close();
            return result;
        }
        //得到数据集的名字
        public List<String> Find() {
             List<String> result=new ArrayList<>();
             int i=0;
             Log.i(TAG, "result");
             Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, new String[]{NAME}, null,null,null,null,null);
             Log.i(TAG, "num:"+mCursor.getCount());
             while(mCursor.moveToNext()) {
                 result.add(mCursor.getString(0));
                 Log.i(TAG, "result"+mCursor.getString(0));
             }
             mCursor.close();
             return result;
         }
    //得到数据集的名字 ceshi 可删
    public void find() {
        openDataBase();
        List<String> result=new ArrayList<>();
        int i=0;
        Log.i(TAG, "result");
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, new String[]{NAME}, null,null,null,null,null);
        Log.i(TAG, "num:"+mCursor.getCount());
        while(mCursor.moveToNext()) {
            result.add(mCursor.getString(0));
            Log.i(TAG, "result"+mCursor.getString(0));
        }
        mCursor.close();
        closeDataBase();
        return;
    }
}