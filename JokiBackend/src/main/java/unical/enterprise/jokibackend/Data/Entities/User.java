package unical.enterprise.jokibackend.Data.Entities;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity(name = "users")
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "user_id")
    private UUID user_id;

    @Column(name = "user_password")
    private String user_password;

    @Column(name = "user_username", unique = true)
    private String user_username;
    
    @Column(name = "user_email", unique = true)
    private String user_email;

    @Column(name = "user_name")
    private String user_name;

    @Column(name = "user_surname")
    private String user_surname;

    @Column(name = "user_birthdate")
    private Date user_birthdate;

    @Column(name = "user_address")
    private String user_address;
}