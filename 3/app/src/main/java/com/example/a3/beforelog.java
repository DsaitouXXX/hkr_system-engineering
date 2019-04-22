package com.example.a3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class beforelog extends AppCompatActivity {
    private final String PREFS_NAME = "MyLog";
    private Button mButton_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beforelog);
        mButton_login = (Button)findViewById(R.id.button_login);
        mButton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","get info= "+getUserInfo());
                if(getUserInfo()==0) {
                    // Start sign_inActivity
                    Intent intent = new Intent(beforelog.this, log_in.class);
                    // Intent intent = new Intent(beforelog.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(beforelog.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private int getUserInfo(){
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return userInfo.getInt("exist", 0);//读取age

    }
}
