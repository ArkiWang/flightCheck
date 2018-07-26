package com.example.yueli.flightcheck.Bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by yueli on 2018/7/13.
 */
@Entity
public class Flight {
    @Id
    public long id;
    public String flno;
    public String planeType,planeNo;
    //public ToOne<Airport>ariport;
    public Flight(String flno,String planeType,String planeNo/*,long ariportId*/){
        this.flno=flno;
        this.planeType=planeType;
        this.planeNo=planeNo;
        //this.ariport.setTargetId(ariportId);
    }
    /*public Flight(String flno,String planeType,String planeNo,Airport airport){
        this.flno=flno;
        this.planeType=planeType;
        this.planeNo=planeNo;
        this.ariport.setTarget(airport);
    }*/
    public Flight(){}
}
