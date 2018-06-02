package com.exam.controller;

import com.exam.bean.ReturnBean;
import com.exam.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by deng on 2017/11/18.
 */
@RestController
@RequestMapping("/papers")
public class PaperController {
    @Autowired
    private PaperService paperService;

    @RequestMapping(value = "/generatePaper", method = RequestMethod.POST)
    public ReturnBean<String> generatePaper(HttpSession session, String examId, String code) {
        return paperService.generatePaper((String) session.getAttribute("username"), examId, code);
    }

    @RequestMapping(value = "/getPaper", method = RequestMethod.GET)
    public String getPaper(HttpSession session, String examId) {
        return paperService.getPaper((String) session.getAttribute("username"), examId);
    }

    @RequestMapping(value = "/getPaperByTeacher", method = RequestMethod.GET)
    public String getPaper(HttpSession session, String studentId, String examId) {
        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("teacher")) {
            return "无权限";
        }
        return paperService.getPaper(studentId, examId);
    }

    @RequestMapping(value = "/getPapers", method = RequestMethod.GET)
    public String getPapers(HttpSession session, String examId) {
        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("teacher")) {
            return "无权限";
        }
        return paperService.getPapers(examId);
    }

    @RequestMapping(value = "/markQuestion", method = RequestMethod.POST)
    public boolean markQuestion(HttpSession session, int paperId, int questionId, boolean marked) {
        if (session.getAttribute("role") == null) {
            return false;
        }
        return paperService.markQuestion(paperId, questionId, marked);
    }

    @RequestMapping(value = "/selectOption", method = RequestMethod.POST)
    public boolean selectOption(HttpSession session, int paperId, int optionId, boolean selected) {
        if (session.getAttribute("role") == null) {
            return false;
        }
        return paperService.selectOption(paperId, optionId, selected);
    }

    @RequestMapping(value = "/hand", method = RequestMethod.POST)
    public boolean handPaper(HttpSession session, int paperId) {
        return paperService.handPaper((String) session.getAttribute("username"), paperId);
    }
}
