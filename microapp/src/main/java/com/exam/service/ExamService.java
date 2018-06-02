package com.exam.service;

import com.exam.bean.ExamVOInput;
import com.exam.bean.ReturnBean;

/**
 * Created by deng on 2017/11/20.
 */
public interface ExamService {
    String getAllExams();

    String getExamParamsByExamId(String examId);

    String getExamByExamId(String examId);

    String getExamsByStudentId(String studentId);

    String createExam(ExamVOInput examVOInput);
}
