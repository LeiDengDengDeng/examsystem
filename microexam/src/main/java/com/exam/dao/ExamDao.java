package com.exam.dao;

import com.exam.bean.Exam;
import com.exam.bean.ExamParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by deng on 2017/11/8.
 */
@Repository
public interface ExamDao {
    List<ExamParam> getExamParamByExamId(@Param("examId") String examId);

    Exam getExamByExamId(@Param("examId") String examId);

    void addExam(@Param("exam") Exam exam);

    void addExamParam(@Param("examParam") ExamParam examParam);

    List<Exam> getExamsByStudentId(@Param("studentId") String studentId);

    List<Exam> getAllExams();

    String getExamNameByExamId(@Param("examId") String examId);
}
