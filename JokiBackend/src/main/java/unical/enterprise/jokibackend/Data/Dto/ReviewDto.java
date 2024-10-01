package unical.enterprise.jokibackend.Data.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private UUID id;
    @JsonIgnore
    private UserDto user;
    @JsonIgnore
    private UUID gameId;
    private String review;
    private Boolean suggested;
}
