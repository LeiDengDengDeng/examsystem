package com.exam.bean;

import lombok.Data;

/**
 * Created by deng on 2017/11/8.
 */
@Data
public class Paper {
    private int id;
    private int score;
    private String studentId;
    private String examId;
    private String code;
    private boolean handed;
    private boolean generated;
    private boolean checked;
}
