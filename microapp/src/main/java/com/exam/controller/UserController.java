package com.exam.controller;

import com.exam.bean.ReturnBean;
import com.exam.bean.UserVO;
import com.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by deng on 2017/10/18.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ReturnBean<String> login(HttpSession session, String username, String password) {
        ReturnBean<String> result = new ReturnBean<>();
        if (userService.auth(username, password)) {
            UserVO userInfo = userService.getUserInfo(username);
            session.setAttribute("role", userInfo.getRole());
            session.setAttribute("username", username);
            result.setData(userInfo.getRole());
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(String username, String password, String name, int code) {
        UserVO userVO = new UserVO();
        userVO.setUsername(username);
        userVO.setCode(code);
        userVO.setName(name);
        return userService.register(userVO, password);
    }

//    @RequestMapping(value = "/getAllStudents", method = RequestMethod.GET)
//    public String getAllStudents(HttpSession session) {
//        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("teacher")) {
//            return "无权限";
//        }
//        return userService.getAllStudents();
//    }

    @RequestMapping(value = "/getStudentsByGroup", method = RequestMethod.GET)
    public String getAllStudents(HttpSession session, int groupId) {
        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("teacher")) {
            return "无权限";
        }
        return userService.getStudentsByGroup(groupId);
    }

    @RequestMapping(value = "/deleteStudentFromGroup", method = RequestMethod.POST)
    public String deleteStudentFromGroup(HttpSession session, int groupId, String username) {
        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("teacher")) {
            return "无权限";
        }
        return userService.deleteStudentFromGroup(groupId, username);
    }
}
