package com.exam.service.impl;

import com.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by deng on 2017/11/22.
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getAllQuestions() {
        return restTemplate.getForObject("http://MICROQUESTION/questions/getAll", String.class);
    }

    @Override
    public String getOptionsByQuestionId(int questionId) {
        String url = String.format("http://MICROQUESTION/questions/options/%d/getOptions", questionId);
        return restTemplate.getForObject(url, String.class);
    }

    @Override
    public String getScoresByCourseId(int courseId) {
        String url = String.format("http://MICROQUESTION/questions/getScores?courseId=%d", courseId);
        return restTemplate.getForObject(url, String.class);
    }
}
