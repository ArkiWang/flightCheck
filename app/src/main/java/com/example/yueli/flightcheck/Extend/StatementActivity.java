package com.example.yueli.flightcheck.Extend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yueli.flightcheck.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class StatementActivity extends AppCompatActivity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);
        tv= (TextView) findViewById(R.id.state_tv1);
        initLv();
        setTitle("隐私声明");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initLv() {
        BufferedReader br=null;
        try {
            InputStream is=getResources().openRawResource(R.raw.privacystatement);
            InputStreamReader isr=new InputStreamReader(is,"UTF-8");
            br=new BufferedReader(isr);
            String ln=null;
            while((ln=br.readLine())!=null){
                tv.append("     "+ln+"\r\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

