package com.example.a3;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.a3.model.trashbin;
import com.example.a3.service.SqliteDBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class trashlist extends AppCompatActivity {
    private ListView list_trash;
    // private ArrayAdapter adapter;
    private MyAdapter adapter;
   // private ArrayList month;
    private ProgressDialog dialog;
    private Button mButton_refresh;
    private boolean flag;
    private final String url_login=" ";
    private final String url_list=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trashbin_list);
        list_trash = (ListView) findViewById(R.id.trash);
        //month=new ArrayList<String>();
        /*for(int i=1;i<=12;i++){
            month.add(i+"");
        }*/
        //sendPost();
       /* adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,month);
        list_trash.setAdapter(adapter);*/
        flag=false;
        SqliteDBHelper db = new SqliteDBHelper(this);
        List<trashbin> trashbin=db.get_all();
        // List<trashbin> trashbin=null;
        if (trashbin!=null){
            adapter = new MyAdapter(trashbin,trashlist.this);
            list_trash.setAdapter(adapter);
        }
        else{
            SetDialog();
            new MyAsyncTask().execute(url_login,url_list);
        }

        mButton_refresh = (Button)findViewById(R.id.refreshlist);
        mButton_refresh.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            SetDialog();
            new MyAsyncTask().execute(url_login,url_list);
        }
        });
    }
    private void SetDialog(){
        flag=true;
        dialog = new ProgressDialog(trashlist.this);
        dialog.setTitle("Trash list");
        dialog.setMessage("loading...");
        dialog.setCancelable(false);
        dialog.show();

    }

    /**
     *  内部类
     *  AsyncTask是android提供的轻量级的异步类，使用简单
     *  < >中的第一个数据类型：doInBackground()传入的数据类型
     *  < >中的第二个数据类型：onProgressUpdate()传入的数据类型，这个方法大多用来更新进度条
     *  < >中的第三个数据类型：onPostExecute()传入的数据类型
     */
    private class MyAsyncTask extends AsyncTask<String, Void, List<trashbin>> {

        @Override
        protected List<trashbin> doInBackground(String... params) {
            String token = getJsonToken(params[0]);
            List<trashbin> trashbin = getJsonData(params[1],token);
            return trashbin;
        }

        @Override
        protected void onPostExecute(List<trashbin>trashbin) {
            super.onPostExecute(trashbin);
            if (flag) {
                dialog.dismiss();
                flag=false;
            }
            SqliteDBHelper db = new SqliteDBHelper(trashlist.this);
            db.add(trashbin);
            adapter = new MyAdapter(trashbin,trashlist.this);
            list_trash.setAdapter(adapter);
        }
        //根据url获取token
        public String getJsonToken(String url_string){
            String token="";
            try {
                URL url = new URL(url_string);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();

                jsonParam.put("username", " ");
                jsonParam.put("pwd", " ");

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();
                String jsonString=readStream(conn.getInputStream());
                JSONObject object;
                object = new JSONObject(jsonString);
                token = object.optString("token");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return token;
        }
        //根据url获取json数据，返回集合
        public List<trashbin> getJsonData(String url_string , String token) {
            List<trashbin> trashbins = new ArrayList<>();
            try {
                //String jsonString = readStream(new URL(url).openStream());
                URL url = new URL(url_string);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization",token);
                conn.setDoOutput(true);
                conn.setDoInput(true);


                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                String jsonString=readStream(conn.getInputStream());
                JSONObject object;
                trashbin trashbin;
                object = new JSONObject(jsonString);
                JSONArray jsonArray = object.getJSONArray("deviceList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    trashbin = new trashbin();
                    object = jsonArray.getJSONObject(i);
                    trashbin.setName(object.getString("Name"));
                    trashbin.setLocation(object.getString("District"));
                    trashbin.setPer(object.getString("Percentage"));
                    trashbin.setId(object.getString("Id"));
                    trashbins.add(trashbin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Log.i("info","finished");
            return trashbins;
        }

        public String readStream(InputStream in) {
            InputStreamReader isb;
            String result = "";
            try {
                String line = "";
                isb = new InputStreamReader(in, "UTF-8");
                BufferedReader br = new BufferedReader(isb);
                while ((line = br.readLine()) != null) {
                    result += line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }


}


