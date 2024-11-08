package com.EduLink.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class File {
    @Id
    private String id;
    private String name;
    private String type;
    private byte[] bytes;

    public File(String s) {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        File other = (File) obj;
        return Objects.equals(id, other.id);
    }
}
