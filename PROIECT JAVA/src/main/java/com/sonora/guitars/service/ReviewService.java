package com.sonora.guitars.service;

import com.sonora.guitars.entity.Review;
import com.sonora.guitars.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    public Review saveReview(Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Nota acordată trebuie să fie între 1 și 5 stele!");
        }
        if (review.getComment() == null || review.getComment().trim().length() < 5) {
            throw new IllegalArgumentException("Comentariul trebuie să aibă cel puțin 5 caractere!");
        }
        return reviewRepository.save(review);
    }

    public Double getAverageRatingForProduct(Long productId) {
        Double avg = reviewRepository.getAverageRatingForProduct(productId);
        return Math.round(avg * 10.0) / 10.0;
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Recenzia cu ID-ul " + id + " nu există!");
        }
        reviewRepository.deleteById(id);
    }
}
