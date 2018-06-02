package com.exam.service.impl;

import com.exam.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by deng on 2017/11/30.
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String addGroup(String name) {
        HttpEntity<String> params = new HttpEntity<>(name, new HttpHeaders());
        return restTemplate.postForObject("http://MICROUSER/groups/addGroup", params, String.class);
    }

    @Override
    public String getAllGroups() {
        return restTemplate.getForObject("http://MICROUSER/groups/getAll", String.class);
    }

    @Override
    public String deleteGroup(int groupId) {
        HttpEntity<String> params = new HttpEntity<>(new HttpHeaders());
        return restTemplate.postForObject("http://MICROUSER/groups/" + groupId + "/deleteGroup", params, String.class);
    }
}
