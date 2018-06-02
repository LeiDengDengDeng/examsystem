package com.exam.service.impl;

import com.exam.bean.Course;
import com.exam.dao.CourseDao;
import com.exam.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liying on 2017/11/23.
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;
    @Override
    public void insertCourse(Course course) {
        courseDao.insertCourse(course);
    }

    @Override
    public Course getCourse(int courseId) {
        Course course=courseDao.getCourse(courseId);
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses= courseDao.getAllCourses();
        return courses;
    }
}
