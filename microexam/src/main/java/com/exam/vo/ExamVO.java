package com.exam.vo;

import lombok.Data;

/**
 * Created by deng on 2017/11/8.
 */
@Data
public class ExamVO {
    private String id;
    private String name;
    private String startTime;
    private String endTime;
    private String courseName;
    private String state;

    private int totalScore; // 试卷总分
}
