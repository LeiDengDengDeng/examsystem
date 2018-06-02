package com.exam.controller.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类的requestmapping不要修改，与拦截器有关
 */
@Controller
@RequestMapping(value = "/teacher")
@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
public class TeacherViewController {
    @Value("${app.gateway}")
    private String gateway;

    @RequestMapping(value = "/homepage")
    public String getHomepage() {
        return "teacher";
    }

    @RequestMapping(value = "/questions")
    public String getQuestionsPage(Model model) {
        model.addAttribute("gateway",gateway);
        return "questions";
    }

    @RequestMapping(value = "/courses")
    public String getCoursesPage() {
        return "courses";
    }

    @RequestMapping(value = "/students")
    public String getStudentsPage(Model model) {
        model.addAttribute("gateway",gateway);
        return "students";
    }

    @RequestMapping(value = "/groups")
    public String getGroupsPage() {
        return "groups";
    }

    @RequestMapping(value = "/examParam")
    public String getExamParamPage() {
        return "examParam";
    }

    @RequestMapping(value = "/exams")
    public String getExamsPage(Model model) {
        model.addAttribute("gateway",gateway);
        return "teacherExams";
    }

    @RequestMapping(value = "/exams/{id}")
    public String getExamPage() {
        return "teacherExam";
    }

}
