package com.EduLink.token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TokenRepository extends MongoRepository<Token, String> {

  @Query("{ 'user.id' : ?0, $or: [ { 'expired' : false }, { 'revoked' : false } ] }")
  List<Token> findAllValidTokenByUser(String id);

  Optional<Token> findByToken(String token);
}
