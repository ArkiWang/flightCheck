package com.example.yueli.flightcheck.Extend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.yueli.flightcheck.R;

public class AboutActivity extends AppCompatActivity {
    private TextView t1, t2, t3, t4, t5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        t1 = (TextView) findViewById(R.id.main_tv1);
        t2 = (TextView) findViewById(R.id.main_tv2);
        t3 = (TextView) findViewById(R.id.main_tv3);
        t4 = (TextView) findViewById(R.id.main_tv4);
        t5 = (TextView) findViewById(R.id.main_tv5);
        setTitle("关于");
        t1.setText("版本号:V1.1.1");
        t2.setText("链接:www.xxx.com");
        t3.setText("服务商:中国民航大学实训小分队");
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, AgreementActivity.class);
                startActivity(intent);
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, StatementActivity.class);
                startActivity(intent);
            }
        });
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
}
