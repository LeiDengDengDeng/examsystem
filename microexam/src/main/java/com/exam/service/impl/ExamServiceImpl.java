package com.exam.service.impl;

import com.exam.bean.*;
import com.exam.dao.ExamDao;
import com.exam.dao.PaperDao;
import com.exam.service.ExamService;
import com.exam.util.*;
import com.exam.vo.ExamVO;
import com.exam.vo.ExamVOInput;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by disinuo on 17/11/16.
 */
@Service
@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
public class ExamServiceImpl implements ExamService {
    @Autowired
    private ExamDao examDao;
    @Autowired
    private PaperDao paperDao;
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private UserInvoker userInvoker;

    @Value("${file.download}")
    private String downloadPath;

    @Override
    public boolean createExam(ExamVOInput examVOInput) throws Exception {
        Exam exam = new Exam();
        String examId = UUID.randomUUID().toString();
        /* 创建考试 */
        exam.setId(examId);
        exam.setStartTime(examVOInput.getStartTime());
        exam.setEndTime(examVOInput.getEndTime());
        exam.setCourseId(examVOInput.getCourseId());
        exam.setName(examVOInput.getExamName());
        examDao.addExam(exam);
        for (int score : examVOInput.getQuestions().keySet()) {
            ExamParam examParam = new ExamParam();
            examParam.setExamId(examId);
            examParam.setScore(score);
            examParam.setNum(examVOInput.getQuestions().get(score));
            examDao.addExamParam(examParam);
        }
        /*给每个学生创建空白试卷，以及对应的考试密码*/
        List<Student> students = userInvoker.getStudentsByGroup(examVOInput.getGroupId());
        for (Student student : students) {
            Paper paper = new Paper();
            paper.setStudentId(student.getUsername());
            paper.setExamId(examId);
            //考试的访问密码
            String code = UUID.randomUUID().toString();
            paper.setCode(code);
            paperDao.addPaper(paper);
            String title = ExamNoticer.createEmailTitle(exam.getName(), exam.getStartTime());
            String content = ExamNoticer.createEmailContent(exam.getName(), exam.getStartTime(),
                    exam.getEndTime(), student.getName(), code);
            mailUtil.sendEmail(student.getUsername(), title, content, false);

        }


        return true;
    }


    @Override
    public ExamVO getExamByExamId(String examId) throws Exception {
        return transferBeanToVO(examDao.getExamByExamId(examId));
    }

    @Override
    public List<ExamVO> getExamsByStudentId(String studentId) throws Exception {
        List<ExamVO> result = new ArrayList<>();
        for (Exam exam : examDao.getExamsByStudentId(studentId)) {
            result.add(transferBeanToVO(exam));
        }
        return result;
    }

    @Override
    public List<ExamVO> getAllExams() throws Exception {
        List<ExamVO> result = new ArrayList<>();
        for (Exam exam : examDao.getAllExams()) {
            result.add(transferBeanToVO(exam));
        }
        return result;
    }

    @Override
    public List<ExamParam> getExamParamByExamId(String examId) {
        return examDao.getExamParamByExamId(examId);
    }

    @Override
    public boolean generateTranscriptByExamId(String examId) {
        boolean isgenerated = true;
        List<Score> scores = paperDao.getScoresByExamId(examId);
        if (scores == null) {
            return false;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        String examname = examDao.getExamNameByExamId(examId);
        String sheetname = examname + "成绩单";
        //创建表头
        HSSFSheet sheet = workbook.createSheet(sheetname);
        String titles = "学号,姓名,分数";
        ExcelUtil.createTitle(workbook, sheet, titles);
        for (int rowNum = 1; rowNum <= scores.size(); ++rowNum) {
            Score score = scores.get(rowNum - 1);
            String studentid = score.getStudentId();
            //得到学号，姓名，分数
            String studentNumber = null;
            try {
                studentNumber = userInvoker.getStudentcodeByEmail(studentid);
            } catch (Exception e) {
                e.printStackTrace();
                isgenerated = false;
            }
            String studentName = null;
            try {
                studentName = userInvoker.getUsernameByEmail(studentid);
            } catch (Exception e) {
                e.printStackTrace();
                isgenerated = false;
            }
            int scoreNumber = score.getScore();
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(studentNumber);
            row.createCell(1).setCellValue(studentName);
            row.createCell(2).setCellValue(scoreNumber);
        }
        String fileName = "/Users/chentiange/Desktop/microexam/transcript/" + sheetname + ".xls";
        ExcelUtil.writeToExcel(workbook, fileName);
        return isgenerated;
    }

    @Override
    public String generateTranscriptCSVByExamId(String examId) {
        List<Score> scores = paperDao.getScoresByExamId(examId);
        if (scores == null) {
            return null;
        }

        List<String> csvData = new ArrayList<>();
        csvData.add("code,name,score");
        for (int rowNum = 1; rowNum <= scores.size(); ++rowNum) {
            Score score = scores.get(rowNum - 1);
            String studentId = score.getStudentId();
            //得到学号，姓名，分数
            String studentNumber = null;
            try {
                studentNumber = userInvoker.getStudentcodeByEmail(studentId);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            String studentName = null;
            try {
                studentName = userInvoker.getUsernameByEmail(studentId);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            csvData.add(studentNumber + "," + studentName + "," + score.getScore());
        }

        String filePath = downloadPath + "scoreSheet.csv";
        CSVUtil.exportCsv(filePath, csvData);
        return filePath;
    }

    private ExamVO transferBeanToVO(Exam exam) throws Exception {
        ExamVO examVO = new ExamVO();
        BeanUtils.copyProperties(exam, examVO);

        // 判断考试状态
        examVO.setState("进行中");
        if (!TimeUtil.isEarlierThanCurrentTime(exam.getStartTime())) {
            examVO.setState("未开始");
        } else if (TimeUtil.isEarlierThanCurrentTime(exam.getEndTime())) {
            examVO.setState("已结束");
        }

        // 调用user微服务获得课程名
        examVO.setCourseName(userInvoker.getCourseName(exam.getCourseId()));

        // 根据考试参数算出考试总分
        int totalScore = 0;
        List<ExamParam> examParams = examDao.getExamParamByExamId(exam.getId());
        for (ExamParam examParam : examParams) {
            totalScore += examParam.getScore() * examParam.getNum();
        }
        examVO.setTotalScore(totalScore);

        return examVO;
    }
}
