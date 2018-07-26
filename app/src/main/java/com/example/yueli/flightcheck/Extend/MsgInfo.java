package com.example.yueli.flightcheck.Extend;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/15 0015.
 */

public class MsgInfo {
    public String mMsg;//信息内容
    Date mTime;
    public String mName;
    public int mHead;

    public MsgInfo(String msg,String name,int head){
        this.mMsg=msg;
        this.mName=name;
        this.mHead=head;
        this.mTime=new Date(System.currentTimeMillis());
    }
    public String getTime(){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return formatter.format(this.mTime);
    }
    public boolean IsMyMsg(){
        if(mName.equals("我")){
            return true;
        }else{
            return false;
        }

    }

}
