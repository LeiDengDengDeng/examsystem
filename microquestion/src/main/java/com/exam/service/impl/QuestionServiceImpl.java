package com.exam.service.impl;

import com.exam.bean.Option;
import com.exam.bean.Question;
import com.exam.bean.QuestionParam;
import com.exam.dao.OptionDao;
import com.exam.dao.QuestionDao;
import com.exam.service.QuestionService;
import com.exam.thread.ThreadPool;
import com.exam.util.ExcelUtil;
import com.exam.util.UserInvoker;
import com.exam.vo.QuestionVO;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by deng on 2017/10/21.
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private OptionDao optionDao;
    @Autowired
    private UserInvoker userInvoker;

    @Override
    public void createImportQuestionsTask(int courseId, String fileCompletePath) {
        ThreadPool.execute(new QuestionImportThread(courseId, fileCompletePath));
    }

    @Override
    @Transactional
    public void importQuestions(int courseId, String fileCompletePath) {
        // TODO:添加日志记录 or 做成同步执行 AND 待测Transactional是否生效 AND 文件数据有效性检查不够完备
        Workbook wb = ExcelUtil.getWorkbook(fileCompletePath);
        if (wb == null) {
            return;
        }

        Sheet sheet = wb.getSheetAt(0); // 默认表单只有一页
        // 读取每行表,第一行为表头,因此忽略
        for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
            Row row = sheet.getRow(j);
            if (row == null) {
                continue;
            }

            Question question = new Question();
            question.setCourseId(courseId);

            Set<Integer> rightAnswers = new HashSet<>();
            // 读取该行每个单元格
            for (int k = 0; k < row.getLastCellNum(); k++) {
                String cellVal = ExcelUtil.getCellVal(row.getCell(k, Row.CREATE_NULL_AS_BLANK));

                // 第一列题目描述，第二列分数，第三列正答(,分割)，之后所有列为选项
                if (k == 0) {
                    question.setDescription(cellVal);
                } else if (k == 1) {
                    question.setScore(Integer.valueOf(cellVal.substring(0, cellVal.indexOf('.'))));
                    questionDao.addQuestion(question);
                } else if (k == 2) {
                    rightAnswers = this.getRightAnswers(cellVal);
                } else {
                    // k-1才是选项号
                    optionDao.addOption(new Option(question.getId(), cellVal, rightAnswers.contains(k - 3)));
                }
            }
        }
    }

    private Set<Integer> getRightAnswers(String rightAnswersStr) {
        Set<Integer> rightAnswers = new HashSet<>();
        String[] rightAnswerList = rightAnswersStr.split(",");
        for (String rightAnswerStr : rightAnswerList) {
            rightAnswers.add(rightAnswerStr.charAt(0) - 'a');
        }
        return rightAnswers;
    }

    @Override
    public Map<Integer, List<Integer>> generateQuestions(int courseId, Map<Integer, Integer> scoreAndNum) {
        Map<Integer, List<Integer>> quesAndOpts = new LinkedHashMap<>();
        List<Integer> questionIds = questionDao.getRandomQuestionIds(courseId, scoreAndNum);
        for (Integer questionId : questionIds) {
            List<Integer> correctOptIds = optionDao.getOptionsByQuestionIdAndValidity(questionId, true);
            List<Integer> incorrectOptIds = optionDao.getOptionsByQuestionIdAndValidity(questionId, false);

            List<Integer> optionIds = new ArrayList<>();
            // 先取一个正确的
            int random = (int) (Math.random() * correctOptIds.size());
            optionIds.add(correctOptIds.get(random));
            correctOptIds.remove(random);

            // 把剩下的合并，随机取三个
            correctOptIds.addAll(incorrectOptIds);
            for (int i = 0; i < 3; i++) {
                random = (int) (Math.random() * correctOptIds.size());
                optionIds.add(correctOptIds.get(random));
                correctOptIds.remove(random);
            }

            quesAndOpts.put(questionId, optionIds);
        }
        return quesAndOpts;
    }

    @Override
    public Question getQuestionInfo(int questionId) {
        return questionDao.getQuestion(questionId);
    }

    @Override
    public List<QuestionVO> getAllQuestionVOs() throws Exception {
        List<QuestionVO> result = new ArrayList<>();
        Map<Integer, String> courseIdAndName = new HashMap<>();
        for (Question question : questionDao.getAllQuestions()) {
            QuestionVO questionVO = new QuestionVO();
            BeanUtils.copyProperties(question, questionVO);

            // 减少网络I/O开销
            if (!courseIdAndName.containsKey(question.getCourseId())) {
                courseIdAndName.put(question.getCourseId(), userInvoker.getCourseName(question.getCourseId()));
            }
            questionVO.setCourseName(courseIdAndName.get(question.getCourseId()));
            questionVO.setOptionNum(optionDao.getOptionsByQuestionId(question.getId()).size());

            result.add(questionVO);
        }
        return result;
    }

    @Override
    public Option getOptionInfo(int optionId) {
        return optionDao.getOption(optionId);
    }

    @Override
    public List<Option> getOptionsByQuestionId(int questionId) {
        return optionDao.getOptionsByQuestionId(questionId);
    }

    @Override
    public List<QuestionParam> getScoresByCourseId(int courseId) {
        return questionDao.getScoresByCourseId(courseId);
    }

    class QuestionImportThread implements Runnable {
        private int courseId;
        private String fileCompletePath;

        public QuestionImportThread(int courseId, String fileCompletePath) {
            this.courseId = courseId;
            this.fileCompletePath = fileCompletePath;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            importQuestions(courseId, fileCompletePath);
        }
    }
}
