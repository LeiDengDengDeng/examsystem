package com.exam.bean;

import lombok.Data;

/**
 * Created by deng on 2017/11/8.
 */
@Data
public class Exam {
    private String id;
    private String name;
    private String startTime;
    private String endTime;
    private int courseId;
}
