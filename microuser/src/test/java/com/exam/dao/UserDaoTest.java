package com.exam.dao;

import com.exam.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by liying on 2017/11/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Test
    public void insertUserTOGroup() throws Exception {
//        userDao.insertUserTOGroup(1,"123");

    }

    @Test
    public void getAllStudents() throws Exception {
//            List<User> list=userDao.getAllStudents();
//            for (User item:list) {
//                System.out.println(item.getEmail());
//            }

    }

}