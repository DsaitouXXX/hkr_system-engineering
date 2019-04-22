package com.example.a3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.a3.model.message;
import com.example.a3.model.trashbin;
import com.example.a3.service.SqliteDBHelper;
import com.example.a3.service.SqliteMessage;

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

import static android.content.Context.MODE_PRIVATE;

public class fragment_message extends Fragment {
    private final String PREFS_NAME = "MyLog";
    private ListView list_message;
    private String area;
    private ProgressDialog dialog;
    private SqliteDBHelper db;
    private List<message> messages=new ArrayList<>();
    private final String url_login=" ";
    private final String url_list=" ";
    // private ArrayAdapter adapter;
   // private MyAdapter adapter;
    public fragment_message() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        db= new SqliteDBHelper(this.getContext());
        list_message = (ListView) view.findViewById(R.id.message_list);
        area=getUserArea();
        final SqliteMessage db0=new SqliteMessage(this.getContext());
        messages=db0.get_all();
        if (messages!=null){
            ToSetAdapter();
        }
        else
        {
            List<trashbin> trashbin=db.get_all();
            if (trashbin!=null){
                db0.add(trashbin,area);
                messages=db0.get_all();
                ToSetAdapter();
            }
            else {
                SetDialog();
                new MyAsyncTask().execute(url_login,url_list);
            }
        }
        list_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                                                    Object o = list_message.getItemAtPosition(position);
                                                    Log.i("OnClick", "position = " + position);
                                                    db0.UpdateFlag(messages.get(position).getId());
                                                    messages=db0.get_all();
                                                    ToSetAdapter();
                                                    JumpTo(position);
                                                }
                                            }
        );
        return view;
    }
    private String getUserArea(){
        SharedPreferences userInfo = this.getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return userInfo.getString("area", " ");

    }
    private void ToSetAdapter(){
        messageAdapter adapter = new messageAdapter(messages,this.getContext());
        list_message.setAdapter(adapter);
    }
    private void SetDialog(){
        dialog = new ProgressDialog(this.getContext());
        dialog.setTitle("Trash list");
        dialog.setMessage("loading...");
        dialog.setCancelable(false);
        dialog.show();

    }
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
            db.add(trashbin);
            ToSetAdapter();
            dialog.dismiss();
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
    private void JumpTo(int position){
        Intent intent = new Intent(this.getContext(), MessageShow.class);
        intent.putExtra("name",messages.get(position).getName());
        intent.putExtra("id",messages.get(position).getId());
        intent.putExtra("location",messages.get(position).getLocation());
        intent.putExtra("per",messages.get(position).getPer());
        startActivity(intent);
    }
}
