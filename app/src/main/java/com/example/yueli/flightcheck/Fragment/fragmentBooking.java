package com.example.yueli.flightcheck.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yueli.flightcheck.Bean.Booking;
import com.example.yueli.flightcheck.Bean.Flight;
import com.example.yueli.flightcheck.BookingActivity;
import com.example.yueli.flightcheck.CallBackInterface.CityBroadCastReceiver;
import com.example.yueli.flightcheck.CallBackInterface.NotifyMsgReceiver;
import com.example.yueli.flightcheck.FlightSelectActivity;
import com.example.yueli.flightcheck.R;
import com.example.yueli.flightcheck.Util.ApplicationUtil;
import com.example.yueli.flightcheck.Util.REQUEST_INT;

import io.objectbox.query.QueryBuilder;

import static com.example.yueli.flightcheck.Bean.Booking_.FlightId;
import static com.example.yueli.flightcheck.Bean.Booking_.UserId;
import static com.example.yueli.flightcheck.Bean.Flight_.flno;

/**
 * Created by yueli on 2018/7/11.
 */

public class fragmentBooking extends Fragment implements View.OnClickListener {
    TextView addBooking;
    TextView bookingMsg,news;
    private final int ADD_BOOKING_FLIGHT=0;
    int result;
    Handler handler;
    NotifyMsgReceiver notifyMsgReceiver;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //getActivity().getActionBar().hide();
        View view=inflater.inflate(R.layout.fragment_booking,null);
        addBooking=view.findViewById(R.id.add_booking);
        addBooking.setOnClickListener(this);
        bookingMsg=view.findViewById(R.id.booking_msg);
        bookingMsg.setOnClickListener(this);
        news=view.findViewById(R.id.newest_msg);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode== REQUEST_INT.FRAG_BOOKING&&resultCode==REQUEST_INT.FRAG_BOOKING){
            final ApplicationUtil applicationUtil=(ApplicationUtil)getActivity().getApplication();
          /*  handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.obj.equals("ok")){
                        switch (result){
                            case 0:
                                Toast.makeText(getActivity(),"关注成功",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getActivity(),"已关注该航班",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    result=applicationUtil.addBooking(data.getStringExtra("booking"));
                    Message message=new Message();
                    message.obj="ok";
                    handler.sendMessage(message);
                }
            }).start();*/
            QueryBuilder<Flight> flightQueryBuilder=applicationUtil.flightBox.query();
            flightQueryBuilder.equal(flno,data.getStringExtra("booking"));
            long fid=flightQueryBuilder.build().findFirst().id;
            QueryBuilder<Booking>bookingQueryBuilder=applicationUtil.bookingBox.query();
            Booking booking=bookingQueryBuilder.equal(UserId,applicationUtil.userId)
                    .equal(FlightId,fid).build().findFirst();
            if(booking==null) {
                applicationUtil.bookingBox.put(new Booking(applicationUtil.userId, fid));
                Toast.makeText(getActivity(), "订阅成功", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(getActivity(), "已订阅该航班", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        notifyMsgReceiver=new NotifyMsgReceiver();
        recvMsg(notifyMsgReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(notifyMsgReceiver);
    }

    public void recvMsg(NotifyMsgReceiver notifyMsgReceiver){
        IntentFilter msgBroadFliter=new IntentFilter();
        msgBroadFliter.addAction("notify");
        getActivity().registerReceiver(notifyMsgReceiver,msgBroadFliter);
        notifyMsgReceiver.setOnMsgReceiveListener(new NotifyMsgReceiver.OnMsgReceiveListener() {
            @Override
            public void onReceive(String msg) {
                news.setText(msg);
            }
        });
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.add_booking:
                intent=new Intent(getActivity(), FlightSelectActivity.class);
                intent.putExtra("request",REQUEST_INT.FRAG_BOOKING);
                startActivityForResult(intent,REQUEST_INT.FRAG_BOOKING);
                //startActivityForResult(intent,ADD_BOOKING_FLIGHT);
                break;
            case R.id.booking_msg:
                intent=new Intent(getActivity(), BookingActivity.class);
                startActivity(intent);
                //startActivityForResult(intent,ADD_BOOKING_FLIGHT);
                break;
        }
    }
}
