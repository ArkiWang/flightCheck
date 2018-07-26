package com.example.yueli.flightcheck.JsonBean;

import java.util.List;

/**
 * Created by yueli on 2018/7/17.
 */

public class PickupJsonBean {
    public List<ListItem>list;
    public static class ListItem{
        public String  flightID;
        public String  placeBegin;
        public String planEnd;
        public String realEnd;
        public String hzl;
        public String status;
    }
}
