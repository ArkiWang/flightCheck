package com.example.yueli.flightcheck.CallBackInterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by yueli on 2018/7/8.
 */

public class CityBroadCastReceiver extends BroadcastReceiver {
    private String position;
    public CityBroadCastReceiver(String position){
        this.position=position;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String city=null;
        if(intent.getAction().equals(position)){
        city=intent.getStringExtra("city");
        onCityReceiveListener.onReceive(city);
        }
    }
    OnCityReceiveListener onCityReceiveListener;
    public interface OnCityReceiveListener{
        public void onReceive(String city);
    }
    public void setOnCityReceiveListener(OnCityReceiveListener onCityReceiveListener){
        this.onCityReceiveListener=onCityReceiveListener;
    }
}
