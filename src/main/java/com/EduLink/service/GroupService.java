package com.EduLink.service;
import com.EduLink.Models.Group;

import java.util.List;

public interface GroupService {
    Group createGroup(Group group);
    List<Group> getAllGroups();
    Group joinGroup(String groupId, String userId);
}
