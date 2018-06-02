package com.exam.bean;

import lombok.Data;

import java.util.Map;

/**
 * Created by disinuo on 17/11/16.
 */
@Data
public class ExamVOInput {
    /**
     * 分数:题目数
     */
    private Map<Integer,Integer> questions;
    /**
     * 考试起止时间 形如2017-01-01 00:00:00
     */
    private String startTime;
    private String endTime;
    private int courseId;
    private int groupId;//参加考试的班级id
    private String examName;

    public ExamVOInput(String startTime, String endTime, int groupId, int courseId, String examName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.courseId = courseId;
        this.examName = examName;
        this.groupId=groupId;
    }
    public ExamVOInput(){}
}
