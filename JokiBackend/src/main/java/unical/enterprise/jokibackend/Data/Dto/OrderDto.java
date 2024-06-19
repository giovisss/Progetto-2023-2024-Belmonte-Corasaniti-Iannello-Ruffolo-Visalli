package unical.enterprise.jokibackend.Data.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unical.enterprise.jokibackend.Data.Entities.Game;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String id;
    private String date;
    private Double price;
    private Boolean status;
    private Collection<Game> game;
    private String user;
}
