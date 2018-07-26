package com.example.yueli.flightcheck;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yueli.flightcheck.Adapter.SearchListAdapter;
import com.example.yueli.flightcheck.Bean.Flight;
import com.example.yueli.flightcheck.CallBackInterface.FilterListener;
import com.example.yueli.flightcheck.Util.ApplicationUtil;
import com.example.yueli.flightcheck.Util.REQUEST_INT;

import java.util.ArrayList;
import java.util.List;

public class FlightSelectActivity extends AppCompatActivity {
    EditText input;
    ListView listView;
    List<String> list=new ArrayList<>();
    boolean isFilter;
    SearchListAdapter searchListAdapter=null;
    Intent getIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setViews();// 控件初始化
       // setData();// 给listView设置searchListAdapter
        init();
        setListeners();// 设置监听
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIntent=getIntent();
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

    private void init() {
        ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        List<Flight>flights=applicationUtil.flightBox.getAll();
        for(Flight flight:flights){
            list.add(flight.flno);
        }
        searchListAdapter = new SearchListAdapter(list, this, new FilterListener() {
            // 回调方法获取过滤后的数据
            public void getFilterData(List<String> list) {
                Log.e("TAG", "接口回调成功");
                Log.e("TAG", list.toString());
                setItemClick(list);
            }
        });
        listView.setAdapter(searchListAdapter);
    }
   /* private void setData() {
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj.equals("ok")){
                    searchListAdapter.notifyDataSetChanged();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
                Message message=new Message();
                message.obj="ok";
                handler.sendMessage(message);
            }
        }).start();
        // 这里创建searchListAdapter的时候，构造方法参数传了一个接口对象，这很关键，回调接口中的方法来实现对过滤后的数据的获取
        searchListAdapter = new SearchListAdapter(list, this, new FilterListener() {
            // 回调方法获取过滤后的数据
            public void getFilterData(List<String> list) {
                Log.e("TAG", "接口回调成功");
                Log.e("TAG", list.toString());
                setItemClick(list);
            }
        });
        listView.setAdapter(searchListAdapter);
    }
*/
    /**
     * 给listView添加item的单击事件
     * @param filter_lists  过滤后的数据集
     */
    protected void setItemClick(final List<String> filter_lists) {
        final ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                Intent intent;
                switch (getIntent.getIntExtra("request",-1)){
                    case REQUEST_INT.FRAG_FLNO_REQUEST:
                        intent=new Intent();
                        intent.putExtra("BookingJsonBean",filter_lists.get(position));
                        setResult(REQUEST_INT.FRAG_FLNO_REQUEST,intent);
                        finish();
                        break;
                    case REQUEST_INT.FRAG_BOOKING://添加订阅
                        intent=new Intent();
                        intent.putExtra("booking",filter_lists.get(position));
                        setResult(REQUEST_INT.FRAG_BOOKING,intent);
                        finish();
                        break;

                }
            }
        });
    }
  /*
    private void initData() {
        ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
           for(Flight flight:applicationUtil.flightBox.getAll()){
            list.add(flight.flno);
        List<FLNOJsonBean.ListItem>fList=applicationUtil.getCheckedFLNOData("-","2017-06-03");
        if(fList!=null) {
            for (FLNOJsonBean.ListItem i : fList) {
                list.add(i.flightID);
            }
        }
    }
*/
    private void setListeners() {
        // 没有进行搜索的时候，也要添加对listView的item单击监听
        setItemClick(list);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 如果searchListAdapter不为空的话就根据编辑框中的内容来过滤数据
                if(searchListAdapter != null){
                    searchListAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * 控件初始化
     */
    private void setViews() {
        input = (EditText) findViewById(R.id.search_input);// EditText控件
        listView = (ListView)findViewById(R.id.search_list);// ListView控件
    }
}
