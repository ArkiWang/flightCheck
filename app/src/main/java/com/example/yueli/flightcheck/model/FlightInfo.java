package com.example.yueli.flightcheck.model;

/**
 * Created by yueli on 2018/7/10.
 */

public class FlightInfo {
    public String flno;
    public String takeOff;
    public String arrive;
    public String state;
    public String boarding;
    public FlightInfo(String flno,String takeOff,String arrive,String state,String boarding){
        this.flno=flno;
        this.arrive=arrive;
        this.boarding=boarding;
        this.takeOff=takeOff;
        this.state=state;
    }
}
