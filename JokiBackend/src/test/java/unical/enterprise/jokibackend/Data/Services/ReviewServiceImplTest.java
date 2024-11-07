package unical.enterprise.jokibackend.Data.Services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import unical.enterprise.jokibackend.Data.Dao.ReviewDao;
import unical.enterprise.jokibackend.Data.Dto.ReviewDto;
import unical.enterprise.jokibackend.Data.Entities.Review;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewDao reviewDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;
    private ReviewDto reviewDto;
    private UUID reviewId;
    private UUID gameId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        reviewId = UUID.randomUUID();
        gameId = UUID.randomUUID();
        userId = UUID.randomUUID();
        review = new Review();
        review.setId(reviewId);
        reviewDto = new ReviewDto();
        reviewDto.setId(reviewId);
    }

    @Test
    void testInsertReview() {
        when(modelMapper.map(reviewDto, Review.class)).thenReturn(review);
        reviewService.insertReview(reviewDto);
        verify(reviewDao, times(1)).save(review);
    }

    @Test
    void testUpdateReview() {
        when(reviewDao.findById(reviewId)).thenReturn(Optional.of(review));
        reviewDto.setReview("Updated review");
        reviewDto.setSuggested(true);
        reviewService.updateReview(reviewId, reviewDto);
        assertEquals("Updated review", review.getReview());
        assertTrue(review.getSuggested());
        verify(reviewDao, times(1)).save(review);
    }

    @Test
    void testUpdateReviewNotFound() {
        when(reviewDao.findById(reviewId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> reviewService.updateReview(reviewId, reviewDto));
    }

    @Test
    void testDeleteReviewById() {
        reviewService.deleteReviewById(reviewId);
        verify(reviewDao, times(1)).deleteById(reviewId);
    }

    @Test
    void testGetReviewById() {
        when(reviewDao.findById(reviewId)).thenReturn(Optional.of(review));
        when(modelMapper.map(review, ReviewDto.class)).thenReturn(reviewDto);
        ReviewDto result = reviewService.getReviewById(reviewId);
        assertNotNull(result);
        assertEquals(reviewId, result.getId());
    }

    @Test
    void testGetReviewByIdNotFound() {
        when(reviewDao.findById(reviewId)).thenReturn(Optional.empty());
        ReviewDto result = reviewService.getReviewById(reviewId);
        assertNull(result);
    }

    @Test
    void testGetAverageRating() {
        when(reviewDao.getAverageRating(gameId)).thenReturn(4.5);
        Double averageRating = reviewService.getAverageRating(gameId);
        assertEquals(4.5, averageRating);
    }

    @Test
    void testGetReviewsByGameId() {
        Object[] reviewData = {reviewId, "Review text", true, "username"};
        Collection<Object[]> reviewsData = Collections.singletonList(reviewData);
        when(reviewDao.getReviewsByGameId(gameId)).thenReturn(reviewsData);
        Collection<ReviewDto> result = reviewService.getReviewsByGameId(gameId);
        assertEquals(1, result.size());
        ReviewDto dto = result.iterator().next();
        assertEquals(reviewId, dto.getId());
        assertEquals("Review text", dto.getReview());
        assertTrue(dto.getSuggested());
        assertEquals("username", dto.getUsername());
    }

    @Test
    void testGetReviewsByUserId() {
        Collection<Review> reviews = Collections.singletonList(review);
        when(reviewDao.getReviewsByUserId(userId)).thenReturn(reviews);
        when(modelMapper.map(reviews, Collection.class)).thenReturn(Collections.singletonList(reviewDto));
        Collection<ReviewDto> result = reviewService.getReviewsByUserId(userId);
        assertEquals(1, result.size());
        assertEquals(reviewId, result.iterator().next().getId());
    }

    @Test
    void testFindAll() {
        List<Review> reviews = Collections.singletonList(review);
        when(reviewDao.findAll()).thenReturn(reviews);
        when(modelMapper.map(reviews, Collection.class)).thenReturn(Collections.singletonList(reviewDto));
        Collection<ReviewDto> result = reviewService.findAll();
        assertEquals(1, result.size());
        assertEquals(reviewId, result.iterator().next().getId());
    }

    @Test
    void testGetReviewByUserIdAndGameId() {
        when(reviewDao.getReviewByUserIdAndGameId(userId, gameId)).thenReturn(review);
        when(modelMapper.map(review, ReviewDto.class)).thenReturn(reviewDto);
        ReviewDto result = reviewService.getReviewByUserIdAndGameId(userId, gameId);
        assertNotNull(result);
        assertEquals(reviewId, result.getId());
    }

    @Test
    void testGetReviewByUserIdAndGameIdNotFound() {
        when(reviewDao.getReviewByUserIdAndGameId(userId, gameId)).thenReturn(null);
        ReviewDto result = reviewService.getReviewByUserIdAndGameId(userId, gameId);
        assertNull(result);
    }

    @Test
    void testDeleteByGameId() {
        reviewService.deleteByGameId(gameId);
        verify(reviewDao, times(1)).deleteReviewsByGameId(gameId);
    }

    @Test
    void testUpdateReviewWithNullDto() {
        when(reviewDao.findById(reviewId)).thenReturn(Optional.of(review));
        assertThrows(NullPointerException.class, () -> reviewService.updateReview(reviewId, null));
    }

    @Test
    void testDeleteReviewByIdWithInvalidId() {
        doThrow(new IllegalArgumentException("Invalid ID")).when(reviewDao).deleteById(any(UUID.class));
        assertThrows(IllegalArgumentException.class, () -> reviewService.deleteReviewById(UUID.randomUUID()));
    }

    @Test
    void testGetAverageRatingWithInvalidGameId() {
        when(reviewDao.getAverageRating(any(UUID.class))).thenReturn(null);
        Double averageRating = reviewService.getAverageRating(UUID.randomUUID());
        assertNull(averageRating);
    }

    @Test
    void testGetReviewsByGameIdWithInvalidGameId() {
        when(reviewDao.getReviewsByGameId(any(UUID.class))).thenReturn(Collections.emptyList());
        Collection<ReviewDto> result = reviewService.getReviewsByGameId(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetReviewsByUserIdWithInvalidUserId() {
        when(reviewDao.getReviewsByUserId(any(UUID.class))).thenReturn(Collections.emptyList());
        when(modelMapper.map(Collections.emptyList(), Collection.class)).thenReturn(Collections.emptyList());
        Collection<ReviewDto> result = reviewService.getReviewsByUserId(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllWithNoReviews() {
        when(reviewDao.findAll()).thenReturn(Collections.emptyList());
        when(modelMapper.map(Collections.emptyList(), Collection.class)).thenReturn(Collections.emptyList());
        Collection<ReviewDto> result = reviewService.findAll();
        assertTrue(result.isEmpty());
    }
    @Test
    void testGetReviewByUserIdAndGameIdWithInvalidIds() {
        when(reviewDao.getReviewByUserIdAndGameId(any(UUID.class), any(UUID.class))).thenReturn(null);
        ReviewDto result = reviewService.getReviewByUserIdAndGameId(UUID.randomUUID(), UUID.randomUUID());
        assertNull(result);
    }
}