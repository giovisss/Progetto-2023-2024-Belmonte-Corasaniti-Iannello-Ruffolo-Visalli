package unical.enterprise.jokibackend.Data.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GameDto {
    private UUID id;
    private String title;
    private String description;
    private Double price;
    private String imagePath;
    private String genre;
    private String developer;
    private String publisher;
    private Date releaseDate;
    private Integer stock;
    private AdminDto admin;
}
