package com.exam.vo;

import lombok.Data;

/**
 * Created by deng on 2017/10/21.
 */
@Data
public class QuestionVO {
    private int id;
    private int score;
    private String description;
    private String courseName;
    private int optionNum;
}
