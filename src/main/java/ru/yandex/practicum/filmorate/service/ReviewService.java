package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewDbStorage;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewDbStorage reviewDbStorage;


    public Review addReview(Review review) {
        return reviewDbStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        return reviewDbStorage.updateReview(review);
    }

    public void deleteReview(Long reviewId) {
        reviewDbStorage.deleteReviewById(reviewId);
    }

    public Review getReviewById(Long reviewId) {
        return reviewDbStorage.getReviewById(reviewId);
    }





}
