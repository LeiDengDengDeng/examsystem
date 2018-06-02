package com.exam.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by deng on 2017/10/21.
 */
@Data
public class PaperQuestionVO {
    private int questionId;
    private String description;
    private int score;
    private boolean marked;
    private boolean scored;
    private List<PaperOptionVO> paperOptionVOs;
}
