package com.example.a3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a3.service.WebService;

public class ModifyEmail extends AppCompatActivity {
    private final String PREFS_NAME = "MyLog";
    private EditText email_get,code;
    private Button msend,mverify;
    private ProgressDialog dialog;
    private String info,email;
    private static Handler handler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_email);
        email_get=(EditText)findViewById(R.id.enetr_new_emailadd);
        code=(EditText)findViewById(R.id.emailadd_verify_code);
        msend = (Button)findViewById(R.id.pwd_send);
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign_inActivity
                dialog = new ProgressDialog(ModifyEmail.this);
                dialog.setTitle("Sending Email");
                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                dialog.show();
                // 创建子线程，分别进行Get和Post传输
                new Thread(new MyThread1()).start();
            }
        });

        mverify = (Button)findViewById(R.id.emailadd_verify);
        mverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign_inActivity
                dialog = new ProgressDialog(ModifyEmail.this);
                dialog.setTitle("Verify Code");
                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                dialog.show();
                // 创建子线程，分别进行Get和Post传输
                new Thread(new MyThread2()).start();
            }
        });
    }

    public class MyThread1 implements Runnable {
        @Override
        public void run() {
            email=email_get.getText().toString();
            info = WebService.executeHttpGet("sendmail","?mail="+email);
            // Log.i("info","hello");
            if (info!=null) Log.i("info",info);
            // info="true";
            dialog.dismiss();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast toast;
                    toast= Toast.makeText(ModifyEmail.this, "Failed", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }
    }

    public class MyThread2 implements Runnable {
        @Override
        public void run() {

            info = WebService.executeHttpGet("changemail","?mail="+ email+"&code="+code.getText().toString()+"&name="+getUsername());
            if (info!=null) Log.i("info",info);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // 最好返回一个固定键值，根据键值判断是否登陆成功，有键值就保存该info跳转，没键值就是错误信息直接toast
                    dialog.dismiss();
                    Toast toast;
                    // Log.i("info",info);
                    if (info !=null && info.equals("true"))
                    {
                        toast= Toast.makeText(ModifyEmail.this, "Succeed", Toast.LENGTH_SHORT);
                        finish();
                    }
                    else
                    {
                        toast= Toast.makeText(ModifyEmail.this, "Failed", Toast.LENGTH_SHORT);
                    }
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }
    }
    private String getUsername(){
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return userInfo.getString("username" , " ");

    }
}
