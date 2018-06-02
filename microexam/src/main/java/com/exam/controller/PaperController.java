package com.exam.controller;

import com.exam.bean.Paper;
import com.exam.bean.ReturnBean;
import com.exam.service.PaperService;
import com.exam.vo.PaperOptionVO;
import com.exam.vo.PaperQuestionVO;
import com.exam.vo.PaperVO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by deng on 2017/11/10.
 */
@Api(value = "试卷功能接口")
@Controller
@RequestMapping(value = "/papers")
public class PaperController {
    @Autowired
    private PaperService paperService;

    @RequestMapping(value = "/generatePaper", method = RequestMethod.POST)
    @ResponseBody
    public ReturnBean<String> generatePaper(@RequestHeader("studentId") String studentId, @RequestHeader("examId") String examId, @RequestHeader("code") String code) {
        ReturnBean<String> result = new ReturnBean<>();
        try {
            result.setSuccess(true);
            result.setData(convertPaperVO2JO(paperService.generatePaper(studentId, examId, code)).toString());
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    @ApiOperation(value = "获得试卷详情", notes = "根据试卷id获得试卷")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "examId", value = "考试id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "studentId", value = "学生id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getPaper", method = RequestMethod.GET)
    @ResponseBody
    public ReturnBean<String> getPaperInformation(@RequestParam(value = "examId") String examId, @RequestParam(value = "studentId") String studentId) {
        ReturnBean<String> result = new ReturnBean();
        result.setSuccess(false);

        PaperVO paperVO = paperService.getPaperInformation(examId, studentId);
        if (paperVO == null) {
            result.setMsg("获得试卷信息失败");
        } else {
            result.setData(convertPaperVO2JO(paperVO).toString());
            result.setSuccess(true);
        }
        return result;
    }

    @ApiOperation(value = "获得某场考试的所有试卷的基本信息", notes = "根据examid获得试卷们")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "examId", value = "考试id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getPapers", method = RequestMethod.GET)
    @ResponseBody
    public ReturnBean<List<Paper>> getPapersByExamId(@RequestParam(value = "examId") String examId) {
        ReturnBean<List<Paper>> result = new ReturnBean();
        result.setSuccess(false);

        List<Paper> papers = paperService.getPapersByExamId(examId);
        if (papers.size() == 0) {
            result.setMsg("获得试卷信息失败");
        } else {
            result.setData(papers);
            result.setSuccess(true);
        }
        return result;
    }

    @ApiOperation(value = "标记/取消标记考题")
    @RequestMapping(value = "{paperId}/{questionId}/mark", method = RequestMethod.POST)
    @ResponseBody
    public boolean markPaperQuestion(@PathVariable(value = "paperId") int paperId, @PathVariable(value = "questionId") int questionId, @RequestHeader("marked") boolean marked) {
        return paperService.markPaperQuestion(paperId, questionId, marked);
    }

    @ApiOperation(value = "选择/取消选择选项")
    @RequestMapping(value = "{paperId}/{optionId}/select", method = RequestMethod.POST)
    @ResponseBody
    public boolean selectPaperOption(@PathVariable(value = "paperId") int paperId, @PathVariable(value = "optionId") int optionId, @RequestHeader("selected") boolean selected) {
        return paperService.selectPaperOption(paperId, optionId, selected);
    }

    @ApiOperation(value = "提前交卷")
    @RequestMapping(value = "{paperId}/hand", method = RequestMethod.POST)
    @ResponseBody
    public boolean handPaper(@RequestParam("username") String username, @PathVariable(value = "paperId") int paperId) {
        return paperService.handPaper(username, paperId);
    }

    private JsonObject convertPaperVO2JO(PaperVO paperVO) {
        JsonObject paperJO = new JsonObject();
        if (paperVO.isChecked()) {
            // 批改过后显示分数
            paperJO.addProperty("score", paperVO.getScore());
        }
        paperJO.addProperty("paperId", paperVO.getId());
        paperJO.addProperty("handed", paperVO.isHanded());

        JsonArray questionJA = new JsonArray();
        for (PaperQuestionVO question : paperVO.getPaperQuestionVOs()) {
            JsonObject questionJO = new JsonObject();

            questionJO.addProperty("questionId", question.getQuestionId());
            questionJO.addProperty("description", question.getDescription());
            questionJO.addProperty("score", question.getScore());
            if (paperVO.isStarted()) {
                // 开考后显示题目标记
                questionJO.addProperty("isMarked", question.isMarked());
            }
            if (paperVO.isChecked()) {
                // 批改过后显示是否得分
                questionJO.addProperty("isScored", question.isScored());
            }

            JsonArray optionJA = new JsonArray();
            for (PaperOptionVO option : question.getPaperOptionVOs()) {
                JsonObject optionJO = new JsonObject();
                optionJO.addProperty("content", option.getContent());
                optionJO.addProperty("optionId", option.getOptionId());
                if (paperVO.isStarted()) {
                    // 开考后显示选项是否被选中
                    optionJO.addProperty("isSelected", option.isSelected());
                }
                if (paperVO.isEnded()) {
                    // 考试结束后显示正确答案
                    optionJO.addProperty("isRight", option.isRight());
                }

                optionJA.add(optionJO);
            }
            questionJO.add("options", optionJA);
            questionJA.add(questionJO);
        }
        paperJO.add("questions", questionJA);
        return paperJO;
    }
}
