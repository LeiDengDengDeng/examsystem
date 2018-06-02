package com.exam.dao;

import com.exam.bean.Role;
import org.springframework.stereotype.Repository;

/**
 * Created by liying on 2017/11/23.
 */
@Repository
public interface RoleDao {
    Role getRole(int id);
}
