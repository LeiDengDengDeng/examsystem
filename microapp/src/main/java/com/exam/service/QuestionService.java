package com.exam.service;

/**
 * Created by deng on 2017/11/22.
 */
public interface QuestionService {
    String getAllQuestions();

    String getOptionsByQuestionId(int questionId);

    String getScoresByCourseId(int courseId);
}
