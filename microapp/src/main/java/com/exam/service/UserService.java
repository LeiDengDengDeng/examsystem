package com.exam.service;

import com.exam.bean.UserVO;

/**
 * Created by deng on 2017/8/13.
 */
public interface UserService {
    boolean auth(String username, String password);

    UserVO getUserInfo(String username);

    String register(UserVO userVO, String password);

    String getAllStudents();

    String getStudentsByGroup(int groupId);

    String deleteStudentFromGroup(int groupId, String username);
}
