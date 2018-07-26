package com.example.yueli.flightcheck.Bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by yueli on 2018/7/7.
 */
@Entity
public class User {
    @Id
    public long id;
    public String name;
    public String pwd;
    public String uri;
    public String phone;
    public User(String name,String pwd){this.name=name;this.pwd=pwd;}
    public User(String name,String pwd,String phone){this.name=name;this.pwd=pwd;this.phone=phone;}
    public User(){}
}
