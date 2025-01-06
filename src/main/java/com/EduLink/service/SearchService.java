package com.EduLink.service;

import com.EduLink.DTO.GroupDTO;
import com.EduLink.DTO.PublicationDTO;
import com.EduLink.DTO.UserDTO;
import com.EduLink.repository.GroupRepository;
import com.EduLink.repository.PublicationRepository;
import com.EduLink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private PublicationRepository postRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;


}

