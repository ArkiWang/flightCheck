package com.example.yueli.flightcheck;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.yueli.flightcheck.CallBackInterface.LogStateListener;
import com.example.yueli.flightcheck.Fragment.fragmentBooking;
import com.example.yueli.flightcheck.Fragment.fragmentCheck;
import com.example.yueli.flightcheck.Fragment.fragmentRecvMsg;
import com.example.yueli.flightcheck.Fragment.fragmentUser;
import com.example.yueli.flightcheck.Provider.PlusActionProvider;
import com.example.yueli.flightcheck.Service.NotificationService;
import com.example.yueli.flightcheck.Util.ApplicationUtil;
import com.example.yueli.flightcheck.Util.REQUEST_INT;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    FragmentManager fm;
    FragmentTransaction ft;
    fragmentCheck fc;
    fragmentRecvMsg frm;
    fragmentUser fu;
    fragmentBooking fb;
    RadioGroup mainGroup;
    RadioButton check,book,msg,user;
    boolean ifLogin;
    LogStateListener logStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ApplicationUtil appUtil=(ApplicationUtil)MainActivity.this.getApplication();
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE
            ,Manifest.permission.CAMERA};
            //验证是否许可权限
            for (String str : permissions) {
                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
        mainGroup=(RadioGroup)findViewById(R.id.main_rd_group);
        fc=new fragmentCheck();
        frm=new fragmentRecvMsg();
        fu=new fragmentUser();
        fb=new fragmentBooking();
        fm=getFragmentManager();
        change(fc);
        check=(RadioButton)findViewById(R.id.rd_menu_check);
        book=(RadioButton)findViewById(R.id.rd_menu_book);
        msg=(RadioButton)findViewById(R.id.rd_menu_msg);
        user=(RadioButton)findViewById(R.id.rd_menu_user);
        mainGroup.setOnCheckedChangeListener(this);

       /* if(appUtil.airportBox.getAll()==null||appUtil.airportBox.getAll().size()==0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    appUtil.getAirportJsonData("-");
                }
            }).start();
        }*/
        ifLogin=appUtil.ifLogin;
        logStateListener=new LogStateListener() {
            @Override
            public void getLogState(boolean ifLogin) {
                appUtil.ifLogin=ifLogin;
                check.setChecked(true);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, NotificationService.class);
        startService(intent);//start service*
    }

    public LogStateListener getLogStateListener(){return logStateListener;}


    private void change(Fragment f){
        ft=fm.beginTransaction();
        ft.replace(R.id.fragment_container,f);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mf=getMenuInflater();
        mf.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        check.setChecked(true);
      /*
       if(requestCode==REQUEST_INT.LOGIN_SUCCEED&&REQUEST_INT.LOGIN_SUCCEED==resultCode){
            mainGroup.check(check.getId());
        }*/
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        ApplicationUtil appUtil=(ApplicationUtil)MainActivity.this.getApplication();
        ifLogin=appUtil.ifLogin;
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        switch (checkedId){
            case R.id.rd_menu_check:
                change(fc);
                break;
            case R.id.rd_menu_book:
                if(ifLogin)
                    change(fb);
                else
                    startActivityForResult(intent, REQUEST_INT.LOGIN_SUCCEED);
                break;
            case R.id.rd_menu_msg:
                if(ifLogin)
                   change(frm);
                else
                    startActivityForResult(intent, REQUEST_INT.LOGIN_SUCCEED);
                break;
            case R.id.rd_menu_user:
                if(ifLogin)
                   change(fu);
                else
                    startActivityForResult(intent, REQUEST_INT.LOGIN_SUCCEED);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        applicationUtil.boxStore.close();
        applicationUtil.boxStore.deleteAllFiles();
        super.onDestroy();
    }
}
