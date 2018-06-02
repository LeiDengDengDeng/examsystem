package com.exam.controller;

import com.exam.bean.ReturnBean;
import com.exam.bean.User;
import com.exam.service.GroupService;
import com.exam.service.UserService;
import com.exam.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by deng on 2017/8/5.
 */
@Api(value = "用户功能接口")
@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ReturnBean<Boolean> auth(@RequestHeader(value = "username") String username, @RequestHeader(value = "password") String password) {
        ReturnBean<Boolean> result = new ReturnBean<>();
        result.setData(userService.auth(username, password));
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ReturnBean<UserVO> getUserInfo(@RequestParam(value = "username") String username) {
        ReturnBean<UserVO> result = new ReturnBean<>();
        try {
            result.setData(userService.getUserInfo(username));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "RequestBody", name = "vo", value = "vo", required = true, dataType = "UserVO")
    })
    public ReturnBean register(@RequestBody UserVO vo, @RequestHeader(value = "password") String password) {
        ReturnBean returnBean = new ReturnBean();
        try {
            userService.register(new User(vo.getUsername(), password, 1, vo.getName(), vo.getCode()));
            returnBean.setSuccess(true);
        } catch (Exception e) {
            returnBean.setSuccess(false);
            returnBean.setMsg(e.getMessage());
        }

        return returnBean;
    }

    @ApiOperation(value = "获得group里所有同学", notes = "根据groupId获得")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "groupId", value = "groupId", required = true, dataType = "int")
    })
    @RequestMapping(value = "/{groupId}/getStudents", method = RequestMethod.GET)
    public ReturnBean<List<UserVO>> getStudentsByGroup(@PathVariable("groupId") int groupId) {
        ReturnBean<List<UserVO>> returnBean = new ReturnBean<>();
        try {
            groupService.getGroupById(groupId);
            returnBean.setSuccess(true);
            returnBean.setData(userService.getUserByGroup(groupId));
        } catch (Exception e) {
            returnBean.setSuccess(false);
            returnBean.setMsg(e.getMessage());
        }
        return returnBean;
    }


    @ApiOperation(value = "获得所有同学")
    @RequestMapping(value = "getAllStudents", method = RequestMethod.GET)
    public ReturnBean<List<UserVO>> getAllStudents() {
        ReturnBean<List<UserVO>> returnBean = new ReturnBean<>();
        returnBean.setData(userService.getAllStudents());
        returnBean.setSuccess(true);
        return returnBean;
    }

    @ApiOperation(value = "从班级里删除学生")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "groupId", value = "groupId", required = true, dataType = "int")
    })
    @RequestMapping(value = "/{groupId}/dropStudent", method = RequestMethod.POST)
    public ReturnBean dropStudentFromGroup(@PathVariable("groupId") int groupId,@RequestParam(value = "email") String email) {
        userService.dropStudentFromGroup(groupId,email);
        ReturnBean returnBean = new ReturnBean<>();
        returnBean.setSuccess(true);
        return returnBean;
    }
}