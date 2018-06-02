package com.exam.bean;

import lombok.Data;

/**
 * Created by liying on 2017/11/23.
 */
@Data
public class Course {
    int courseId;
    String name;
    String profile;

    public Course(String name, String profile) {
        this.name = name;
        this.profile = profile;
    }

    public Course(){

    }
    public Course(int courseId, String name, String profile) {
        this.courseId = courseId;
        this.name = name;
        this.profile = profile;
    }
}
