package com.exam.bean;

import lombok.Data;

/**
 * Created by deng on 2017/10/21.
 */
@Data
public class Option {
    private int id;
    private int questionId;
    private String content;
    private boolean right;

    public Option(int id, int questionId, String content, boolean right) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.right = right;
    }

    public Option(int questionId, String content, boolean right) {
        this.questionId = questionId;
        this.content = content;
        this.right = right;
    }
}
