package com.example.yueli.flightcheck.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.yueli.flightcheck.AirportSelectActivity;
import com.example.yueli.flightcheck.CallBackInterface.CityBroadCastReceiver;
import com.example.yueli.flightcheck.FlightCheckActivity;
import com.example.yueli.flightcheck.R;
import com.example.yueli.flightcheck.Util.ApplicationUtil;
import com.example.yueli.flightcheck.Util.showDialog;

import java.util.Date;

/**
 * Created by yueli on 2018/7/5.
 */

public class fragCheckAirport extends Fragment implements View.OnClickListener{
    private View cityDate,left,right;
    private TextView cityDateText;
    private Date date;
    private CityBroadCastReceiver leftReceiver,rightReceiver ;
    private TextView sourCity,desCity,xchgBtn;
    private Button submit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_tabcon_city,null);
        cityDate=view.findViewById(R.id.city_date);
        ApplicationUtil applicationUtil=(ApplicationUtil)getActivity().getApplication();
        left=view.findViewById(R.id.left_city);
        right=view.findViewById(R.id.right_city);
        cityDateText=view.findViewById(R.id.city_date_text);
        date=new Date();
        String month=""+(date.getMonth()+1);
        String day=""+(date.getDay()+1);
        if(month.length()<2)
            month=0+month;
        if(day.length()<2)
            day=0+day;
        cityDateText.setText(date.getYear()-100+2000+"-"+month+"-"+day);
        cityDate.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        sourCity=view.findViewById(R.id.sour_city);
        desCity=view.findViewById(R.id.des_city);
        sourCity.setText("天津");
        desCity.setText("上海虹桥");
        xchgBtn=view.findViewById(R.id.exchange_city);
        xchgBtn.setOnClickListener(this);
        submit=view.findViewById(R.id.frag_tabcon_city_btn);
        submit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        leftReceiver=new CityBroadCastReceiver("left_city");
        rightReceiver=new CityBroadCastReceiver("right_city");
        recvCity("left_city",leftReceiver);
        recvCity("right_city",rightReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(leftReceiver);
        getActivity().unregisterReceiver(rightReceiver);
    }

    public void recvCity(final String fliter,CityBroadCastReceiver cityBroadCastReceiver){
        IntentFilter cityBroadFliter=new IntentFilter();
        cityBroadFliter.addAction(fliter);
        getActivity().registerReceiver(cityBroadCastReceiver,cityBroadFliter);
        cityBroadCastReceiver.setOnCityReceiveListener(new CityBroadCastReceiver.OnCityReceiveListener() {
            @Override
            public void onReceive(String city) {
                if(fliter.equals("left_city"))
                    sourCity.setText(city);
                else if(fliter.equals("right_city"))
                    desCity.setText(city);
            }
        });

    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
       // 参数1： 触发事件    参数2：年    参数3：月    参数4：：日(ps:参数2、3、4是显示的时间)
          @Override
       public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
              String month=""+(monthOfYear+1);
              String day=""+dayOfMonth;
              if(month.length()<2)
                  month=0+month;
              if(day.length()<2)
                  day=0+day;
              cityDateText.setText(year+"-"+month+"-"+day);
       }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city_date:
                showDialog.showDateDiaglog(getActivity(),dateSetListener);
                Log.v("arki","??????");
                break;
            case R.id.left_city:
                Intent intent=new Intent(getActivity(), AirportSelectActivity.class);
                intent.putExtra("class","check");
                intent.putExtra("position","left");
                //intent.putExtra("city",sourCity.getText().toString());
                startActivity(intent);
                break;
            case R.id.right_city:
                Intent intent1=new Intent(getActivity(), AirportSelectActivity.class);
                intent1.putExtra("class","check");
                intent1.putExtra("position","right");
                //intent1.putExtra("city",desCity.getText().toString());
                startActivity(intent1);
                break;
            case R.id.exchange_city:
                String tmp=sourCity.getText().toString();
                sourCity.setText(desCity.getText());
                desCity.setText(tmp);
                break;
            case R.id.frag_tabcon_city_btn:
                Intent intent2=new Intent(getActivity(), FlightCheckActivity.class);
                intent2.putExtra("airport",sourCity.getText()+"-"+desCity.getText());
                intent2.putExtra("time",cityDateText.getText().toString());
                startActivity(intent2);
                break;
        }
    }
}

