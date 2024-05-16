package unical.enterprise.jokibackend.Data.Entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "user_id")
    private UUID user_id;

    @Column(name = "user_password")
    private String user_password;

    @Column(name = "user_role")
    private String user_role;

    @Column(name = "user_username", unique = true)
    private String user_username;
}