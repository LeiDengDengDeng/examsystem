package com.exam.bean;

import lombok.Data;

/**
 * Created by deng on 2017/8/6.
 */
@Data
public class ReturnBean<T> {
    private T data;
    private boolean success;
    private String msg;
}
