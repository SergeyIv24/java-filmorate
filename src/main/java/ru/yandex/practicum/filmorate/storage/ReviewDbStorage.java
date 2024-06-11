package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

@Repository
public class ReviewDbStorage extends BaseStorage<Review> {

    public ReviewDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Review> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Review addReview(Review review) {
        Long id = insert(SQLqueries.ADD_REVIEW,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId());
        review.setReviewId(id);
        return review;
    }

    public Review updateReview(Review review) {
        update(SQLqueries.UPDATE_REVIEW,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful(),
                review.getReviewId());
        return review;
    }

    public void deleteReviewById(Long reviewId) {
        deleteItem(SQLqueries.DELETE_REVIEW, reviewId);
    }

    public Optional<Review> getReviewById(Long reviewId) {
        return getOnePosition(SQLqueries.REVIEW_BY_ID, reviewId);
    }

    public Collection<Review> getAllReviews(Integer count) {
        if (count == null) {
            return getAllItems(SQLqueries.GET_ALL_REVIEW_NO_LIMIT);
        }
        return getAllItems(SQLqueries.GET_ALL_REVIEWS, count);
    }

    public Collection<Review> getReviewByFilmId(Long filmId, Integer count) {
        return getAllItems(SQLqueries.GET_ALL_FILMS_REVIEWS, filmId, count);
    }

    public void addLikeToReview(Long reviewId) {
        insertMany(SQLqueries.ADD_LIKE_TO_REVIEW, reviewId, reviewId);
    }

    public void addDislikeToReview(Long reviewId) {
        insertMany(SQLqueries.ADD_DISLIKE_TO_REVIEW, reviewId, reviewId);
    }
}
