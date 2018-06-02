package com.exam.service;

import com.exam.bean.User;
import com.exam.vo.UserVO;

import java.util.List;

/**
 * Created by deng on 2017/8/13.
 */
public interface UserService {
    boolean auth(String username,String password);

    UserVO getUserInfo(String username) throws Exception;

    void register(User user) throws Exception;

    List<UserVO> getUserByGroup(int groupId);

    void insertUserTOGroup(int groupId,String email);

    void importUsers(int courseId, String fileCompletePath) throws Exception;

    List<UserVO> getAllStudents();

    void dropStudentFromGroup(int groupId,String email);
}
