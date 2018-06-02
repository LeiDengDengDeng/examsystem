package com.exam.vo;

import lombok.Data;

/**
 * Created by deng on 2017/10/21.
 */
@Data
public class PaperOptionVO {
    private int optionId;
    private String content;
    private boolean selected;
    private boolean right;
}
