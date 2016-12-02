package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.User;

public interface UserDAO {
    User query(String login, String password) throws Exception;
}
