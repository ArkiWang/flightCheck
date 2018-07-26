package com.example.yueli.flightcheck;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yueli.flightcheck.Adapter.CityListAdapter;
import com.example.yueli.flightcheck.Adapter.SearchListAdapter;
import com.example.yueli.flightcheck.Bean.Airport;
import com.example.yueli.flightcheck.CallBackInterface.FilterListener;
import com.example.yueli.flightcheck.Util.ApplicationUtil;
import com.example.yueli.flightcheck.Util.REQUEST_INT;

import java.util.ArrayList;
import java.util.List;

import static com.example.yueli.flightcheck.Bean.Airport_.kind;

public class AirportSelectActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private ListView listView;
    private SearchView searchView;
    private Intent intent;
    private List<String>dynamicList;
    private SearchListAdapter searchListAdapter;
    private RadioGroup radioGroup;

    //String activity,fragment,subFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        dynamicList=new ArrayList<>();
        radioGroup=findViewById(R.id.check_radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        //dynamicList.addAll(CityListAdapter.cityList);
        ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        List<Airport>airports=applicationUtil.airportBox.find(kind,0);
        for(Airport a:airports)
            dynamicList.add(a.name);
        listView=findViewById(R.id.city_list);
        listView.setOnItemClickListener(onItemClickListener);
        intent=getIntent();
        setData();
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
    private void setData() {
        // catch callback
        searchListAdapter = new SearchListAdapter(dynamicList, this, new FilterListener() {
            // 回调方法获取过滤后的数据
            public void getFilterData(List<String> list) {
                Log.e("TAG", "接口回调成功");
                Log.e("TAG", list.toString());
                setItemClick(list);
            }
        });
        listView.setAdapter(searchListAdapter);
    }

    protected void setItemClick(final List<String> filter_lists) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
             sendCallback(filter_lists,position);
            }
        });
    }
    private void sendCallback(List<String>list,int position){
        switch (intent.getStringExtra("class")){
            case "check":
                sendCity(intent.getStringExtra("position"),list.get(position));
                finish();
                break;
                    /*case "pickup":
                        Intent intent=new Intent();
                        intent.putExtra("airport",filter_lists.get(position));
                        setResult(REQUEST_INT.PICKUP_MSG_AIRPORT,intent);
                        finish();
                        break;*/
            case "recv":
                Intent intent1=new Intent();
                intent1.putExtra("airport",list.get(position));
                setResult(REQUEST_INT.RECV_XCHG_AIRPORT,intent1);
                finish();
                break;
        }
    }
    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            sendCity(intent.getStringExtra("position"), dynamicList.get(position));
            sendCallback(dynamicList,position);
            finish();
        }
    };
    private void setSearchView(SearchView searchView) {
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(onQueryTextListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_search_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.bar_city_search).getActionView();
        setSearchView(searchView);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }


    private SearchView.OnQueryTextListener onQueryTextListener=new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            /*if (searchView != null) {
                // 得到输入管理对象
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    if(CityListAdapter.cityList.contains(query)) {
                        if (!dynamicList.contains("历史查询"))
                            dynamicList.add(0, "历史查询");
                        dynamicList.add(1, query);
                        cityListAdapter.notifyDataSetChanged();
                        sendCity(intent.getStringExtra("position"), query);
                        finish();
                    }
                }
                //searchView.clearFocus(); // 不获取焦点
            }*/
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(searchListAdapter != null){
                searchListAdapter.getFilter().filter(newText);
            }
            return false;
        }
    };
    public void sendCity(String position,String city){
        Intent intent=null;
        if(position.equals("left"))
            intent=new Intent("left_city");
        else if(position.equals("right"))
            intent=new Intent("right_city");
        if(intent!=null) {
            intent.putExtra("city",city);
            sendBroadcast(intent);
            finish();
        }else
            Toast.makeText(this,"error!",Toast.LENGTH_SHORT);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        List<Airport>airports;
        switch (checkedId){
            case R.id.civil_city_tab:
                dynamicList.clear();
                airports=applicationUtil.airportBox.find(kind,0);
                for(Airport a:airports)
                    dynamicList.add(a.name);
                searchListAdapter.notifyDataSetChanged();
                break;
            case R.id.national_city_tab:
                dynamicList.clear();
                airports=applicationUtil.airportBox.find(kind,1);
                for(Airport a:airports)
                    dynamicList.add(a.name);
                searchListAdapter.notifyDataSetChanged();
                break;
        }
    }
}
