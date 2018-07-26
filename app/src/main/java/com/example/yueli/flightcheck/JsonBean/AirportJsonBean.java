package com.example.yueli.flightcheck.JsonBean;

import java.util.List;

/**
 * Created by yueli on 2018/7/16.
 */

public class AirportJsonBean {
    public List<ListItem>list;
    public static class ListItem{
        public String airportName;
        public int state;
    }
}

