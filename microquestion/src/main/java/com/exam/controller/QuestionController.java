package com.exam.controller;

import com.exam.bean.Option;
import com.exam.bean.Question;
import com.exam.bean.QuestionParam;
import com.exam.bean.ReturnBean;
import com.exam.service.QuestionService;
import com.exam.vo.QuestionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by deng on 2017/11/8.
 */
@Api(value = "题库功能接口")
@RestController
@RequestMapping(value = "/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @ApiOperation(value = "生成考题及考题选项", notes = "随机生成考题和考题下的选项")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "courseId", value = "课程Id", required = true),
            @ApiImplicitParam(paramType = "body", name = "scoreAndNum", value = "分数及题数", required = true, dataType = "Map<Integer, Integer>")
    })
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ReturnBean<Map<Integer, List<Integer>>> generate(@RequestHeader("courseId") int courseId, @RequestBody Map<Integer, Integer> scoreAndNum) {
        ReturnBean<Map<Integer, List<Integer>>> result = new ReturnBean<>();
        try {
            result.setData(questionService.generateQuestions(courseId, scoreAndNum));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMsg("QuestionService错误：" + e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/{questionId}/get", method = RequestMethod.GET)
    public ReturnBean<Question> getQuestionInfo(@PathVariable("questionId") int questionId) {
        ReturnBean<Question> result = new ReturnBean<>();
        Question question = questionService.getQuestionInfo(questionId);
        if (question != null) {
            result.setData(question);
            result.setSuccess(true);
        } else {
            result.setMsg("对应问题不存在");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ReturnBean<List<QuestionVO>> getAllQuestionInfo() {
        ReturnBean<List<QuestionVO>> result = new ReturnBean<>();
        try {
            result.setData(questionService.getAllQuestionVOs());
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("获取题库失败：" + e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/options/{optionId}/get", method = RequestMethod.GET)
    public ReturnBean<Option> getOptionInfo(@PathVariable("optionId") int optionId) {
        ReturnBean<Option> result = new ReturnBean<>();
        Option option = questionService.getOptionInfo(optionId);
        if (option != null) {
            result.setData(option);
            result.setSuccess(true);
        } else {
            result.setMsg("对应选项不存在");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/options/{questionId}/getOptions", method = RequestMethod.GET)
    public ReturnBean<List<Option>> getOptionsByQuestionId(@PathVariable("questionId") int questionId) {
        ReturnBean<List<Option>> result = new ReturnBean<>();
        result.setData(questionService.getOptionsByQuestionId(questionId));
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/getScores", method = RequestMethod.GET)
    public ReturnBean<List<QuestionParam>> getScoresByCourseId(@RequestParam("courseId") int courseId) {
        ReturnBean<List<QuestionParam>> result = new ReturnBean<>();
        result.setData(questionService.getScoresByCourseId(courseId));
        result.setSuccess(true);
        return result;
    }
}
