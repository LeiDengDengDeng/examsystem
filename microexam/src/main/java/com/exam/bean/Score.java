package com.exam.bean;

import lombok.Data;

/**
 * Created by chentiange on 2017/11/23.
 */
@Data
public class Score {
    private String examId; //考试id
    private int score; //分数
    private String studentId; //学生id

}
