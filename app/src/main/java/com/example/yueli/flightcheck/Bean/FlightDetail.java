package com.example.yueli.flightcheck.Bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;

/**
 * Created by yueli on 2018/7/13.
 */
@Entity
public class FlightDetail {
    @Id
    @Index
    public long id;
    public long FlightId;
    public String parking;
    public long fromId,toId;
    public String takeoffTime0,takeoffTime1;
    public String arriveTime0,arriveTime1;
    public String state;
    public String checkDay;
    public FlightDetail(long FlightId,String parking,String takeoffTime0
            ,String arriveTime0,String state,long fromId,long toId){
        this.FlightId=FlightId;
        this.parking=parking;
        this.takeoffTime0=takeoffTime0;
        this.arriveTime0=arriveTime0;
        this.state=state;
        this.fromId=fromId;
        this.toId=toId;
        checkDay=takeoffTime0.split(" ")[0];
    }
    public FlightDetail(long FlightId,String parking,String takeoffTime0,String arriveTime0
    ,String takeoffTime1,String arriveTime1,String state,long fromId,long toId){
        this.FlightId=FlightId;
        this.parking=parking;
        this.takeoffTime0=takeoffTime0;
        this.arriveTime0=arriveTime0;
        this.takeoffTime1=takeoffTime1;
        this.arriveTime1=arriveTime1;
        this.state=state;
        this.fromId=fromId;
        this.toId=toId;
        checkDay=takeoffTime0.split(" ")[0];
    }
    public FlightDetail(){}
}
