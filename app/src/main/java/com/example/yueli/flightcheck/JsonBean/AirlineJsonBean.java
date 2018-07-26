package com.example.yueli.flightcheck.JsonBean;

import java.util.List;

/**
 * Created by yueli on 2018/7/16.
 */

public class AirlineJsonBean {
    public List<ListItem>list;
    public static class ListItem{
        public String flightID;
        public String planBegin;
        public String realBegin;
        public String planEnd;
        public String realEnd;
        public String hzl;
        public String status;
    }
}
