package com.exam.dao;

import com.exam.bean.Option;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by deng on 2017/10/21.
 */
@Repository
public interface OptionDao {
    int addOption(Option option);

    /**
     * 获取questionId所有正确／错误选项
     *
     * @param questionId
     * @param right    正确选项or错误选项
     * @return
     */
    List<Integer> getOptionsByQuestionIdAndValidity(@Param("questionId") int questionId, @Param("right") boolean right);

    /**
     * 获取questionId所有选项
     *
     * @param questionId
     * @return
     */
    List<Option> getOptionsByQuestionId(@Param("questionId") int questionId);

    Option getOption(@Param("optionId") int optionId);
}
