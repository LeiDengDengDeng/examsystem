package com.exam.controller;

import com.exam.bean.ExamVOInput;
import com.exam.bean.ReturnBean;
import com.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by deng on 2017/11/21.
 */
@RestController
@RequestMapping("/exams")
public class ExamController {
    @Autowired
    private ExamService examService;

    @RequestMapping(value = "/getAllExams", method = RequestMethod.GET)
    public String getAllExams(HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "无权限";
        }
        return examService.getAllExams();
    }

    @RequestMapping(value = "/getExamParams", method = RequestMethod.GET)
    public String getExamParams(HttpSession session, String examId) {
        if (session.getAttribute("username") == null) {
            return "无权限";
        }
        return examService.getExamParamsByExamId(examId);
    }

    @RequestMapping(value = "/getExam", method = RequestMethod.GET)
    public String getExam(HttpSession session, String examId) {
        if (session.getAttribute("username") == null) {
            return "无权限";
        }
        return examService.getExamByExamId(examId);
    }

    @RequestMapping(value = "/getStudentExams", method = RequestMethod.GET)
    public String getStudentExams(HttpSession session) {
        return examService.getExamsByStudentId((String) session.getAttribute("username"));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createExam(HttpSession session, ExamVOInput examVOInput) {
        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("teacher")) {
            return "无权限";
        }
        return examService.createExam(examVOInput);
    }
}
