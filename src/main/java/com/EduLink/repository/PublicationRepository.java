package com.EduLink.repository;
import com.EduLink.Models.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends MongoRepository<Publication, String> {
    Page<Publication> findByTagsContaining(String tag, Pageable pageable);}
