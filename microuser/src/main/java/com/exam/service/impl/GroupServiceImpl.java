package com.exam.service.impl;

import com.exam.bean.Group;
import com.exam.dao.GroupDao;
import com.exam.dao.UserDao;
import com.exam.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by disinuo on 17/11/16.
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<Group> getAllGroups() {
        return groupDao.getAllGroups();
    }

    @Override
    public Group getGroupById(int groupId) throws Exception {
        Group group=groupDao.getGroupById(groupId);
        if(group==null){
            throw new Exception("no such group");
        }
        return group;
    }

    @Override
    public void insertGroup(String name) throws Exception {
        Group g=groupDao.getGroupByName(name);
        if(g!=null){
            throw new Exception("duplicate name");
        }
        Group group=new Group(name);
        groupDao.insertGroup(group);
    }

    @Override
    public void deleteGroup(int id) {
        userDao.dropAllStudentsFromGroup(id);
        groupDao.deleteGroup(id);
    }


}
