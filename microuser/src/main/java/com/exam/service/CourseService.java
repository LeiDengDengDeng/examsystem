package com.exam.service;

import com.exam.bean.Course;

import java.util.List;

/**
 * Created by liying on 2017/11/23.
 */
public interface CourseService {
    void insertCourse(Course course);
    Course getCourse(int courseId);
    List<Course> getAllCourses();
}
