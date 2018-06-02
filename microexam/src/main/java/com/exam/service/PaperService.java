package com.exam.service;

import com.exam.bean.Paper;
import com.exam.vo.PaperVO;

import java.util.List;

/**
 * Created by deng on 2017/11/8.
 */
public interface PaperService {
    /**
     * 校验考生密码，生成对应考试试卷，获得试卷信息
     *
     * @param studentId 学生邮箱
     * @param examId    考试ID
     * @param code      学生收到的密码
     * @return 试卷id
     * @throws Exception 校验失败
     */
    PaperVO generatePaper(String studentId, String examId, String code) throws Exception;

    /**
     * 根据考试Id和学生Id获得试卷信息
     *
     * @param examId
     * @param studentId
     * @return
     */
    PaperVO getPaperInformation(String examId, String studentId);

    /**
     * 由考试的ID得到该场考试的所有试卷的基本信息
     *
     * @param examId 考试ID
     * @return 该场考试所有试卷的基本信息
     */
    List<Paper> getPapersByExamId(String examId);

    /**
     * 计算试卷ID对应试卷的分数
     *
     * @param paperId 试卷ID
     * @return 计算分数是否成功
     */
    boolean countScoreForAPaper(int paperId);

    /**
     * 标记/取消标记试卷题目
     *
     * @param paperId
     * @param questionId
     * @param marked     标记/取消标记
     * @return
     */
    boolean markPaperQuestion(int paperId, int questionId, boolean marked);

    /**
     * 选择/取消选择试卷选项
     *
     * @param paperId
     * @param optionId
     * @param selected 选择/取消选择
     * @return
     */
    boolean selectPaperOption(int paperId, int optionId, boolean selected);

    /**
     * 提前交卷
     *
     * @param studentId
     * @param paperId
     * @return
     */
    boolean handPaper(String studentId, int paperId);
}
