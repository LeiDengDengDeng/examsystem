package com.exam.controller;

import com.exam.bean.Group;
import com.exam.bean.ReturnBean;
import com.exam.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by disinuo on 17/11/16.
 */
@Api(value = "班级功能接口")
@RestController
@RequestMapping(value = "/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @ApiOperation(value = "获得所有group", notes = "返回所有group信息")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ReturnBean<List<Group>> getAllGroups() {
        ReturnBean<List<Group>> result = new ReturnBean<>();
        List<Group> groups = groupService.getAllGroups();

        result.setData(groups);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation(value = "新建一个组", notes = "组名不允许重复")
    @RequestMapping(value = "/addGroup", method = RequestMethod.POST)
    public ReturnBean addGroup(@RequestBody String name) {
        ReturnBean returnBean = new ReturnBean();
        try {
            groupService.insertGroup(name);
            returnBean.setSuccess(true);
        } catch (Exception e) {
            returnBean.setSuccess(false);
            returnBean.setMsg(e.getMessage());
        }
        return returnBean;

    }

    @ApiOperation(value = "删除一个组")
    @RequestMapping(value = "/{groupId}/deleteGroup", method = RequestMethod.POST)
    public ReturnBean deleteGroup(@PathVariable("groupId") int groupId) {
        ReturnBean returnBean = new ReturnBean();
        groupService.deleteGroup(groupId);
        returnBean.setSuccess(true);
        return returnBean;
    }

}
