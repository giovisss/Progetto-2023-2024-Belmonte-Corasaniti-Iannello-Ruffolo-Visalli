// package unical.enterprise.jokibackend.Data.Entities;

// import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import unical.enterprise.jokibackend.Data.Entities.Embeddable.CartId;

// @Data
// @Entity(name = "carts")
// @AllArgsConstructor
// @NoArgsConstructor
// @Table(
//         name = "carts",
//         uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "game_id"})
// )
// public class Cart {
//     @EmbeddedId
//     private CartId id;

//     @OneToOne
//     @MapsId("userId")
//     @JoinColumn(name = "user_id")
//     private User user;

//     @ManyToOne
//     @MapsId("gameId")
//     @JoinColumn(name = "game_id")
//     private Game game;
// }