package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.ReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewDbStorage reviewDbStorage;
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    public Review addReview(Review review) {
        isUserExist(review.getUserId());
        isFilmExist(review.getFilmId());
        return reviewDbStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        isUserExist(review.getUserId());
        isFilmExist(review.getFilmId());
        Optional<Review> existedReview = reviewDbStorage.getReviewById(review.getReviewId());
        if (existedReview.isPresent()) {
            review.setUserId(existedReview.get().getUserId());
            review.setFilmId(existedReview.get().getFilmId());
            review.setUseful(existedReview.get().getUseful());
        }
        if (review.getUseful() == null) {
            review.setUseful(0L);
        }
        return reviewDbStorage.updateReview(review);
    }

    public void deleteReview(Long reviewId) {
        isReviewExist(reviewId);
        reviewDbStorage.deleteReviewById(reviewId);
    }

    public Review getReviewById(Long reviewId) {
        isReviewExist(reviewId);
        return reviewDbStorage.getReviewById(reviewId).get();
    }

    public Collection<Review> getSomeReviews(Long filmId, Integer count) {
        if (filmId == null) {
            return reviewDbStorage.getAllReviews(count);
        }
        return reviewDbStorage.getReviewByFilmId(filmId, count);
    }

    public void addLikeToReview(Long reviewId) {
        isReviewExist(reviewId);
        reviewDbStorage.addLikeToReview(reviewId);
    }

    public void addDislikeToReview(Long reviewId) {
        isReviewExist(reviewId);
        reviewDbStorage.addDislikeToReview(reviewId);
        Optional<Review> updatedReview = reviewDbStorage.getReviewById(reviewId);
        if (updatedReview.isPresent() && updatedReview.get().getUseful() == 0) {
            reviewDbStorage.addDislikeToReview(reviewId);
        }
    }

    public void deleteLike(Long reviewId) {
        isReviewExist(reviewId);
        reviewDbStorage.addDislikeToReview(reviewId);
    }

    private void isFilmExist(Long filmId) {
        if (filmStorage.getFilm(filmId).isEmpty()) {
            log.warn("Фильма не существует");
            throw new NotFoundException("Фильма с таким id не существует");
        }
    }

    private void isUserExist(Long userId) {
        if (userStorage.getUser(userId).isEmpty()) {
            log.warn("Пользователь не существует");
            throw new NotFoundException("Пользователь с таким id не существует");
        }
    }

    private void isReviewExist(Long reviewId) {
        if (reviewDbStorage.getReviewById(reviewId).isEmpty()) {
            log.warn("Отзыва не существует");
            throw new NotFoundException("Отзыв с таким id не существует");
        }
    }
}
