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

public class ModifyPwd extends AppCompatActivity {
    private final String PREFS_NAME = "MyLog";
    private EditText Textoldpwd,Textnewpwd1,Textnewpwd2;
    private String username,oldpwd,newpwd1,newpwd2,info;
    private Button mButton;
    private ProgressDialog dialog;
    private static Handler handler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);

        Textoldpwd=(EditText)findViewById(R.id.oldpwd);
        Textnewpwd1=(EditText)findViewById(R.id.newpwd1);
        Textnewpwd2=(EditText)findViewById(R.id.newpwd2);
        getUserInfo();
        mButton = (Button)findViewById(R.id.buttonpwd);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newpwd1=Textnewpwd1.getText().toString();
                newpwd2=Textnewpwd2.getText().toString();
                if (oldpwd.equals(Textoldpwd.getText().toString()) && newpwd1.equals(newpwd2))
                {
                    Toast.makeText(ModifyPwd.this, "0.0", Toast.LENGTH_SHORT).show();
                    // Start sign_inActivity
                    dialog = new ProgressDialog(ModifyPwd.this);
                    dialog.setTitle("Change Password");
                    dialog.setMessage("connecting...");
                    dialog.setCancelable(false);
                    dialog.show();
                    // 创建子线程，分别进行Get和Post传输
                    new Thread(new MyThread()).start();
                }
                else
                {
                     Toast.makeText(ModifyPwd.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getUserInfo(){
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        username=userInfo.getString("username", " ");
        oldpwd=userInfo.getString("password", " ");
        return;
    }
    public class MyThread implements Runnable {
        @Override
        public void run() {
            Log.i("info","name= "+username);
            info = WebService.executeHttpGet("changeinfo","?name="+username+"&oldpwd="+oldpwd+"&newpwd="+newpwd1);
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
                    if (info !=null && info.equals("true"))
                    {
                        toast= Toast.makeText(ModifyPwd.this, "Succeed", Toast.LENGTH_SHORT);
                        clearUserInfo();
                        Intent intent = new Intent(ModifyPwd.this, log_in.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        toast= Toast.makeText(ModifyPwd.this, "Failed", Toast.LENGTH_SHORT);
                    }
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }
    }
    private void clearUserInfo(){
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        editor.clear();
        editor.commit();
    }
}
