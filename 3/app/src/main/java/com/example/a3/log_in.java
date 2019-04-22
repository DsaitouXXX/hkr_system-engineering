package com.example.a3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3.service.WebService;


public class log_in extends AppCompatActivity {
    private final String PREFS_NAME = "MyLog";
    private Spinner sp;
    private String info,username,pwd,title;
    private ProgressDialog dialog;
    private static Handler handler= new Handler();
    private TextView forgetpwd;
    private Button mButton_login1;
    private EditText login_name,login_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        sp = (Spinner) findViewById(R.id.title_choice);
        login_name=(EditText)findViewById(R.id.text_name);
        login_pwd=(EditText)findViewById(R.id.text_pwd);

        forgetpwd=(TextView)findViewById(R.id.forgetpwd);
        forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(log_in.this, sendmail.class);
                startActivity(intent);
            }
        });
        mButton_login1 = (Button)findViewById(R.id.button_login1);
        mButton_login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Start sign_inActivity
                    dialog = new ProgressDialog(log_in.this);
                    dialog.setTitle("Log in");
                    dialog.setMessage("connecting...");
                    dialog.setCancelable(false);
                    dialog.show();
                    // 创建子线程，分别进行Get和Post传输
                    new Thread(new MyThread()).start();
            }
        });


    }

    private void setUserInfo(){
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userInfo.edit()
                .putString("username", username)
                .putString("password", pwd)
                .putString("area",info)
                .putInt("exist",1)
                .apply();


    }
    private void log(){
        Intent intent = new Intent(log_in.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public class MyThread implements Runnable {
        @Override
        public void run() {
            username=login_name.getText().toString();
            pwd=login_pwd.getText().toString();
            title=sp.getSelectedItem().toString();
            info = WebService.executeHttpGet("login",username, pwd,title);
            // Log.i("info","hello");
            if (info!=null)Log.i("info",info);
            // info="true";
            dialog.dismiss();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // 最好返回一个固定键值，根据键值判断是否登陆成功，有键值就保存该info跳转，没键值就是错误信息直接toast
                    // dialog.dismiss();
                    Toast toast;
                    Log.i("info",info);
                    if ((info !=null && info.equals("false")) || info ==null)
                    {
                        toast= Toast.makeText(log_in.this, "Failed", Toast.LENGTH_SHORT);
                    }
                    else
                    {
                        toast= Toast.makeText(log_in.this, "Succeed", Toast.LENGTH_SHORT);
                        setUserInfo();
                        log();
                    }
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }
    }

    //  protected void onDes

}
