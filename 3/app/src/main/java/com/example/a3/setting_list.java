package com.example.a3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class setting_list extends Activity {
    private final String PREFS_NAME = "MyLog";
    private Button mButton1,mButton2,mButton3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_list);

        mButton1 = (Button)findViewById(R.id.to_mod_pwd);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setting_list.this, ModifyPwd.class);
                startActivity(intent);
            }
        });

        mButton2 = (Button)findViewById(R.id.to_mod_emailadd);
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setting_list.this, ModifyEmail.class);
                startActivity(intent);
            }
        });

        mButton3 = (Button)findViewById(R.id.log_out);
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUserInfo();
                Intent intent = new Intent(setting_list.this, beforelog.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void clearUserInfo(){
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userInfo.edit().clear().commit();
    }
}
