package com.exam.dao;

import com.exam.bean.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by disinuo on 17/11/16.
 */
@Repository
public interface GroupDao {
    List<Group> getAllGroups();
    Group getGroupById(int groupId);
    void insertGroup(Group group);
    Group getGroupByName(String name);
    void deleteGroup(int groupId);

}
