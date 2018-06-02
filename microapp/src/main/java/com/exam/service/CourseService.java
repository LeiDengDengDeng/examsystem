package com.exam.service;

/**
 * Created by deng on 2017/11/23.
 */
public interface CourseService {
    boolean addCourse(String name, String profile);

    String getAllCourses();
}
