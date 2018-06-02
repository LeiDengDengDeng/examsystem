package com.exam.dao;

import com.exam.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by deng on 2017/7/31.
 */
@Repository
public interface UserDao {
    User getUser(String username);

    void insertUser(User user);

    List<User> getUserByGroup(int groupId);

    void insertUserTOGroup(@Param(value="groupId") int groupId, @Param(value="email") String email);

    List<User> getAllStudents();

    void dropStudentFromGroup(@Param(value="groupId") int groupId, @Param(value="email") String email);

    void dropAllStudentsFromGroup(@Param(value="groupId") int groupId);
}
