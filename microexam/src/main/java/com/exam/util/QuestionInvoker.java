package com.exam.util;

import com.exam.vo.PaperOptionVO;
import com.exam.vo.PaperQuestionVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by deng on 2017/11/8.
 */
@Component
public class QuestionInvoker {
    @Autowired
    private RestTemplate restTemplate;

    private final static String QUESTION_URL_PREFIX = "http://MICROQUESTION/";
    private final static Logger logger = LoggerFactory.getLogger(QuestionInvoker.class);

    private ObjectMapper objectMapper = new ObjectMapper();
    private Gson gson = new GsonBuilder().create();

    public Map<Integer, List<Integer>> generateQuestions(int courseId, Map<Integer, Integer> scoreAndNum) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("courseId", String.valueOf(courseId));
        HttpEntity<Map<Integer, Integer>> param = new HttpEntity<>(scoreAndNum, headers);
        try {
            String result = restTemplate.postForObject(QUESTION_URL_PREFIX + "/questions/generate", param, String.class);
            JsonNode jsonRes = objectMapper.readTree(result);
            if (jsonRes.path("success").asBoolean()) {
                return gson.fromJson(jsonRes.path("data").toString(), new TypeToken<Map<Integer, List<Integer>>>() {
                }.getType());
            } else {
                throw new Exception("调用MICROQUESTION接口生成question失败:" + jsonRes.path("msg").toString());
            }
        } catch (IOException e) {
            logger.error("调用MICROQUESTION接口生成question失败：" + e.getMessage());
            throw new Exception("调用MICROQUESTION接口生成question失败：" + e.getMessage());
        }
    }

    public PaperQuestionVO getQuestionInfo(int questionId) throws Exception {
        try {
            String result = restTemplate.getForObject(QUESTION_URL_PREFIX + "/questions/" + questionId + "/get", String.class);
            JsonNode jsonRes = objectMapper.readTree(result);
            if (jsonRes.path("success").asBoolean()) {
                JsonNode questionNode = objectMapper.readTree(jsonRes.path("data").toString());

                PaperQuestionVO paperQuestionVO = new PaperQuestionVO();
                paperQuestionVO.setDescription(questionNode.path("description").toString());
                paperQuestionVO.setScore(questionNode.path("score").asInt());
                return paperQuestionVO;
            } else {
                throw new Exception("调用MICROQUESTION接口获取question信息失败:" + jsonRes.path("msg").toString());
            }
        } catch (Exception e) {
            logger.error("调用MICROQUESTION接口获取question信息失败：" + e.getMessage());
            throw new Exception("调用MICROQUESTION接口获取question信息失败：" + e.getMessage());
        }
    }

    public PaperOptionVO getOptionInfo(int optionId) throws Exception {
        try {
            String result = restTemplate.getForObject(QUESTION_URL_PREFIX + "/questions/options/" + optionId + "/get", String.class);
            JsonNode jsonRes = objectMapper.readTree(result);
            if (jsonRes.path("success").asBoolean()) {
                JsonNode optionNode = objectMapper.readTree(jsonRes.path("data").toString());

                PaperOptionVO paperOptionVO = new PaperOptionVO();
                paperOptionVO.setContent(optionNode.path("content").toString());
                paperOptionVO.setRight(optionNode.path("right").asBoolean());
                return paperOptionVO;
            } else {
                throw new Exception("调用MICROQUESTION接口获取option信息失败:" + jsonRes.path("msg").toString());
            }
        } catch (Exception e) {
            logger.error("调用MICROQUESTION接口获取option信息失败：" + e.getMessage());
            throw new Exception("调用MICROQUESTION接口获取option信息失败：" + e.getMessage());
        }
    }
}
