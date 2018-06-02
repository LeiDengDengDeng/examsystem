package com.exam.controller;

import com.exam.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by deng on 2017/11/23.
 */
@RestController
@RequestMapping(value = "/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "/addCourse", method = RequestMethod.POST)
    public boolean addCourse(HttpSession session, String name, String profile) {
        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("teacher")) {
            return false;
        }
        return courseService.addCourse(name, profile);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public String getAllCourses(HttpSession session) {
        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("teacher")) {
            return "无权限";
        }
        return courseService.getAllCourses();
    }
}
