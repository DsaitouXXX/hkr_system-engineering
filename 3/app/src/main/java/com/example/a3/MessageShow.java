package com.example.a3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageShow extends AppCompatActivity {
    private TextView name,location,fulllevel,id;
    private ImageView imgshow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_show);
        id = (TextView)findViewById(R.id.id0);
        name = (TextView)findViewById(R.id.name);
        location = (TextView)findViewById(R.id.location);
        fulllevel = (TextView)findViewById(R.id.fulllevel);
        imgshow = (ImageView)findViewById(R.id.imgshow);
        Intent intent = getIntent();
        if (intent!=null){
            id.setText("ID :"+intent.getStringExtra("id"));
            name.setText("NAME :"+intent.getStringExtra(" name"));
            location.setText("LOCATION :"+intent.getStringExtra("location"));
            fulllevel.setText("FULL LEVEL :"+intent.getStringExtra("per"));
            int x=Integer.parseInt(intent.getStringExtra("per"));
            if (x>80) imgshow.setImageResource(R.drawable.icons_levelfull_32);
            else if (x>=50 && x<80)  imgshow.setImageResource(R.drawable.icons_levelcomm_32);

        }
    }
}
