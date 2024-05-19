package unical.enterprise.jokibackend.Data.Entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity(name = "admins")
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "admin_id")
    private UUID admin_id;

    @Column(name = "admin_password")
    private String admin_password;

    @Column(name = "admin_username", unique = true)
    private String admin_username;

    @Column(name = "admin_email", unique = true)
    private String admin_email;
}
