package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReview(@Valid @RequestBody Review review) {
        Review reviewResponded = reviewService.addReview(review);
        userService.addUserActivity(reviewResponded.getUserId(), reviewResponded.getReviewId(),
                Events.REVIEW, Operations.OPERATION_ADD);
        return reviewResponded;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Review updateReview(@Valid @RequestBody Review review) {
        Review updatedReview = reviewService.updateReview(review);
        userService.addUserActivity(updatedReview.getUserId(), updatedReview.getReviewId(),
                Events.REVIEW, Operations.OPERATION_UPDATE);
        return reviewService.getReviewById(review.getReviewId());
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReview(@PathVariable(value = "reviewId") Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        userService.addUserActivity(review.getUserId(), review.getReviewId(),
                Events.REVIEW, Operations.OPERATION_REMOVE);
        reviewService.deleteReview(reviewId);
    }

    @GetMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public Review getReviewById(@PathVariable(value = "reviewId") Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Review> getSomeReviews(@RequestParam (required = false) Long filmId,
                                             @RequestParam(required = false, defaultValue = "10") Integer count) {
        return reviewService.getSomeReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeToReview(@PathVariable(value = "id") Long id,
                                @PathVariable(value = "userId") Long userId) {
        reviewService.addLikeToReview(id);
    }

    @PutMapping("/{id}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addDislikeToReview(@PathVariable(value = "id") Long id,
                                   @PathVariable(value = "userId") Long userId) {
        reviewService.addDislikeToReview(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable(value = "id") Long id,
                           @PathVariable(value = "userId") Long userId) {
        reviewService.deleteLike(id);
    }
}
