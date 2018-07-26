package com.example.yueli.flightcheck;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.yueli.flightcheck.Util.ApplicationUtil;

public class StartActivity extends AppCompatActivity {
   Button start;
   Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
       final ApplicationUtil appUtil=(ApplicationUtil)getApplication();
        new Thread(new Runnable() {
            @Override
            public void run() {
                appUtil.init();
               // appUtil.localData();
            }
        }).start();
        start=findViewById(R.id.start);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj.equals("ok")) {
                    Intent it = new Intent(getApplicationContext(), MainActivity.class);//启动MainActivity
                    startActivity(it);
                    finish();//关闭当前活动
                }
            }
        };
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=new Message();
                message.obj="ok";
                handler.sendMessage(message);
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    sleep(5000);//使程序休眠五秒
                    Message message=new Message();
                    message.obj="ok";
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
