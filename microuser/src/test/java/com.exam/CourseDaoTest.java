package com.exam;

import com.exam.bean.Course;
import com.exam.dao.CourseDao;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by liying on 2017/11/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseDaoTest {
    @Autowired
    private CourseDao courseDao;

    @org.junit.Test
    public void getCourse() throws Exception {
//        assertEquals(courseDao.getCourse(1).getCourseId(), 1);
    }

    @org.junit.Test
    public void insertCourse() throws Exception {
//        courseDao.insertCourse(new Course("软件工程","这是一门无聊的课程"));
    }

    @org.junit.Test
    public void getAllCourses() throws Exception {
//        courseDao.getAllCourses();
    }

}