package com.exam.controller;

import com.exam.bean.Course;
import com.exam.bean.ReturnBean;
import com.exam.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liying on 2017/11/23.
 */
@Api(value="课程功能接口")
@RestController
@RequestMapping(value = "/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;


    @ApiOperation(value = "添加一门课程", notes = "课程参数：课程名（name），课程介绍（profile）")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Boolean addCourse(@RequestBody Course course){
        try {
            courseService.insertCourse(course);
        }catch (Exception e){
            return false;
        }
        return true;

    }

    @ApiOperation(value = "获取一门课程", notes = "参数课程号（courseId）")
    @RequestMapping(value = "/{courseId}/get", method = RequestMethod.GET)
    public ReturnBean<Course> getCourse(@PathVariable("courseId") int courseId){
        ReturnBean<Course> returnBean=new ReturnBean<>();
        Course course=courseService.getCourse(courseId);
        if (course==null) {
            returnBean.setMsg("fail to get");
            returnBean.setSuccess(false);
        }else {
            returnBean.setData(course);
            returnBean.setSuccess(true);
        }
        return returnBean;

    }

    @ApiOperation(value = "获取所有课程")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ReturnBean<List<Course>> getAllCourses(){
        ReturnBean<List<Course>> returnBean=new ReturnBean<>();
        List<Course> course=courseService.getAllCourses();
        if (course==null) {
            returnBean.setMsg("fail to get");
            returnBean.setSuccess(false);
        }else {
            returnBean.setSuccess(true);
            returnBean.setData(course);
        }
        return returnBean;

    }


}
