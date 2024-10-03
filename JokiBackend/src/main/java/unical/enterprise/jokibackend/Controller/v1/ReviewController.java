package unical.enterprise.jokibackend.Controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.ReviewDto;
import unical.enterprise.jokibackend.Data.Services.Interfaces.ReviewService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class   ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity <Collection <ReviewDto>> getReviewsList() {
        Collection <ReviewDto> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping(value = "", consumes = "application/json")
    public ResponseEntity <ReviewDto> insertReview(@RequestBody ReviewDto reviewDto) {
        reviewDto.setUser(userService.getUserByUsername(UserContextHolder.getContext().getPreferredUsername()));
        reviewService.insertReview(reviewDto);
        return ResponseEntity.ok(reviewDto);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity <ReviewDto> updateReview(@PathVariable("id") String id, @RequestBody ReviewDto reviewDto) {
        reviewDto.setUser(userService.getUserByUsername(UserContextHolder.getContext().getPreferredUsername()));
        System.out.println("Stampo il reviewDto");
        System.out.println(reviewDto.toString());
        reviewService.updateReview(UUID.fromString(id), reviewDto);
        return ResponseEntity.ok(reviewDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity <String> deleteReview(@PathVariable("id") String id) {
        reviewService.deleteReviewById(UUID.fromString(id));
        return ResponseEntity.ok("Review deleted");
    }

    @GetMapping(value = "/average/{gameId}", produces = "application/json")
    public ResponseEntity <Double> getAverageRating(@PathVariable("gameId") String gameId) {
        return ResponseEntity.ok(reviewService.getAverageRating(UUID.fromString(gameId)));
    }

    @GetMapping(value = "/game/{gameId}", produces = "application/json")
    public ResponseEntity <Collection <ReviewDto>> getReviewsByGameId(@PathVariable("gameId") String gameId) {
        return ResponseEntity.ok(reviewService.getReviewsByGameId(UUID.fromString(gameId)));
    }

    @GetMapping(value = "/user/{userId}", produces = "application/json")
    public ResponseEntity <Collection <ReviewDto>> getReviewsByUserId(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUserId(UUID.fromString(userId)));
    }
}