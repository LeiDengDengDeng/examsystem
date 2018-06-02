package com.exam.service.impl;

import com.exam.bean.ReturnBean;
import com.exam.service.PaperService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by deng on 2017/11/18.
 */
@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ReturnBean<String> generatePaper(String studentId, String examId, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("studentId", studentId);
        headers.add("examId", examId);
        headers.add("code", code);
        String result = restTemplate.postForObject("http://MICROEXAM/papers/generatePaper", new HttpEntity<String>(headers), String.class);
        return new Gson().fromJson(result, new TypeToken<ReturnBean<String>>() {
        }.getType());
    }

    @Override
    public String getPaper(String studentId, String examId) {
        String url = String.format("http://MICROEXAM/papers/getPaper?examId=%s&studentId=%s", examId, studentId);
        return restTemplate.getForObject(url, String.class);
    }

    @Override
    public String getPapers(String examId) {
        String url = String.format("http://MICROEXAM/papers/getPapers?examId=%s", examId);
        return restTemplate.getForObject(url, String.class);
    }

    @Override
    public boolean markQuestion(int paperId, int questionId, boolean marked) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("marked", String.valueOf(marked));
        String url = String.format("http://MICROEXAM/papers/%d/%d/mark", paperId, questionId);
        return restTemplate.postForObject(url, new HttpEntity<String>(headers), Boolean.class);
    }

    @Override
    public boolean selectOption(int paperId, int optionId, boolean selected) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("selected", String.valueOf(selected));
        String url = String.format("http://MICROEXAM/papers/%d/%d/select", paperId, optionId);
        return restTemplate.postForObject(url, new HttpEntity<String>(headers), Boolean.class);
    }

    @Override
    public boolean handPaper(String studentId, int paperId) {
        String url = String.format("http://MICROEXAM/papers/%d/hand?username=%s", paperId, studentId);
        return restTemplate.postForObject(url, new HttpEntity<String>(new HttpHeaders()), Boolean.class);
    }
}
