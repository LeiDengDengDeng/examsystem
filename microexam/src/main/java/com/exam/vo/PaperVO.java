package com.exam.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by deng on 2017/11/10.
 */
@Data
public class PaperVO {
    private int id;
    private int score;
    private boolean handed;
    private boolean checked;
    private List<PaperQuestionVO> paperQuestionVOs;

    private boolean started;
    private boolean ended;
}
