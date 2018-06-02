package com.exam.bean;

import lombok.Data;

/**
 * Created by deng on 2017/11/9.
 */
@Data
public class PaperQuestion {
    private int id;
    private int paperId;
    private int questionId;
    private boolean marked;
    private boolean scored;

    public PaperQuestion(int paperId, int questionId, boolean marked, boolean scored) {
        this.paperId = paperId;
        this.questionId = questionId;
        this.marked = marked;
        this.scored = scored;
    }
}
