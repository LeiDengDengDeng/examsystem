package com.exam.service;

import com.exam.bean.Group;

import java.util.List;

/**
 * Created by disinuo on 17/11/16.
 */
public interface GroupService {
    List<Group> getAllGroups();
    Group getGroupById(int groupId) throws Exception;
    void insertGroup(String name)throws Exception;
    void deleteGroup(int id);
}
