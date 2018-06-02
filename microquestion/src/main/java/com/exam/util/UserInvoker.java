package com.exam.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by deng on 2017/11/24.
 */
@Component
public class UserInvoker {
    @Autowired
    private RestTemplate restTemplate;

    private final static String USER_URL_PREFIX = "http://MICROUSER/";
    private ObjectMapper objectMapper = new ObjectMapper();

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
}
