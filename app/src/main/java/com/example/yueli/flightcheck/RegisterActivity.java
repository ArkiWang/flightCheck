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
import android.widget.TimePicker;
import android.widget.Toast;

import com.aliyuncs.exceptions.ClientException;
import com.example.yueli.flightcheck.Bean.User;
import com.example.yueli.flightcheck.Bean.User_;
import com.example.yueli.flightcheck.Util.ApplicationUtil;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.yueli.flightcheck.Bean.User_.name;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText phone,user,authCode,pwd,rePwd;
    Button getAuthCode,register;
    TextView hint,hintName;
    boolean flag=false,isUserName=false;
    String code;
    public static boolean isCellphone(String str) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    private TextWatcher phoneWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            flag=isCellphone(s.toString());
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
            if(s!=null&&s.toString().equals(pwd.getText().toString())) {
               // register.setClickable(true);
                hint.setVisibility(View.GONE);
            }else {
                //register.setClickable(false);
                hint.setVisibility(View.VISIBLE);
            }
        }
    };
    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
    TextWatcher userNameWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           
        }

        @Override
        public void afterTextChanged(Editable s) {
            isUserName=ifUseable(s.toString());
            if(!isUserName)
                hintName.setVisibility(View.VISIBLE);
            else
                hintName.setVisibility(View.GONE);
        }
    };
 
    private boolean ifUseable(String  s){
        ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        List<User>userList= applicationUtil.userBox.find(name,s);
        if(userList.size()>0)
            return false;
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        phone=findViewById(R.id.phone);
        user=findViewById(R.id.register_username);
        user.addTextChangedListener(userNameWatcher);
        authCode=findViewById(R.id.auth_code);
        pwd=findViewById(R.id.register_pwd);
        rePwd=findViewById(R.id.register_pwd_repeat);
        getAuthCode=findViewById(R.id.get_auth_code);
        register=findViewById(R.id.register_submit);
        phone.addTextChangedListener(phoneWatcher);
        rePwd.addTextChangedListener(rePwdWatcher);
        register.setOnClickListener(this);
        getAuthCode.setOnClickListener(this);
        //register.setClickable(false);
        hint=findViewById(R.id.register_hint);
        getAuthCode.setClickable(false);
        hintName=findViewById(R.id.register_name_hint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("注册");
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
   public static String getAuthCode(){
        int count=6;
       StringBuffer sb = new StringBuffer();
       String str = "0123456789";
       Random r = new Random();
       for (int i = 0; i < count; i++) {
           int num = r.nextInt(str.length());
           sb.append(str.charAt(num));
           str = str.replace((str.charAt(num) + ""),"");
       }
       return sb.toString();
   }
    @Override
    public void onClick(View v) {
        final ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        switch (v.getId()) {
            case R.id.get_auth_code:
                if (applicationUtil.userBox.find(User_.phone, phone.getText().toString()).size() > 0)
                    showError("该手机已被注册！");
                else {
                    code = getAuthCode();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            applicationUtil.getHttpJsonData("http://172.20.10.6:8080/register?phone=" + phone.getText().toString() + "&auth=" + code);
                        }
                    }).start();
                    new TimeCount(15000,1000).start();
                }
                break;
            case R.id.register_submit:
                if (flag && code != null && code.equals(authCode.getText().toString())) {
                    User u = new User(user.getText().toString(), pwd.getText().toString(), phone.getText().toString());
                    long id = applicationUtil.userBox.put(u);
                    applicationUtil.ifLogin = true;
                    applicationUtil.userId = id;
                    finish();
                } else if (!flag) {
                     showError("手机号有错");
                } else if(user.getText().toString()!=null){
                    showError("用户名不能为空");
                }else {
                    if (authCode.getText().toString().length() > 0)
                        showError("验证码不正确");
                    else
                        showError("验证码不能为空");
                }
               break;
        }
    }
    class TimeCount extends CountDownTimer{

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
