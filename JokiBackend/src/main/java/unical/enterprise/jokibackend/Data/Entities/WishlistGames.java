//package unical.enterprise.jokibackend.Data.Entities;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import unical.enterprise.jokibackend.Data.Entities.Embeddable.WishlistGamesId;
//
//@Data
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(
//        name = "wishlist_games",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"wishlist_id", "game_id"})
//)
//public class WishlistGames {
//    @EmbeddedId
//    private WishlistGamesId id;
//
//    @ManyToOne
//    @MapsId("wishlistId")
//    @JoinColumn(name = "wishlist_id")
//    private Wishlist wishlist;
//
//    @ManyToOne
//    @MapsId("gameId")
//    @JoinColumn(name = "game_id")
//    private Game game;
//}