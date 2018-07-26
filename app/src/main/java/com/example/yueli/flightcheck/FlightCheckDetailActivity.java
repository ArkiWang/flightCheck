package com.example.yueli.flightcheck;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yueli.flightcheck.Bean.Airport;
import com.example.yueli.flightcheck.Bean.Flight;
import com.example.yueli.flightcheck.Bean.FlightDetail;
import com.example.yueli.flightcheck.Bean.FlightDetail_;
import com.example.yueli.flightcheck.Util.ApplicationUtil;

import java.util.List;

import io.objectbox.query.QueryBuilder;

import static com.example.yueli.flightcheck.Bean.Airport_.id;
import static com.example.yueli.flightcheck.Bean.FlightDetail_.FlightId;
import static com.example.yueli.flightcheck.Bean.FlightDetail_.checkDay;
import static com.example.yueli.flightcheck.Bean.Flight_.flno;

public class FlightCheckDetailActivity extends AppCompatActivity {
    TextView state,distance,duration;
    TextView from,to;
    TextView takeoff0,takeoff1,arrive0,arrive1;
    TextView planeType,planeNo,parking;
    Handler handler;
   // List<FLNOJsonBean.ListItem> flnoList;
    private void init(){
        state=findViewById(R.id.flight_state);
        distance=findViewById(R.id.flight_distance);
        duration=findViewById(R.id.flight_duration);
        from=findViewById(R.id.flight_from_city);
        to=findViewById(R.id.flight_to_city);
        takeoff0=findViewById(R.id.flight_start_time0);
        takeoff1=findViewById(R.id.flight_start_time1);
        arrive0=findViewById(R.id.flight_end_time0);
        arrive1=findViewById(R.id.flight_end_time1);
        planeType=findViewById(R.id.flight_kind);
        planeNo=findViewById(R.id.flight_no);
        parking=findViewById(R.id.flight_port);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_check_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("flno"));
        init();
       // initData();
        setData();
    }
    private void setData(){
        ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        QueryBuilder<Flight>flightQueryBuilder=applicationUtil.flightBox.query();
        QueryBuilder<FlightDetail> flightDetailQueryBuilder=applicationUtil.flightDetailBox.query();
        Flight flight=flightQueryBuilder.equal(flno,getIntent().getStringExtra("flno")).build().findFirst();
        FlightDetail flightDetail;
        if(!getIntent().getStringExtra("time").equals("null")) {
            flightDetailQueryBuilder.equal(FlightId, flight.id)
                    .equal(checkDay, getIntent().getStringExtra("time"));
            flightDetail=flightDetailQueryBuilder.build().findFirst();
        }else {
           long fid=flightDetailQueryBuilder.equal(FlightId, flight.id).build().max(FlightDetail_.id);
           flightDetail=applicationUtil.flightDetailBox.get(fid);
        }
        if(flightDetail!=null) {
            QueryBuilder<Airport> airportQueryBuilder = applicationUtil.airportBox.query();
            airportQueryBuilder.equal(id, flightDetail.fromId);
            Airport fromA = airportQueryBuilder.build().findFirst();
            QueryBuilder<Airport> airportQueryBuilder1 = applicationUtil.airportBox.query();
            airportQueryBuilder1.equal(id, flightDetail.toId);
            Airport toA = airportQueryBuilder1.build().findFirst();
            if (flightDetail != null&&toA!=null&&fromA!=null) {
                from.setText(fromA.name);
                to.setText(toA.name);
                planeType.setText(flight.planeType);
                planeNo.setText(flight.planeNo);
                takeoff0.setText(flightDetail.takeoffTime0);
                arrive0.setText(flightDetail.arriveTime0);
                parking.setText(flightDetail.parking);
                state.setText(flightDetail.state);
            }
        }
    }
    /*private void initData(){
        if(flnoList!=null&&flnoList.size()>0) {
            FLNOJsonBean.ListItem item = flnoList.get(0);
            from.setText(item.placeBegin);
            to.setText(item.placeEnd);
            takeoff0.setText(item.planBegin);
            takeoff1.setText(item.realBegin);
            arrive0.setText(item.planEnd);
            arrive1.setText(item.realEnd);
            parking.setText(item.hzl);
            planeType.setText(item.planeType);
            planeNo.setText(item.planeNum);
            state.setText(item.status);
        }*/
        /*
        final ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
         final Intent intent=getIntent();
        handler=new Handler(){
             @Override
             public void handleMessage(Message msg) {
                 if(msg.obj.equals("ok")){
                     if(flnoList!=null&&flnoList.size()>0) {
                         FLNOJsonBean.ListItem item = flnoList.get(0);
                         from.setText(item.placeBegin);
                         to.setText(item.placeEnd);
                         takeoff0.setText(item.planBegin);
                         takeoff1.setText(item.realBegin);
                         arrive0.setText(item.planEnd);
                         arrive1.setText(item.realEnd);
                         parking.setText(item.hzl);
                         planeType.setText(item.planeType);
                         planeNo.setText(item.planeNum);
                         state.setText(item.status);
                     }
                 }
             }
         };
         new Thread(new Runnable() {
             @Override
             public void run() {
                 String flno=intent.getStringExtra("flno");
                 flnoList=applicationUtil.getCheckedFLNOData(flno.substring(2)
                         ,intent.getStringExtra("time"));
                 Message message=new Message();
                 message.obj="ok";
                 handler.sendMessage(message);
             }
         }).start();
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

}
