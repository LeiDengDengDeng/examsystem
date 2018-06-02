package com.exam.service.impl;

import com.exam.bean.UserVO;
import com.exam.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by deng on 2017/10/16.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean auth(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("username", username);
        headers.add("password", password);
        String result = restTemplate.postForObject("http://" + "MICROUSER" + "/users/auth", new HttpEntity<String>(headers), String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonRes = objectMapper.readTree(result);
            return jsonRes.path("success").asBoolean() && jsonRes.path("data").asBoolean();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public UserVO getUserInfo(String username) {
        String result = restTemplate.getForObject("http://" + "MICROUSER" + "/users/getUserInfo?username=" + username, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonRes = objectMapper.readTree(result);
            if (jsonRes.path("success").asBoolean()) {
                return objectMapper.readValue(jsonRes.path("data").toString(), UserVO.class);
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String register(UserVO userVO, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("password", password);
        HttpEntity<UserVO> param = new HttpEntity<>(userVO, headers);
        return restTemplate.postForObject("http://MICROUSER/users/register", param, String.class);
    }

    @Override
    public String getAllStudents() {
        return restTemplate.getForObject("http://MICROUSER/users/getAllStudents", String.class);
    }

    @Override
    public String getStudentsByGroup(int groupId) {
        return restTemplate.getForObject("http://MICROUSER/users/" + groupId + "/getStudents", String.class);
    }

    @Override
    public String deleteStudentFromGroup(int groupId, String username) {
        String url = String.format("http://MICROUSER/users/%d/dropStudent?email=%s", groupId, username);
        return restTemplate.postForObject(url, new HttpEntity<String>(new HttpHeaders()), String.class);
    }

}
