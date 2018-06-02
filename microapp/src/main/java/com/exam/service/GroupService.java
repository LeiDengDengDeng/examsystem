package com.exam.service;

/**
 * Created by deng on 2017/11/30.
 */
public interface GroupService {
    String addGroup(String name);

    String getAllGroups();

    String deleteGroup(int groupId);
}
