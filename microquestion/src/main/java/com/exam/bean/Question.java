package com.exam.bean;

import lombok.Data;

/**
 * Created by deng on 2017/10/21.
 */
@Data
public class Question {
    private int id;
    private int courseId;
    private int score;
    private String description;
}
