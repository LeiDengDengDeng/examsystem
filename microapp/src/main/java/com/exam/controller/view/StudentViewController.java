package com.exam.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类的requestmapping不要修改，与拦截器有关
 */
@Controller
@RequestMapping(value = "/student")
public class StudentViewController {
    @RequestMapping(value = "/homepage")
    public String getHomepage() {
        return "student";
    }

    @RequestMapping(value = "/exams")
    public String getExamsPage() {
        return "exams";
    }

    @RequestMapping(value = "/exams/{id}")
    public String getExam(){
        return "exam";
    }
}
