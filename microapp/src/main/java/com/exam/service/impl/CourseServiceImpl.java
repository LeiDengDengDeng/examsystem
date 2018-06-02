package com.exam.service.impl;

import com.exam.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deng on 2017/11/23.
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean addCourse(String name, String profile) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("name", name);
        paramMap.put("profile", profile);
        HttpEntity<Map<String, String>> params = new HttpEntity<>(paramMap, new HttpHeaders());
        return restTemplate.postForObject("http://MICROUSER/courses/add", params, Boolean.class);
    }

    @Override
    public String getAllCourses() {
        return restTemplate.getForObject("http://MICROUSER/courses/getAll", String.class);
    }
}
