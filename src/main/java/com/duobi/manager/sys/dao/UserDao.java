package com.duobi.manager.sys.dao;

import com.duobi.manager.sys.entity.User;

import java.util.List;

public interface UserDao {

    List<User> getAll();

    User getOne(Long id);

    void insert(User user);

    void update(User user);

    void delete(Long id);

    User findUserByUserName(String userName);
}
