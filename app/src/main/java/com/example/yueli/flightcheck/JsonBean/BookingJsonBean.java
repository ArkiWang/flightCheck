package com.example.yueli.flightcheck.JsonBean;

import java.util.List;

/**
 * Created by yueli on 2018/7/16.
 */

public class BookingJsonBean {
    public String result;
    public List<com.example.yueli.flightcheck.Bean.BookingItem>list;

    /**
     * Created by yueli on 2018/7/7.
     */

    public static class UsersJsonBean {
        public List<JUser> results;
         public static class  JUser{
             public String createdAt;
             public long id;
             public String name;
             public String objectId;
             public String pwd;
             public String updatedAt;
         }
    }
}
