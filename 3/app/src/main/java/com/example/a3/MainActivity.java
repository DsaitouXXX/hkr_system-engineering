package com.example.a3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Fragment
    private Fragment fragment_home;
    private Fragment fragment_message;

    //底端菜单栏LinearLayout
    private LinearLayout linear_home;
    private LinearLayout linear_message;

    //底端菜单栏Imageview
    private ImageView iv_home;
    private ImageView iv_message;

    //底端菜单栏textView
    private TextView tv_home;
    private TextView tv_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    //初始化各个控件
    InitView();

    //初始化点击触发事件
    InitEvent();

    //初始化Fragment
    InitFragment(1);

    //将第一个图标设置为选中状态
        iv_home.setImageResource(R.drawable.home_select);
        tv_home.setTextColor(getResources().getColor(R.color.colorTextViewPress));
}

    private void InitView(){

        linear_home = (LinearLayout) findViewById(R.id.line1);
        linear_message = (LinearLayout) findViewById(R.id.line2);

        iv_home = (ImageView) findViewById(R.id.ic_1);
        iv_message = (ImageView) findViewById(R.id.ic_2);

        tv_home = (TextView) findViewById(R.id.textview_1);
        tv_message = (TextView) findViewById(R.id.textview_2);

    }

    private void InitFragment(int index){
        //v4包下的Fragment，使用getSupportFragmentManager获取
        FragmentManager fragmentManager = getSupportFragmentManager();
        //启动事务
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

        //将所有的Fragment隐藏
        hideAllFragment(transaction);
        switch (index){
            case 1:
                if (fragment_home== null){
                    fragment_home = new fragment_home();
                    transaction.add(R.id.frame_content,fragment_home);
                }
                else{
                    transaction.show(fragment_home);
                }
                break;

            case 2:
                if (fragment_message== null){
                    fragment_message = new fragment_message();
                    transaction.add(R.id.frame_content,fragment_message);
                }
                else{
                    transaction.show(fragment_message);
                }
                break;

        }
        //提交事务
        transaction.commit();
    }
    private void InitEvent(){
        //设置LinearLayout监听
        linear_home.setOnClickListener(this);
        linear_message.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        //每次点击之后，将所有的ImageView和TextView设置为未选中
        restartButton();
        //再根据所选，进行跳转页面，并将ImageView和TextView设置为选中
        switch(view.getId()){
            case R.id.line1:
                iv_home.setImageResource(R.drawable.home_select);
                tv_home.setTextColor(getResources().getColor(R.color.colorTextViewPress));
                InitFragment(1);
                break;

            case R.id.line2:
                iv_message.setImageResource(R.drawable.message_select);
                tv_message.setTextColor(getResources().getColor(R.color.colorTextViewPress));
                InitFragment(2);
                break;

        }
    }

    //隐藏所有的Fragment
    private void hideAllFragment(android.support.v4.app.FragmentTransaction transaction){
        if (fragment_home!= null){
            transaction.hide(fragment_home);
        }

        if (fragment_message!= null){
            transaction.hide(fragment_message);
        }
        // transaction.commit();
    }

    //重新设置ImageView和TextView的状态
    private void restartButton(){
        //设置为未点击状态
        iv_home.setImageResource(R.drawable.home_normal);
        iv_message.setImageResource(R.drawable.message_normal);
        //设置为灰色
        tv_home.setTextColor(getResources().getColor(R.color.colorTextViewNormal));
        tv_message.setTextColor(getResources().getColor(R.color.colorTextViewNormal));
    }


}
