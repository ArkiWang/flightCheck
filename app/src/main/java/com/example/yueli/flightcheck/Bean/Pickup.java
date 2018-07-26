package com.example.yueli.flightcheck.Bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by yueli on 2018/7/13.
 */
@Entity
public class Pickup {
    @Id
    public long id;
    public long AirportId;//final
    public long FlightId;
    public Pickup(long Airport,long FlightId){
        this.FlightId=FlightId;
        this.AirportId=Airport;
    }
    public Pickup(){}
}
