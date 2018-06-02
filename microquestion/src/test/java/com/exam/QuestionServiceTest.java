package com.exam;

import com.exam.service.QuestionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceTest {
    @Autowired
    private QuestionService questionService;

    @Test
    public void generatePaper() {
//        Map<Integer, Integer> scoreAndNum = new HashMap<>();
//        scoreAndNum.put(1, 2);
//        scoreAndNum.put(2, 2);
//        scoreAndNum.put(3, 1);
//        Map<Integer, List<Integer>> result = questionService.generateQuestions(1, scoreAndNum);
//        assertNotNull(result);
    }

}
