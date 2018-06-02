package com.exam.bean;

import lombok.Data;

/**
 * Created by deng on 2017/7/30.
 */
@Data
public class User {
    private String email;
    private String password;
    private int roleId;
    private String name;
    private int code;
    public User(){
    }
    public User(String email, String password, int roleId, String name, int code) {
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.name = name;
        this.code = code;
    }
    public User(String email, String password, int roleId, String name) {
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.name = name;
    }
}
