package com.example.yueli.flightcheck;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yueli.flightcheck.Bean.User;
import com.example.yueli.flightcheck.Bean.User_;
import com.example.yueli.flightcheck.Util.ApplicationUtil;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener{
    EditText phone,authCode,pwd,REpwd;
    Button getAuthCode,submit;
    String code;
    TextView hint;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        phone=findViewById(R.id.forget_phone);
        authCode=findViewById(R.id.forget_auth_code);
        pwd=findViewById(R.id.forget_pwd);
        REpwd=findViewById(R.id.forget_pwd_repeat);
        getAuthCode=findViewById(R.id.forget_get_auth_code);
        getAuthCode.setOnClickListener(this);
        submit=findViewById(R.id.forget_submit);
        phone.addTextChangedListener(phoneWatcher);
        REpwd.addTextChangedListener(rePwdWatcher);
        submit.setOnClickListener(this);
        //submit.setClickable(false);
        getAuthCode.setClickable(false);
        hint=findViewById(R.id.forget_hint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("找回密码");
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
    private TextWatcher phoneWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            flag=RegisterActivity.isCellphone(s.toString());
            if(flag)
                getAuthCode.setClickable(true);
            else
                getAuthCode.setClickable(false );
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher rePwdWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(flag&&s!=null&&s.toString().equals(pwd.getText().toString())) {
                submit.setClickable(true);
                hint.setVisibility(View.GONE);
            }else {
                submit.setClickable(false);
                hint.setVisibility(View.VISIBLE);
            }
        }
    };
    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        final ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        switch (v.getId()){
            case R.id.forget_get_auth_code:
                if(applicationUtil.userBox.find(User_.phone,phone.getText().toString())==null)
                    showError("该手机未被注册！");
                else {
                    code = RegisterActivity.getAuthCode();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            applicationUtil.getHttpJsonData("http://172.20.10.6:8080/register?phone=" + phone.getText().toString() + "&auth=" + code);
                        }
                    }).start();
                    new TimeCount(15000,1000).start();
                }
                break;
            case R.id.forget_submit:
                if(flag&&code!=null&&code.equals(authCode.getText().toString())) {
                    User u = applicationUtil.userBox.query().equal(User_.phone,phone.getText().toString()).build().findFirst();
                    u.pwd=pwd.getText().toString();
                    long id=applicationUtil.userBox.put(u);
                    applicationUtil.ifLogin=true;
                    applicationUtil.userId=id;
                    finish();
                }else if(!flag) {
                    showError("手机号有错");
                }else {
                    if (authCode.getText().toString().length() > 0)
                        showError("验证码不正确");
                    else
                        showError("验证码不能为空");
                }
                break;
        }
    }
    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getAuthCode.setBackground(getDrawable(R.drawable.single_unselect));
            getAuthCode.setClickable(false);
            getAuthCode.setTextColor(getColor(R.color.border_clo));
            getAuthCode.setText("("+millisUntilFinished / 1000 +")"+"秒后再试");
        }

        @Override
        public void onFinish() {
            getAuthCode.setText("发送验证码");
            getAuthCode.setClickable(true);
            getAuthCode.setTextColor(getColor(R.color.white));
            getAuthCode.setBackground(getDrawable(R.drawable.single_select));
        }
    }
}
