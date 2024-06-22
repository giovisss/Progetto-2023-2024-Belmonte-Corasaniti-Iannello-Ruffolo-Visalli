//package unical.enterprise.jokibackend.Data.Entities;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Collection;
//import java.util.UUID;
//
//@Data
//@Entity(name = "libraries")
//@AllArgsConstructor
//@NoArgsConstructor
//public class Library {
//    @Id
//    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
//    @Column
//    private UUID id;
//
//    @Id
//    @OneToOne
//    private User user;
//
//    @Id
//    @OneToOne
//    private Game game;
//}
