package com.exam.service.impl;

import com.exam.bean.ExamVOInput;
import com.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by deng on 2017/11/20.
 */
@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getAllExams() {
        return restTemplate.getForObject("http://MICROEXAM/exams/getAll", String.class);
    }

    @Override
    public String getExamParamsByExamId(String examId) {
        String url = String.format("http://MICROEXAM/exams/%s/getExamParams", examId);
        return restTemplate.getForObject(url, String.class);
    }

    @Override
    public String getExamByExamId(String examId) {
        String url = String.format("http://MICROEXAM/exams/%s/get", examId);
        return restTemplate.getForObject(url, String.class);
    }

    public String getExamsByStudentId(String studentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("studentId", studentId);
        return restTemplate.postForObject("http://MICROEXAM/exams/getStudentExams", new HttpEntity<String>(headers), String.class);
    }

    @Override
    public String createExam(ExamVOInput examVOInput) {
        HttpEntity<ExamVOInput> param = new HttpEntity<>(examVOInput, new HttpHeaders());
        return restTemplate.postForObject("http://MICROEXAM/exams/create", param, String.class);
    }
}
