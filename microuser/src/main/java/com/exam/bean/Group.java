package com.exam.bean;

import lombok.Data;

/**
 * Created by disinuo on 17/11/16.
 */

@Data
public class Group {
    private int id;
    private String name;

    public Group(){

    }
    public Group(String name ){
        this.name=name;

    }
    public Group(int id,String name){
        this.id=id;
        this.name=name;
    }

}
