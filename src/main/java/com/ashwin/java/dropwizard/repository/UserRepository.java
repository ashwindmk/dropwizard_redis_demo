package com.ashwin.java.dropwizard.repository;

import com.ashwin.java.dropwizard.model.User;

public interface UserRepository {
    User getUser(int id);
    void storeUser(User user);
    boolean deleteUser(int id);
}
