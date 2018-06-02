package com.exam.bean;

import lombok.Data;

/**
 * Created by deng on 2017/11/9.
 */
@Data
public class PaperOption {
    private int id;
    private int paperId;
    private int optionId;
    private int questionId;
    private boolean selected;

    public PaperOption(int paperId, int optionId, int questionId, boolean selected) {
        this.paperId = paperId;
        this.optionId = optionId;
        this.questionId = questionId;
        this.selected = selected;
    }
}
