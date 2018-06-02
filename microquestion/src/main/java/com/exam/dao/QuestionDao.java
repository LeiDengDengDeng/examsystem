package com.exam.dao;

import com.exam.bean.Question;
import com.exam.bean.QuestionParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by deng on 2017/10/21.
 */
@Repository
public interface QuestionDao {
    int addQuestion(@Param("question") Question question);

    /**
     * 根据分数和分数下的题数随机获得某门课程题库中的题
     *
     * @param courseId
     * @param scoreAndNum
     * @return
     */
    List<Integer> getRandomQuestionIds(@Param("courseId") int courseId, @Param("scoreAndNum") Map<Integer, Integer> scoreAndNum);

    Question getQuestion(@Param("questionId") int questionId);

    List<Question> getAllQuestions();

    /**
     * 获得指定课程下的所有分数类型和分数下的题目总数
     *
     * @param courseId
     * @return
     */
    List<QuestionParam> getScoresByCourseId(@Param("courseId") int courseId);
}
