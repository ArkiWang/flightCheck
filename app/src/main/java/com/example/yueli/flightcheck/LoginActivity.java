package com.example.yueli.flightcheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yueli.flightcheck.Bean.User;
import com.example.yueli.flightcheck.CallBackInterface.LogStateListener;
import com.example.yueli.flightcheck.Util.ApplicationUtil;
import com.example.yueli.flightcheck.Util.REQUEST_INT;

import io.objectbox.query.QueryBuilder;

import static com.example.yueli.flightcheck.Bean.User_.name;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button login;
    private EditText userName;
    private EditText password;
    TextView register,forgetPwd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox rem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button)findViewById(R.id.login);
        userName=(EditText)findViewById(R.id.user_name);
        password=(EditText)findViewById(R.id.user_pwd);
        login.setOnClickListener(this);
        register=findViewById(R.id.register);
        register.setOnClickListener(this);
        forgetPwd=findViewById(R.id.forget_pwd);
        forgetPwd.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("登录");
        rem=findViewById(R.id.rem_me);
        rememeberMe();
    }
    private void rememeberMe(){
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean flag=sharedPreferences.getBoolean("remember_password",false);
        if(flag){
            userName.setText(sharedPreferences.getString("Name",""));
            password.setText(sharedPreferences.getString("Password",""));
            rem.setChecked(true);
        }
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

    @Override
    public void onClick(View v) {
        final ApplicationUtil appUtil=(ApplicationUtil)LoginActivity.this.getApplication();
        switch (v.getId()){
            case R.id.login:
               /* final Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.obj.equals("ok")){
                            appUtil.ifLogin=true;
                            Intent intent=new Intent();
                            intent.putExtra("login","succeed");
                            setResult(REQUEST_INT.LOGIN_SUCCEED,intent);
                            finish();
                        }
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ifSucceed=appUtil.Login(userName.getText().toString(),password.getText().toString());
                        if(ifSucceed) {
                            Message message = new Message();
                            message.obj = "ok";
                            handler.sendMessage(message);
                        }
                    }
                }).start();*/
               editor=sharedPreferences.edit();
               String tempName=userName.getText().toString();
               String tempPass=password.getText().toString();
               QueryBuilder<User>userQueryBuilder=appUtil.userBox.query();
               User user=userQueryBuilder.equal(name,tempName).build().findFirst();
               if(user!=null){
                   if(user.pwd.equals(tempPass)){
                       appUtil.ifLogin=true;
                       appUtil.userId=user.id;
                       if(rem.isChecked()){
                           editor.putString("Name",tempName);
                           editor.putString("Password",tempPass);
                           editor.putBoolean("remember_password",true);
                       }else
                           editor.clear();
                       editor.apply();
                       finish();
                   }else
                       Toast.makeText(this,"密码有误",Toast.LENGTH_SHORT).show();
               }else
                   Toast.makeText(this,"用户名有误",Toast.LENGTH_SHORT).show();
                break;
            case R.id.register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.forget_pwd:
                Intent intent1=new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }
}
