package com.exam.controller;

import com.exam.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by deng on 2017/11/30.
 */
@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addGroup(String name) {
        return groupService.addGroup(name);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public String getAll() {
        return groupService.getAllGroups();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(HttpSession session, int groupId) {
        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("teacher")) {
            return "无权限";
        }
        return groupService.deleteGroup(groupId);
    }
}
