package unical.enterprise.jokibackend.Data.Services.Interfaces;

import unical.enterprise.jokibackend.Data.Dto.ReviewDto;

import java.util.Collection;
import java.util.UUID;

public interface ReviewService {
    Collection<ReviewDto> findAll();

    void insertReview(ReviewDto reviewDto);
    public void updateReview(UUID id, ReviewDto reviewDto);
    public void deleteReviewById(UUID id);
    public Double getAverageRating(UUID gameId);
    public Collection <ReviewDto> getReviewsByGameId(UUID gameId);
    public Collection <ReviewDto> getReviewsByUserId(UUID userId);
    public ReviewDto getReviewByUserIdAndGameId(UUID userId, UUID gameId);

    void deleteByGameId(UUID id);
}