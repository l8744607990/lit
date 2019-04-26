package com.github.lit.security.dao;

import com.github.lit.plugin.core.dao.BaseDao;
import com.github.lit.security.model.UserRole;

import java.util.List;

/**
 * User : liulu
 * Date : 2018/4/16 16:54
 * version $Id: UserRoleDao.java, v 0.1 Exp $
 */
public interface UserRoleDao extends BaseDao<UserRole> {

    List<UserRole> findByUserId(Long userId);
}