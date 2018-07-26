package com.example.yueli.flightcheck.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yueli.flightcheck.Adapter.FlightMsgListAdapter;
import com.example.yueli.flightcheck.AirportSelectActivity;
import com.example.yueli.flightcheck.Bean.Airport;
import com.example.yueli.flightcheck.Bean.Airport_;
import com.example.yueli.flightcheck.Bean.Flight;
import com.example.yueli.flightcheck.Bean.FlightDetail;
import com.example.yueli.flightcheck.Bean.Flight_;
import com.example.yueli.flightcheck.Bean.Pickup;
import com.example.yueli.flightcheck.Bean.PickupDetail;
import com.example.yueli.flightcheck.Bean.Pickup_;
import com.example.yueli.flightcheck.R;
import com.example.yueli.flightcheck.Util.ApplicationUtil;
import com.example.yueli.flightcheck.Util.REQUEST_INT;
import com.example.yueli.flightcheck.view.WaterDropListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.objectbox.query.QueryBuilder;

import static com.example.yueli.flightcheck.Bean.FlightDetail_.FlightId;


/**
 * Created by yueli on 2018/7/5.
 */

public class fragmentRecvMsg extends Fragment implements AdapterView.OnItemClickListener,WaterDropListView.IWaterDropListViewListener{
    private FlightMsgListAdapter flightMsgListAdapter;
    private WaterDropListView msgList;
    private List<PickupDetail>pickupList=new ArrayList<>();
    MenuItem airport;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_recv_msg,null);
        //appUtil.init();
        //init();
       // getHttpJsonData("https://api.bmob.cn/1/classes/user$Test?all");
       /* final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.obj.equals("ok")){
                    flightMsgListAdapter.notifyDataSetChanged();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                pickupList=appUtil.getPickupData(selectAirport.getText().toString());
                if(pickupList==null)
                    pickupList=new ArrayList<>();
                Message message=new Message();
                message.obj="ok";
                handler.sendMessage(message);
            }
        }).start();
        if(pickupList==null||pickupList.size()==0) {
            Toast.makeText(getActivity(), "no data!", Toast.LENGTH_SHORT);
            Log.v("arki","no data");
        }*/
       initData("天津");
        flightMsgListAdapter = new FlightMsgListAdapter(getActivity(),pickupList);
        msgList = view.findViewById(R.id.frag_recv_msg_list);
        msgList.setAdapter(flightMsgListAdapter);
        msgList.setOnItemClickListener(this);
        msgList.setWaterDropListViewListener(this);
        msgList.setPullLoadEnable(true);
        setHasOptionsMenu(true);
        return view;
    }
    private void initData(String Airport){
        final ApplicationUtil appUtil=(ApplicationUtil)getActivity().getApplication();
        List<Pickup>pickups= appUtil.pickupBox.find(Pickup_.AirportId,appUtil.getAirport(Airport).id);
        for(Pickup p:pickups){
            List<Flight>flights=appUtil.flightBox.find(Flight_.id,p.FlightId);
            for(Flight f:flights){
                QueryBuilder<FlightDetail>flightDetailQueryBuilder=appUtil.flightDetailBox.query();
                FlightDetail flightDetail=flightDetailQueryBuilder.equal(FlightId,f.id).build().findFirst();
                pickupList.add(new PickupDetail(f.flno,flightDetail.parking,Airport
                        ,flightDetail.arriveTime0.split(" ")[1],
                        flightDetail.arriveTime1!=null?flightDetail.arriveTime1.split("-")[1]:null
                        ,flightDetail.state));
            }
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.recv_airport_xchg,menu);
        menu.findItem(R.id.recv_xchg).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent=new Intent(getActivity(),AirportSelectActivity.class);
                intent.putExtra("class","recv");
                startActivityForResult(intent,REQUEST_INT.RECV_XCHG_AIRPORT);
                return true;
            }
        });
        airport=menu.findItem(R.id.recv_text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode==REQUEST_INT.PICKUP_MSG_AIRPORT&&resultCode==REQUEST_INT.PICKUP_MSG_AIRPORT){
            String airport=data.getStringExtra("airport");
            ApplicationUtil applicationUtil=(ApplicationUtil)getActivity().getApplication();
            List<PickupJsonBean.ListItem> pickupList=applicationUtil.getPickupData(airport);
        }*/
        if(requestCode==REQUEST_INT.RECV_XCHG_AIRPORT&&resultCode==REQUEST_INT.RECV_XCHG_AIRPORT){
            String ap=data.getStringExtra("airport");
            airport.setTitle(ap);
            pickupList.clear();
            initData(ap);
            flightMsgListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(),"item:"+position,Toast.LENGTH_SHORT).show();
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    msgList.stopRefresh();
                    break;
                case 2:
                    msgList.stopLoadMore();
                    break;
            }

        }
    };

    @Override
    public void onRefresh() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onLoadMore() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    handler.sendEmptyMessage(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
