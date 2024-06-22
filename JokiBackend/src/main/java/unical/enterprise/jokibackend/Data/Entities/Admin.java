package unical.enterprise.jokibackend.Data.Entities;

import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity(name = "admins")
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @Column
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Game> games;
}