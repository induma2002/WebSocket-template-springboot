package edu.induma.service;

import edu.induma.model.User;

import java.util.List;

public interface UserService {
    void add(User user);

    List<User> get();
}
