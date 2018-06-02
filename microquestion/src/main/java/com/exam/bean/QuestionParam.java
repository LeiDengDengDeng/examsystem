package com.exam.bean;

import lombok.Data;

/**
 * Created by deng on 2017/12/10.
 * 问题分数和该分数下的题目总数
 */
@Data
public class QuestionParam {
    private int score;
    private int num;
}
