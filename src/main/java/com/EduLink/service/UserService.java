package com.EduLink.service;
import com.EduLink.Models.User;

import java.util.List;

public interface UserService {
    void deleteUser(String userId);
    List<User> getAllUsers();
}
