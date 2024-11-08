package com.EduLink.service;
import com.EduLink.Models.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User findUserByEmail(String email);
    List<User> getAllUsers();
}
