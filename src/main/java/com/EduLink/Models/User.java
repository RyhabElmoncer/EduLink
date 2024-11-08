package com.EduLink.Models;

import com.EduLink.Enum.Role;
import com.EduLink.token.Token;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class User implements UserDetails {


    @Id
    private String id;
    private String FirstName;
    private String LastName;
    private String email;
    private String password;
    private Role role;
    private String bio;  // Biographie de l'utilisateur
    private File profileImageUrl;

    /**
     * Annotation Spring Data MongoDB qui spécifie une référence à une autre collection. Ici, la liste des tokens associés à l'utilisateur.
     */
    @DBRef
    private List<Token> tokens;


    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retourne le nom d'utilisateur, ici l'adresse email de l'utilisateur.
     * @return Adresse email de l'utilisateur.
     */
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        return Objects.equals(id, other.id);
    }

    /**
     * Indique si le compte de l'utilisateur a expiré.
     * Ici, toujours vrai (non expiré).
     * @return Toujours true.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indique si le compte de l'utilisateur est verrouillé.
     * Ici, toujours vrai (non verrouillé).
     * @return Toujours true.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indique si l'utilisateur est activé.
     * Ici, toujours vrai (activé).
     * @return Toujours true.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getId() {
        return id;
    }
}
