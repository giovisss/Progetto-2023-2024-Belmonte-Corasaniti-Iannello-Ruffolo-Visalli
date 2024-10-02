package unical.enterprise.jokibackend.Data.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private UUID id;
    @JsonIgnore
    private UserDto user;
    private UUID gameId;
    private String review;
    private Boolean suggested;
}