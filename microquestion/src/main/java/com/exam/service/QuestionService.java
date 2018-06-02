package com.exam.service;

import com.exam.bean.Option;
import com.exam.bean.Question;
import com.exam.bean.QuestionParam;
import com.exam.vo.QuestionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by deng on 2017/10/21.
 */
public interface QuestionService {
    void createImportQuestionsTask(int courseId, String fileCompletePath);

    /**
     * 导入具体课程下的试题
     *
     * @param courseId
     * @param fileCompletePath 文件路径
     */
    void importQuestions(int courseId, String fileCompletePath);

    /**
     * 随机生成试题id和试题下的选项id
     *
     * @param courseId
     * @param scoreAndNum
     * @return
     */
    Map<Integer, List<Integer>> generateQuestions(int courseId, Map<Integer, Integer> scoreAndNum);

    /**
     * 获得单个question的基本信息
     *
     * @param questionId
     * @return
     */
    Question getQuestionInfo(int questionId);

    /**
     * 获得所有question信息，除基本信息外还包括question下option总个数和所属course名
     *
     * @return
     * @throws Exception 获取course信息失败
     */
    List<QuestionVO> getAllQuestionVOs() throws Exception;

    Option getOptionInfo(int optionId);

    /**
     * 获得question下所有option信息
     *
     * @param questionId
     * @return
     */
    List<Option> getOptionsByQuestionId(int questionId);

    List<QuestionParam> getScoresByCourseId(int courseId);
}
