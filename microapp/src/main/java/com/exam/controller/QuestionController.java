package com.exam.controller;

import com.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by deng on 2017/11/22.
 */
@RestController
@RequestMapping(value = "/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public String getAllQuestions(HttpSession session) {
        if (session.getAttribute("username") == null || !session.getAttribute("role").equals("teacher")) {
            return "无权限";
        }
        return questionService.getAllQuestions();
    }

    @RequestMapping(value = "/getOptionsByQuestionId", method = RequestMethod.GET)
    public String getOptionsByQuestionId(HttpSession session, int questionId) {
        if (session.getAttribute("username") == null || !session.getAttribute("role").equals("teacher")) {
            return "无权限";
        }
        return questionService.getOptionsByQuestionId(questionId);
    }

    @RequestMapping(value = "/getScoresByCourseId", method = RequestMethod.GET)
    public String getScoresByCourseId(HttpSession session, int courseId) {
        if (session.getAttribute("username") == null || !session.getAttribute("role").equals("teacher")) {
            return "无权限";
        }
        return questionService.getScoresByCourseId(courseId);
    }
}
