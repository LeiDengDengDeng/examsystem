package com.exam.dao;

import com.exam.bean.Paper;
import com.exam.bean.PaperOption;
import com.exam.bean.PaperQuestion;
import com.exam.bean.Score;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by deng on 2017/11/8.
 */
@Repository
public interface PaperDao {
    /* Paper */
    Paper getPaperByExamIdAndStudentId(@Param("examId") String examId, @Param("studentId") String studentId);

    Paper getPaperByCode(@Param("code") String code);

    Paper getPaperById(@Param("id") int id);

    List<Paper> getPapersByExamId(@Param("examId") String examId);

    int updatePaper(@Param("paper") Paper paper);

    void addPaper(@Param("paper") Paper paper);

    /**
     * 获得处于可批改状态的试卷Id，满足的条件如下：1、未批改 2、已交卷或考试已结束
     *
     * @return 获得处于可批改状态的所有试卷Id
     */
    List<Integer> getCheckablePaperIds();
    /* Paper */

    /* PaperQuestion */
    void addPaperQuestion(@Param("paperQuestion") PaperQuestion paperQuestion);

    PaperQuestion getPaperQuestion(@Param("paperId") int paperId, @Param("questionId") int questionId);

    List<PaperQuestion> getPaperQuestions(@Param("paperId") int paperId);

    int updatePaperQuestion(@Param("paperQuestion") PaperQuestion paperQuestion);
    /* PaperQuestion */

    /* PaperOption */
    void addPaperOption(@Param("paperOption") PaperOption paperOption);

    PaperOption getPaperOption(@Param("paperId") int paperId, @Param("optionId") int optionId);

    List<PaperOption> getPaperOptions(@Param("paperId") int paperId, @Param("questionId") int questionId);

    int updatePaperOption(@Param("paperOption") PaperOption paperOption);
    /* PaperOption */

    List<Score> getScoresByExamId(@Param("examId") String examId);
}
