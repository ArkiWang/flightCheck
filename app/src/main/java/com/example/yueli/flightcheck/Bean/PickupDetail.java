package com.example.yueli.flightcheck.Bean;

/**
 * Created by yueli on 2018/7/19.
 */

public  class PickupDetail{
    public String flno;
    public String parking;
    public String toA;
   // public String takeoffTime0,takeoffTime1;
    public String arriveTime0,arriveTime1;
    public String state;
    public PickupDetail(String flno,String parking,String toA/*,String takeoffTime0,String takeoffTime1*/,String arriveTime0
            ,String arriveTime1,String state){
        this.flno=flno;
        this.parking=parking;
        this.toA=toA;
        //this.takeoffTime0=takeoffTime0;
       // this.takeoffTime1=takeoffTime1;
        this.arriveTime0=arriveTime0;
        this.arriveTime1=arriveTime1;
        this.state=state;
    }
}