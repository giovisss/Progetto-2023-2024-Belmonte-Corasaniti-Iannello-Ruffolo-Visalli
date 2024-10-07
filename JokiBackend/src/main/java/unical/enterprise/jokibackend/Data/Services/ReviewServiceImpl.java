package unical.enterprise.jokibackend.Data.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Dao.ReviewDao;
import unical.enterprise.jokibackend.Data.Dto.ReviewDto;
import unical.enterprise.jokibackend.Data.Entities.Review;
import unical.enterprise.jokibackend.Data.Services.Interfaces.ReviewService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void insertReview(ReviewDto reviewDto) {
        Review review = modelMapper.map(reviewDto, Review.class);
        reviewDao.save(review);
    }

    @Override
    @Transactional
    public void updateReview(UUID id, ReviewDto reviewDto) {
        Review review = reviewDao.findById(id).orElse(null);
        if (review == null) {
            throw new EntityNotFoundException("Review not found with id: " + id);
        }
        if (reviewDto.getReview() != null) {
            review.setReview(reviewDto.getReview());
        }
        if (reviewDto.getSuggested() != null) {
            review.setSuggested(reviewDto.getSuggested());
        }
        reviewDao.save(review);
    }

    @Override
    @Transactional
    public void deleteReviewById(UUID id) {
        reviewDao.deleteById(id);
    }

    public ReviewDto getReviewById(UUID id) {
        Review review = reviewDao.findById(id).orElse(null);
        if (review == null) {
            return null;
        }
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public Double getAverageRating(UUID gameId) {
        return reviewDao.getAverageRating(gameId);
    }

    @Override
    @Transactional
    public Collection<ReviewDto> getReviewsByGameId(UUID gameId) {
        Collection<Object[]> reviewsData = reviewDao.getReviewsByGameId(gameId);
        Collection<ReviewDto> reviewDtos = new ArrayList<>();
    
        for (Object[] reviewData : reviewsData) {
            UUID reviewId = (UUID) reviewData[0];
            String reviewText = (String) reviewData[1];
            Boolean suggested = (Boolean) reviewData[2];
            String username = (String) reviewData[3];
    
            // Mappa manualmente il DTO
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setId(reviewId);
            reviewDto.setReview(reviewText);
            reviewDto.setSuggested(suggested);
            reviewDto.setUsername(username);
            reviewDtos.add(reviewDto);
        }
    
        return reviewDtos;
    }

    @Override
    public Collection <ReviewDto> getReviewsByUserId(UUID userId) {
        Collection <Review> reviews = reviewDao.getReviewsByUserId(userId);
        return modelMapper.map(reviews, Collection.class);
    }

    @Override
    public Collection<ReviewDto> findAll() {
        Collection<Review> reviews = reviewDao.findAll();
        return modelMapper.map(reviews, Collection.class);
    }

    @Override
    public ReviewDto getReviewByUserIdAndGameId(UUID userId, UUID gameId) {
        Review review = reviewDao.getReviewByUserIdAndGameId(userId, gameId);
        if (review == null) {
            return null;
        }
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public void deleteByGameId(UUID id) {
        reviewDao.deleteReviewsByGameId(id);
    }
}