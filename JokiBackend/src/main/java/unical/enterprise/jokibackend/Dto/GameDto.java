package unical.enterprise.jokibackend.Dto;

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
    private Double price;
    private String imagePath;
    private String category;
    private String author;
    private String publisher;
    private Date releaseDate;
    private Integer stock;
}
