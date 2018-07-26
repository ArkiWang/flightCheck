package com.example.yueli.flightcheck.Bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by yueli on 2018/7/13.
 */
@Entity
public class Booking {
    @Id
    public long id;
    public long UserId;//final
    public long FlightId;
    public Booking(long UserId,long FlightId){
        this.UserId=UserId;
        this.FlightId=FlightId;
    }
    public Booking(){}
}
