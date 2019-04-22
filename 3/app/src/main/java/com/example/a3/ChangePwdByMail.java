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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3.service.WebService;

public class ChangePwdByMail extends AppCompatActivity {
    private Button buttonconfirm;
    private TextView emailsend;
    private String info,mailString,spwd1,spwd2;
    private ProgressDialog dialog;
    private EditText pwd1,pwd2;
    private static Handler handler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd_by_mail);
        Intent intent = getIntent();
        mailString = intent.getStringExtra("mail");
        emailsend=(TextView)findViewById(R.id.MODIFY_PWD) ;
        emailsend.setText(mailString);
        pwd1=(EditText) findViewById(R.id.pwd1) ;
        spwd1=pwd1.getText().toString();
        pwd2=(EditText) findViewById(R.id.pwd2) ;
        spwd2=pwd2.getText().toString();
        buttonconfirm = (Button)findViewById(R.id.confirm);
        buttonconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spwd1=pwd1.getText().toString();
                spwd2=pwd2.getText().toString();
                if (spwd1.equals(spwd2)){
                    Log.i("info","spwd1= "+spwd1 +" spwd2 = "+spwd2);
                    // Start sign_inActivity
                    dialog = new ProgressDialog(ChangePwdByMail.this);
                    dialog.setTitle("Reset");
                    dialog.setMessage("Waiting...");
                    dialog.setCancelable(false);
                    dialog.show();
                    // 创建子线程，分别进行Get和Post传输
                    new Thread(new MyThread()).start();
                }
                else{
                    Toast toast= Toast.makeText(ChangePwdByMail.this, "The two passwords you typed do not match", Toast.LENGTH_SHORT);
                }
            }
        });
    }
    public class MyThread implements Runnable {
        @Override
        public void run() {
            info = WebService.executeHttpGet("mailpwd","?mail="+ mailString+"&password="+spwd1);
            // Log.i("info","hello");
            if (info!=null) Log.i("info",info);
            // info="true";
            // dialog.dismiss();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // 最好返回一个固定键值，根据键值判断是否登陆成功，有键值就保存该info跳转，没键值就是错误信息直接toast
                    dialog.dismiss();
                    Toast toast;
                    // Log.i("info",info);
                    if (info !=null && info.equals("true"))
                    {
                        toast= Toast.makeText(ChangePwdByMail.this, "Succeed", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(ChangePwdByMail.this, log_in.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        toast= Toast.makeText(ChangePwdByMail.this, "Failed", Toast.LENGTH_SHORT);
                    }
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }
    }
}
