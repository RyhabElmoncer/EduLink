package com.EduLink.token;


import com.EduLink.Models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tokens")
public class Token {

  @Id
  private String id;

  private String token;

  private TokenType tokenType = TokenType.BEARER;

  private boolean revoked;

  private boolean expired;

  @DBRef
  private User user;
}
