package com.exam.dao;

import com.exam.bean.Course;
import com.exam.bean.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDao {
    Course getCourse(int courseId);

    void insertCourse(Course course);

    List<Course> getAllCourses();
}
