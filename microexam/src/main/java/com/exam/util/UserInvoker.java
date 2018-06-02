package com.exam.util;

import com.exam.bean.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by deng on 2017/11/24.
 */
@Component
public class UserInvoker {
    @Autowired
    private RestTemplate restTemplate;

    private final static String USER_URL_PREFIX = "http://MICROUSER/";
    private ObjectMapper objectMapper = new ObjectMapper();
    private Gson gson = new GsonBuilder().create();

    public String getCourseName(int courseId) throws Exception {
        try {
            String result = restTemplate.getForObject(USER_URL_PREFIX + "/courses/" + courseId + "/get", String.class);
            JsonNode jsonRes = objectMapper.readTree(result);
            if (jsonRes.path("success").asBoolean()) {
                JsonNode courseNode = objectMapper.readTree(jsonRes.path("data").toString());
                String courseName = courseNode.get("name").toString();
                // 去掉首尾的双引号
                return courseName.substring(1, courseName.length() - 1);
            } else {
                throw new Exception("调用MICROUSER接口获取course信息失败:" + jsonRes.path("msg").toString());
            }
        } catch (Exception e) {
            throw new Exception("调用MICROUSER接口获取course信息失败：" + e.getMessage());
        }
    }


    public String getUsernameByEmail(String email)throws Exception{
        try {
            String result = restTemplate.getForObject(USER_URL_PREFIX + "/users/getUserInfo?username="+email, String.class);
            JsonNode jsonRes = objectMapper.readTree(result);
            if (jsonRes.path("success").asBoolean()) {
                JsonNode studentNode = objectMapper.readTree(jsonRes.path("data").toString());
                String username = studentNode.get("username").toString();
                // 去掉首尾的双引号
                return username.substring(1, username.length() - 1);
            } else {
                throw new Exception("调用MICROUSER接口获取学生姓名失败:" + jsonRes.path("msg").toString());
            }
        } catch (Exception e) {
            throw new Exception("调用MICROUSER接口获取学生姓名失败：" + e.getMessage());
        }

    }

    public String getStudentcodeByEmail(String email)throws Exception{
        try {
            String result = restTemplate.getForObject(USER_URL_PREFIX + "/users/getUserInfo?username="+email, String.class);
            JsonNode jsonRes = objectMapper.readTree(result);
            if (jsonRes.path("success").asBoolean()) {
                JsonNode studentNode = objectMapper.readTree(jsonRes.path("data").toString());
                String code = studentNode.get("code").toString();
                // 去掉首尾的双引号
                return code;
            } else {
                throw new Exception("调用MICROUSER接口获取学生学号失败:" + jsonRes.path("msg").toString());
            }
        } catch (Exception e) {
            throw new Exception("调用MICROUSER接口获取学生学号失败：" + e.getMessage());
        }

    }



    /**
     *
     * @param groupId
     * @return
     * @throws Exception
     */
    public List<Student> getStudentsByGroup(int groupId) throws Exception{
        try {
            String result = restTemplate.getForObject(USER_URL_PREFIX + "/users/" + groupId + "/getStudents", String.class);
            JsonNode jsonRes = objectMapper.readTree(result);
            if (jsonRes.path("success").asBoolean()) {
                return gson.fromJson(jsonRes.path("data").toString(), new TypeToken<List<Student>>() {
                }.getType());
            } else {
                throw new Exception("调用MICROUSER接口获取course信息失败:" + jsonRes.path("msg").toString());
            }
        } catch (Exception e) {
            throw new Exception("调用MICROUSER接口获取course信息失败：" + e.getMessage());
        }
    }

}
