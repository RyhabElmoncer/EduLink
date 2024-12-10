package com.EduLink.repository;


import com.EduLink.Models.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    // Recherche de groupe par nom (ajoutez cette méthode si nécessaire pour une fonctionnalité de recherche)
    List<Group> findByNameContainingIgnoreCase(String name);
}
