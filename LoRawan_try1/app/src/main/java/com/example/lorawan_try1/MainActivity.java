package com.example.lorawan_try1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mButton_signin;
    private Button mButton_signup;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mButton_signin = (Button)findViewById(R.id.button_signin);
        mButton_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign_inActivity
                Intent intent = new Intent(MainActivity.this, sign_in.class);
                startActivity(intent);
            }
        });

        mButton_signup = (Button)findViewById(R.id.button_signup);
        mButton_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign_inActivity
                Intent intent = new Intent(MainActivity.this, sign_up.class);
                startActivity(intent);
            }
        });
    }
}
