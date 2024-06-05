package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

@Repository
public class ReviewDbStorage extends BaseStorage<Review> {

    public ReviewDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Review> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Review addReview(Review review) {
        Long id = insert(SQLqueries.PUT_REVIEW,
                review.getContent(),
                review.isPositive(),
                review.getUserId(),
                review.getFilmsId(),
                review.getUseful());
        review.setReviewId(id);
        return review;
    }

    public Review updateReview(Review review) {
        update(SQLqueries.UPDATE_REVIEW,
                review.getContent(),
                review.isPositive(),
                review.getFilmsId(),
                review.getReviewId());
        return review;
    }

    public void deleteReviewById(Long reviewId) {
        deleteItem(SQLqueries.DELETE_REVIEW, reviewId);
    }

    public Review getReviewById(Long reviewId) {
        return getOnePosition(SQLqueries.REVIEW_BY_ID, reviewId).get();
    }

}
