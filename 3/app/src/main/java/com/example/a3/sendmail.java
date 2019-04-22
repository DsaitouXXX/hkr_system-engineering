package com.example.a3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3.service.WebService;

public class sendmail extends AppCompatActivity {
    private Button buttonsend;
    private TextView emailsend;
    private String info;
    private ProgressDialog dialog;
    private static Handler handler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmail);
        emailsend=(TextView)findViewById(R.id.emailsend) ;
        buttonsend = (Button)findViewById(R.id.buttonsend);
        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign_inActivity
                dialog = new ProgressDialog(sendmail.this);
                dialog.setTitle("Sending");
                dialog.setMessage("Waiting...");
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
            info = WebService.executeHttpGet("sendmail","?mail="+emailsend.getText().toString());
            // Log.i("info","hello");
            if (info!=null) Log.i("info","before "+info);
            else  Log.i("info","hello");
            // info="true";
            // dialog.dismiss();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // 最好返回一个固定键值，根据键值判断是否登陆成功，有键值就保存该info跳转，没键值就是错误信息直接toast
                    dialog.dismiss();
                    Toast toast;
                    Log.i("info","run "+info);
                    if (info !=null && info.equals("true"))
                    {
                        toast= Toast.makeText(sendmail.this, "Succeed", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(sendmail.this, verify.class);
                        intent.putExtra("mail",emailsend.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        toast= Toast.makeText(sendmail.this, "Failed", Toast.LENGTH_SHORT);
                    }
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }
    }
}
