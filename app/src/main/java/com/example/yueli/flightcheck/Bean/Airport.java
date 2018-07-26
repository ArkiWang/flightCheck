package com.example.yueli.flightcheck.Bean;

import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * Created by yueli on 2018/7/13.
 */
@Entity
public class Airport {
    @Id
    public long id;
    public String name;
    public int kind;
   // @Backlink
    //public ToMany<Flight>flights;
    public Airport(){}
   /* public Airport(String name, int kind, List<Flight>flights){
        this.name=name;
        this.kind=kind;
        this.flights.addAll(flights);
    }*/
    public Airport(String name, int kind){
        this.name=name;
        this.kind=kind;
    }
}
