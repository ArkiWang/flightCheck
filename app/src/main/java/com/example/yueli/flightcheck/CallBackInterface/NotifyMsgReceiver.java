package com.example.yueli.flightcheck.CallBackInterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by yueli on 2018/7/20.
 */

public class NotifyMsgReceiver extends BroadcastReceiver {
    String msg;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("notify")){
            msg=intent.getStringExtra("msg");
            onMsgReceiveListener.onReceive(msg);
        }
    }
    NotifyMsgReceiver.OnMsgReceiveListener onMsgReceiveListener;
    public interface OnMsgReceiveListener{
        public void onReceive(String msg);
    }
    public void setOnMsgReceiveListener(NotifyMsgReceiver.OnMsgReceiveListener onMsgReceiveListener){
        this.onMsgReceiveListener=onMsgReceiveListener;
    }
}
