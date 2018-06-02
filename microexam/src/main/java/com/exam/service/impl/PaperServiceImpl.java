package com.exam.service.impl;

import com.exam.bean.*;
import com.exam.dao.ExamDao;
import com.exam.dao.PaperDao;
import com.exam.service.PaperService;
import com.exam.util.MailUtil;
import com.exam.util.QuestionInvoker;
import com.exam.util.TimeUtil;
import com.exam.vo.PaperOptionVO;
import com.exam.vo.PaperQuestionVO;
import com.exam.vo.PaperVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by deng on 2017/11/8.
 */
@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    private PaperDao paperDao;
    @Autowired
    private ExamDao examDao;
    @Autowired
    private QuestionInvoker questionInvoker;
    @Autowired
    private MailUtil mailUtil;

    @Override
    public PaperVO generatePaper(String studentId, String examId, String code) throws Exception {
//        synchronized (this) { // 感觉这里需要加锁保证不会有多个线程同时生成一个code的paper...不考虑并发就算了
        Paper paper = paperDao.getPaperByCode(code);
        if (paper == null || !studentId.equals(paper.getStudentId()) || !examId.equals(paper.getExamId())) {
            throw new Exception("试卷信息校验失败");
        }
        // 需要判断试卷是否已经生成过
        if (paper.isGenerated()) {
            return convertBeanToVO(paper);
        }

        Map<Integer, Integer> params = new HashMap<>();
        List<ExamParam> examParams = examDao.getExamParamByExamId(examId);
        for (ExamParam examParam : examParams) {
            params.put(examParam.getScore(), examParam.getNum());
        }

        Exam exam = examDao.getExamByExamId(examId);
        Map<Integer, List<Integer>> quesAndOpt = questionInvoker.generateQuestions(exam.getCourseId(), params);
        if (quesAndOpt == null) {
            throw new Exception("试卷生成失败");
        } else {
            for (int questionId : quesAndOpt.keySet()) {
                paperDao.addPaperQuestion(new PaperQuestion(paper.getId(), questionId, false, false));

                List<Integer> optionIds = quesAndOpt.get(questionId);
                for (int optionId : optionIds) {
                    paperDao.addPaperOption(new PaperOption(paper.getId(), optionId, questionId, false));
                }
            }

            paper.setGenerated(true);
            paperDao.updatePaper(paper);

            return convertBeanToVO(paper);
        }
//        }
    }

    @Override
    public PaperVO getPaperInformation(String examId, String studentId) {
        Paper paper = paperDao.getPaperByExamIdAndStudentId(examId, studentId);
        return convertBeanToVO(paper);
    }

    @Override
    public List<Paper> getPapersByExamId(String examId) {
        return paperDao.getPapersByExamId(examId);
    }

    private PaperVO convertBeanToVO(Paper paper) {
        PaperVO paperVO = new PaperVO();
        if (paper == null || !paper.isGenerated()) {
            return null;
        }

        BeanUtils.copyProperties(paper, paperVO);
        paperVO.setStarted(false);
        paperVO.setEnded(false);
        try {
            Exam exam = examDao.getExamByExamId(paper.getExamId());
            paperVO.setStarted(TimeUtil.isEarlierThanCurrentTime(exam.getStartTime()));
            paperVO.setEnded(TimeUtil.isEarlierThanCurrentTime(exam.getEndTime()));

            int paperId = paper.getId();
            List<PaperQuestion> paperQuestions = paperDao.getPaperQuestions(paperId);
            List<PaperQuestionVO> paperQuestionVOs = new ArrayList<>();
            for (PaperQuestion question : paperQuestions) {
                PaperQuestionVO paperQuestionVO = questionInvoker.getQuestionInfo(question.getQuestionId());
                BeanUtils.copyProperties(question, paperQuestionVO);

                List<PaperOption> paperOptions = paperDao.getPaperOptions(paperId, question.getQuestionId());
                List<PaperOptionVO> paperOptionVOs = new ArrayList<>();
                for (PaperOption option : paperOptions) {
                    PaperOptionVO paperOptionVO = questionInvoker.getOptionInfo(option.getOptionId());
                    BeanUtils.copyProperties(option, paperOptionVO);

                    paperOptionVOs.add(paperOptionVO);
                }
                paperQuestionVO.setPaperOptionVOs(paperOptionVOs);
                paperQuestionVOs.add(paperQuestionVO);
            }
            paperVO.setPaperQuestionVOs(paperQuestionVOs);
        } catch (Exception e) {
            return null;
        }
        return paperVO;
    }

    @Override
    public boolean countScoreForAPaper(int paperId) {
        int totalScore = 0;
        boolean result = false;
        Paper paper = paperDao.getPaperById(paperId);
        if (paper == null || !paper.isGenerated()) {
            return false;
        }
        //得到试卷上的每个题目
        List<PaperQuestion> questions = paperDao.getPaperQuestions(paperId);
        for (PaperQuestion question : questions) {
            boolean scored = false;
            //评判答案是否正确【只有全对才得分】
            //1.首先得到选项中的所有正确答案
            //1.1得到试卷中所有的选项
            List<PaperOption> options = paperDao.getPaperOptions(paperId, question.getQuestionId());
            //1.2得到所有正确的选项
            List<PaperOption> rightOptions = new ArrayList<PaperOption>();
            //1.3得到考生选择的所有选项
            List<PaperOption> selectedOptions = new ArrayList<PaperOption>();
            for (int i = 0; i < options.size(); ++i) {
                PaperOption tempOption = options.get(i);
                PaperOptionVO optionVO;
                try {
                    optionVO = questionInvoker.getOptionInfo(tempOption.getOptionId());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                if (optionVO.isRight()) {
                    rightOptions.add(tempOption);
                }

                if (tempOption.isSelected()) {
                    selectedOptions.add(tempOption);
                }
            }
            //2.判断是否选择了所有正确选项以及没有错选
            if ((selectedOptions.size() == rightOptions.size()) && selectedOptions.containsAll(rightOptions)) {
                scored = true;
                //总分加上这道题的分数
                try {
                    PaperQuestionVO questionVO = questionInvoker.getQuestionInfo(question.getQuestionId());
                    totalScore += questionVO.getScore();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

            }
            //3.给这道题目标记是否得分
            question.setScored(scored);
            paperDao.updatePaperQuestion(question);
        }
        //给这张试卷设置总分
        paper.setScore(totalScore);
        paper.setChecked(true);
        //同步试卷
        paperDao.updatePaper(paper);

        // 发送邮件给考生
        Exam exam = examDao.getExamByExamId(paper.getExamId());
        if (exam != null) {
            String title = "【" + exam.getName() + "】考试结果通知";
            String content = "你的考试结果为【" + paper.getScore() + "分】，试卷详细情况请打开网站查看。";
            try {
                mailUtil.sendEmail(paper.getStudentId(), title, content, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public boolean markPaperQuestion(int paperId, int questionId, boolean marked) {
        PaperQuestion paperQuestion = paperDao.getPaperQuestion(paperId, questionId);
        if (paperQuestion == null) {
            return false;
        }
        paperQuestion.setMarked(marked);
        paperDao.updatePaperQuestion(paperQuestion);
        return true;
    }

    @Override
    public boolean selectPaperOption(int paperId, int optionId, boolean selected) {
        PaperOption paperOption = paperDao.getPaperOption(paperId, optionId);
        if (paperOption == null) {
            return false;
        }
        paperOption.setSelected(selected);
        paperDao.updatePaperOption(paperOption);
        return true;
    }

    @Override
    public boolean handPaper(String studentId, int paperId) {
        Paper paper = paperDao.getPaperById(paperId);
        if (paper == null || !paper.getStudentId().equals(studentId) || paper.isHanded()) {
            return false;
        }
        paper.setHanded(true);
        paperDao.updatePaper(paper);
        return true;
    }
}