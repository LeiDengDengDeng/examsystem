package com.exam.service;

import com.exam.bean.ReturnBean;

/**
 * Created by deng on 2017/11/18.
 */
public interface PaperService {
    ReturnBean<String> generatePaper(String studentId, String examId, String code);

    String getPaper(String studentId, String examId);

    String getPapers(String examId);

    boolean markQuestion(int paperId, int questionId, boolean marked);

    boolean selectOption(int paperId, int optionId, boolean selected);

    boolean handPaper(String studentId, int paperId);
}
