package unical.enterprise.jokibackend.Data.Services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Dao.ReviewDao;
import unical.enterprise.jokibackend.Data.Dto.ReviewDto;
import unical.enterprise.jokibackend.Data.Entities.Review;
import unical.enterprise.jokibackend.Data.Services.Interfaces.ReviewService;

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
            return;
        }

        ModelMapper modelMapperUpdater = new ModelMapper();
        modelMapperUpdater.typeMap(ReviewDto.class, Review.class).addMappings(mapper -> {
            mapper.skip(Review::setId);
            mapper.skip(Review::setUser);
            mapper.skip(Review::setGame);
        });
        modelMapperUpdater.map(reviewDto, review);
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
}