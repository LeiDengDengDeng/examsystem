package com.exam.vo;

import com.exam.bean.User;
import lombok.Data;

/**
 * Created by deng on 2017/10/19.
 */
@Data
public class UserVO {
    private String username;
    private String role;
    private String name;
    private int code;

    public UserVO(String username, String role,String name,int code) {
        this.username = username;
        this.role = role;
        this.code=code;
        this.name=name;
    }
    public UserVO(){

    }
}
