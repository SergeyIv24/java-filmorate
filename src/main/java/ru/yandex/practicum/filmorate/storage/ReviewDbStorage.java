package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

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
                review.getFilmId(),
                review.getUseful(),
                review.getReviewId());
        return review;
    }

    public void deleteReviewById(Long reviewId) {
        deleteItem(SQLqueries.DELETE_REVIEW, reviewId);
    }

    public Review getReviewById(Long reviewId) {
        return getOnePosition(SQLqueries.REVIEW_BY_ID, reviewId).get();
    }

    public Collection<Review> getAllReviews(Integer count) {
        return getAllItems(SQLqueries.GET_ALL_REVIEWS, count);
    }

    public Collection<Review> getReviewByFilmId(Long filmId, Integer count) {
        return getAllItems(SQLqueries.GET_ALL_FILMS_REVIEWS, filmId, count);
    }

}
