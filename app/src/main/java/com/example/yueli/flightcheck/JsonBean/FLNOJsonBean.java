package com.example.yueli.flightcheck.JsonBean;

import java.util.List;

/**
 * Created by yueli on 2018/7/16.
 */

public class FLNOJsonBean {
    public List<ListItem>list;
    public String requestStatus;
    public static class ListItem{
        public String flightID;
        public String planBegin;
        public String realBegin;
        public String planEnd;
        public String realEnd;
        public String placeBegin;
        public String placeEnd;
        public String pacCode;
        public String planeType;
        public String planeNum;
        public String hzl;
        public String status;
    }
}
