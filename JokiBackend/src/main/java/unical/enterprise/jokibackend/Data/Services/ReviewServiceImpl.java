package unical.enterprise.jokibackend.Data.Services;

import java.util.Collection;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import unical.enterprise.jokibackend.Data.Dao.ReviewDao;
import unical.enterprise.jokibackend.Data.Dto.ReviewDto;
import unical.enterprise.jokibackend.Data.Entities.Review;
import unical.enterprise.jokibackend.Data.Services.Interfaces.ReviewService;

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
    public Collection <ReviewDto> getReviewsByGameId(UUID gameId) {
        Collection <Review> reviews = reviewDao.getReviewsByGameId(gameId);
        return modelMapper.map(reviews, Collection.class);
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
}