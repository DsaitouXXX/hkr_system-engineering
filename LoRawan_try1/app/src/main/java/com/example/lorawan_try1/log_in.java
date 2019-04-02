package com.example.lorawan_try1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lorawan_try1.service.WebService;

public class log_in extends AppCompatActivity {
    private String info;
    private ProgressDialog dialog;
    private static Handler handler= new Handler();
    private Button mButton_login1;
    private EditText login_mail,login_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        login_mail=(EditText)findViewById(R.id.text_email);
        login_pwd=(EditText)findViewById(R.id.text_pwd);
        mButton_login1 = (Button)findViewById(R.id.button_login1);
        mButton_login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign_inActivity
                dialog = new ProgressDialog(log_in.this);
                dialog.setTitle("提示");
                dialog.setMessage("正在登陆，请稍后...");
                dialog.setCancelable(false);
                dialog.show();
                // 创建子线程，分别进行Get和Post传输
                new Thread(new MyThread()).start();
            }
        });
    }
    public class MyThread implements Runnable {
        @Override
        public void run() {
            info = WebService.executeHttpGet("login",login_mail.getText().toString(), login_pwd.getText().toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // 最好返回一个固定键值，根据键值判断是否登陆成功，有键值就保存该info跳转，没键值就是错误信息直接toast
                    dialog.dismiss();
                    Toast toast;

                    if (info !=null && info.equals("true"))
                    {
                        toast= Toast.makeText(log_in.this, "登陆成功", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(log_in.this, perinfo.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        toast= Toast.makeText(log_in.this, "登陆失败", Toast.LENGTH_SHORT);
                    }
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }
    }
}
