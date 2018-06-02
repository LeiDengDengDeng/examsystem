package com.exam.controller;

import com.exam.bean.ExamParam;
import com.exam.bean.ReturnBean;
import com.exam.service.ExamService;
import com.exam.vo.ExamVO;
import com.exam.vo.ExamVOInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by disinuo on 17/11/16.
 */
@Api(value = "考试功能接口")
@RestController
@RequestMapping(value = "/exams")
public class ExamController {
    @Autowired
    private ExamService examService;

    @ApiOperation(value = "创建考试", notes = "根据老师输入的参数创建考试")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ReturnBean<String> createExam(
            @RequestBody ExamVOInput examVOInput
    ) {
        ReturnBean<String> result = new ReturnBean();
        boolean res = false;
        try {
            res = examService.createExam(examVOInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setSuccess(res);
        return result;
    }

    @ApiOperation(value = "获得考试信息", notes = "根据考试id获得考试信息")
    @RequestMapping(value = "{examId}/get", method = RequestMethod.GET)
    public ReturnBean<ExamVO> getExamInfoById(@PathVariable("examId") String examId) {
        ReturnBean<ExamVO> result = new ReturnBean();
        try {
            result.setData(examService.getExamByExamId(examId));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("获取考试信息失败：" + e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获得学生的考试信息", notes = "根据学生id获得学生所有的考试信息")
    @RequestMapping(value = "/getStudentExams", method = RequestMethod.POST)
    public ReturnBean<List<ExamVO>> getStudentExams(@RequestHeader("studentId") String studentId) {
        ReturnBean<List<ExamVO>> result = new ReturnBean();
        try {
            result.setData(examService.getExamsByStudentId(studentId));
            result.setSuccess(true);
        } catch (Exception e) {
//            e.printStackTrace();
            result.setSuccess(false);
            result.setMsg("获取学生考试信息失败：" + e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获得全部考试信息")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ReturnBean<List<ExamVO>> getExamInfoById() {
        ReturnBean<List<ExamVO>> result = new ReturnBean();
        try {
            result.setData(examService.getAllExams());
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("获取考试信息失败：" + e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获得考试参数信息", notes = "根据考试id获得考试参数信息")
    @RequestMapping(value = "{examId}/getExamParams", method = RequestMethod.GET)
    public ReturnBean<List<ExamParam>> getExamParamsById(@PathVariable("examId") String examId) {
        ReturnBean<List<ExamParam>> result = new ReturnBean();
        try {
            result.setData(examService.getExamParamByExamId(examId));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("获取考试信息失败：" + e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "生成某场考试的成绩单", notes = "根据考试id生成该场考试所有参加考试人员的成绩单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "examId", value = "考试id", required = true, dataType = "String")
    })
    @RequestMapping(value = "{examId}/generateTranscript")
    @ResponseBody
    public String generateTranscriptByExamId(HttpServletResponse response, @PathVariable(value = "examId") String examId) {
        String completePath = examService.generateTranscriptCSVByExamId(examId);
        File file = new File(completePath);
        if (file.exists()) {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition",
                    "attachment;fileName="+file.getName());// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    return "failed:" + e.getMessage();
                }
            }
        }
        return "";
    }
}
