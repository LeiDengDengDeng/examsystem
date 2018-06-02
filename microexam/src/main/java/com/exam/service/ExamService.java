package com.exam.service;

import com.exam.bean.ExamParam;
import com.exam.vo.ExamVO;
import com.exam.vo.ExamVOInput;

import java.util.List;

/**
 * Created by disinuo on 17/11/16.
 */
public interface ExamService {
    /**
     * 根据输入参数创建考试
     * @param examVOInput
     * @return
     */
    boolean createExam(ExamVOInput examVOInput)throws Exception ;

    ExamVO getExamByExamId(String examId) throws Exception;

    /**
     * 根据学生id获取学生所有考试信息
     *
     * @param studentId
     * @return
     * @throws Exception
     */
    List<ExamVO> getExamsByStudentId(String studentId) throws Exception;

    List<ExamVO> getAllExams() throws Exception;

    List<ExamParam> getExamParamByExamId(String examId);

    /**
     * 根据考试ID生成所有参加该场考试人员成绩单
     * @param examId 考试ID
     * @return 成绩单是否生成成功
     */
    boolean generateTranscriptByExamId(String examId);

    String generateTranscriptCSVByExamId(String examId);
}
