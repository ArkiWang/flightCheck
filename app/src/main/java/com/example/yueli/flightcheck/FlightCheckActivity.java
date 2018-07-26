package com.example.yueli.flightcheck;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yueli.flightcheck.Adapter.FlightInfoListViewAdapter;
import com.example.yueli.flightcheck.Adapter.FlightInfoViewPagerAdapter;
import com.example.yueli.flightcheck.Bean.Airport;
import com.example.yueli.flightcheck.Bean.Flight;
import com.example.yueli.flightcheck.Bean.FlightDetail;
import com.example.yueli.flightcheck.Bean.FlightDetail_;
import com.example.yueli.flightcheck.Bean.Flight_;
import com.example.yueli.flightcheck.Util.ApplicationUtil;
import com.example.yueli.flightcheck.model.FlightInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import io.objectbox.query.QueryBuilder;

import static com.example.yueli.flightcheck.Bean.Airport_.name;
import static com.example.yueli.flightcheck.Bean.FlightDetail_.FlightId;
import static com.example.yueli.flightcheck.Bean.FlightDetail_.fromId;
import static com.example.yueli.flightcheck.Bean.FlightDetail_.toId;
import static com.example.yueli.flightcheck.Bean.Flight_.flno;
import static com.example.yueli.flightcheck.Bean.Flight_.id;


public class FlightCheckActivity extends AppCompatActivity {
    private ViewPager mPager;
    private List<View> mPagerList;
    private List<FlightInfo> mDatas;
    private LinearLayout mLlDot;
    private LayoutInflater inflater;
    Handler handler;
    /**
     * 总的页数
     */
    private int pageCount;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 15;
    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_check);
        mPager = (ViewPager) findViewById(R.id.flight_info_viewPager);
        mLlDot = (LinearLayout) findViewById(R.id.ll_dot);
        mPagerList = new ArrayList<View>();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj.equals("ok"))
                    setList();
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                initDatas();
                Message message=new Message();
                message.obj="ok";
                handler.sendMessage(message);
            }
        }).start();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("airport"));
    }
    private void setList(){
        inflater = LayoutInflater.from(this);
        // 总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        for (int i = 0; i < pageCount; i++) { // 每个页面都是inflate出一个新实例
            View view = inflater.inflate(R.layout.flight_info_list, mPager, false);
            ListView listView=view.findViewById(R.id.flight_info_list);
            FlightInfoListViewAdapter flightInfoListViewAdapter=new FlightInfoListViewAdapter(this, mDatas, i, pageSize);
            listView.setAdapter(flightInfoListViewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * pageSize;
                    Intent intent=new Intent(FlightCheckActivity.this,FlightCheckDetailActivity.class);
                    intent.putExtra("flno",mDatas.get(pos).flno);
                    intent.putExtra("time","null");
                    startActivity(intent);
                }
            });
            mPagerList.add(listView);
        }
        mPager.setAdapter(new FlightInfoViewPagerAdapter(mPagerList));
        //设置圆点
        if(mDatas.size()!=0)
            setOvalLayout();
    }
   /* private void initData(){
        mDatas = new ArrayList<>();
        ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        String s[]=getIntent().getStringExtra("airport").split("-");

                list=applicationUtil.
                        getCheckedAirLineData(s[0],s[1],getIntent().getStringExtra("time"));
                if(list!=null) {

                    for (AirlineJsonBean.ListItem item : list) {
                        mDatas.add(new FlightInfo(item.flightID, item.planBegin.split(" ")[1].substring(0,5),
                                item.planEnd.split(" ")[1].substring(0,5), item.status, item.hzl));
                    }
                }
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

private void initDatas() {
        String str[] = getIntent().getStringExtra("airport").split("-");
        mDatas = new ArrayList<>();
        ApplicationUtil applicationUtil = (ApplicationUtil) getApplication();
        QueryBuilder<FlightDetail> flightDetailQueryBuilder = applicationUtil.flightDetailBox.query();
        QueryBuilder<Airport> airportQueryBuilder = applicationUtil.airportBox.query();
        airportQueryBuilder.equal(name, str[0]);
        Airport fromA = airportQueryBuilder.build().findFirst();
        QueryBuilder<Airport> airportQueryBuilder1 = applicationUtil.airportBox.query();
        airportQueryBuilder1.equal(name, str[1]);
        Airport toA = airportQueryBuilder1.build().findFirst();
        if (fromA != null && toA != null) {
            flightDetailQueryBuilder.equal(fromId, fromA.id)
                    .equal(toId, toA.id).equal(FlightDetail_.checkDay,getIntent().getStringExtra("time"));
            List<FlightDetail> flightDetails = flightDetailQueryBuilder.build().find();
            for (FlightDetail flightDetail : flightDetails) {
                QueryBuilder<Flight>flightQueryBuilder=applicationUtil.flightBox.query();
                Flight flight=flightQueryBuilder.equal(Flight_.id,flightDetail.FlightId).build().findFirst();
                mDatas.add(new FlightInfo(flight.flno, flightDetail.takeoffTime0.split(" ")[1],
                        flightDetail.arriveTime0.split(" ")[1], flightDetail.state, flightDetail.parking));
            }
        }else
            Toast.makeText(this,"不存在结果",Toast.LENGTH_SHORT).show();
    }

    public void setOvalLayout() {
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                60, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        layoutParams.gravity= Gravity.CENTER;

        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null),layoutParams);
        }
        // 默认显示第一页
        mLlDot.getChildAt(0).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) { // 取消圆点选中
                mLlDot.getChildAt(curIndex).findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                mLlDot.getChildAt(position).findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }
}

