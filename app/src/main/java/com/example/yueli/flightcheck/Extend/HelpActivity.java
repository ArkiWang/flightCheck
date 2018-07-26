package com.example.yueli.flightcheck.Extend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yueli.flightcheck.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpActivity extends AppCompatActivity {
    ListView lvMsg,lvMsg2;
    EditText edtTxtMsg;
    Button btnSend;
    ArrayList<MsgInfo> mArray;
    MsgAdapter mAdapter;
    List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        lvMsg=(ListView)findViewById(R.id.service_main_lv_msg);
        lvMsg2=(ListView) findViewById(R.id.service_main_lv2_msg);
        edtTxtMsg=(EditText)findViewById(R.id.service_main_msg);
        btnSend=(Button)findViewById(R.id.service_main_send);
        initLv();

        mArray=new ArrayList<MsgInfo>();
        mArray.add(new MsgInfo("你好！","客服",R.drawable.head_2));
        mArray.add(new MsgInfo("请问需要什么帮助？","客服",R.drawable.head_2));
        mAdapter=new MsgAdapter(HelpActivity.this,mArray);

        lvMsg.setAdapter(mAdapter);

        lvMsg2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map item=data.get(position);
                String str=null;
                switch(position){
                    case 1:
                        str="在\"个人中心\"->\"我的资料\"中修改个人信息。";
                        break;
                    case 2:
                        str="在\"航班查询\"中按航班号或城市名查询航班,再点击查询结果,即可查看航班详细信息。";
                        break;
                    case 3:
                        str="在\"订阅\"中点击右上角的\"+\"添加订阅航班,通过航班号或城市名查询想要订阅的航班,在查询结果的右侧点击\"关注\"即订阅成功。";
                        break;
                    case 4:
                        str="在\"接机消息\"->\"我的消息\"中查看航班通知。";
                        break;
                    case 5:
                        str="在\"接机消息\"右上角中进行筛选。";
                        break;
                }
                if(position!=0){
                    //提问
                    String strMsg=item.get("tv1").toString();
                    mArray.add(new MsgInfo(strMsg,"我",R.drawable.head_1));
                    mAdapter.notifyDataSetChanged();
                    //回答
                    mArray.add(new MsgInfo("回答"+position+": "+str,"客服",R.drawable.head_2));
                    mAdapter.notifyDataSetChanged();
                }}
        });

        btnSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String strName="我";
                String strMsg=edtTxtMsg.getText().toString();
                edtTxtMsg.getText().clear();
                int iHead;
                iHead=R.drawable.head_1;
                mArray.add(new MsgInfo(strMsg,strName,iHead));
                mAdapter.notifyDataSetChanged();

                String str="我母鸡哦~";
                mArray.add(new MsgInfo("回答"+": "+str,"客服",R.drawable.head_2));
                mAdapter.notifyDataSetChanged();
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


    private void initLv() {
        for (int i = 0; i <=5; i++) {
            Map<String,Object> item =new HashMap();
            String str=null;
            switch(i){
                case 0:
                    str="您可能遇到的问题:";
                    break;
                case 1:
                    str="问题"+i+": "+"如何修改个人信息";
                    break;
                case 2:
                    str="问题"+i+": "+"如何查看航班详情";
                    break;
                case 3:
                    str="问题"+i+": "+"如何订阅航班";
                    break;
                case 4:
                    str="问题"+i+": "+"如何查看航班通知";
                    break;
                case 5:
                    str="问题"+i+": "+"如何查看符合某一条件的接机消息";
                    break;
            }
            item.put("tv1",str);
            data.add(item);}
        QueAdapter ba=new QueAdapter(this,data,R.layout.question_item);
        lvMsg2.setAdapter(ba);
    }
}
