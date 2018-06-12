package com.pratiksymz.android.bookscanner.model;

public class BookReview {
    private String mReviewSnippet;
    private String mReviewSource;
    private String mReviewLink;
    private Float mReviewStarRating;
    private String mReviewDate;
    private String mReviewSourceLogo;

    /* Empty Constructor */
    public BookReview() {}

    /* SETTERS */
    public void setReviewSnippet(String reviewSnippet) {
        this.mReviewSnippet = reviewSnippet;
    }

    public void setReviewSource(String reviewSource) {
        this.mReviewSource = reviewSource;
    }

    public void setReviewLink(String reviewLink) {
        this.mReviewLink = reviewLink;
    }

    public void setReviewStarRating(Float reviewStarRating) {
        this.mReviewStarRating = reviewStarRating;
    }

    public void setReviewDate(String reviewDate) {
        this.mReviewDate = reviewDate;
    }

    public void setReviewSourceLogo(String reviewSourceLogo) {
        this.mReviewSourceLogo = reviewSourceLogo;
    }

    /* GETTERS */
    public String getReviewSnippet() {
        return mReviewSnippet;
    }

    public String getReviewSource() {
        return mReviewSource;
    }

    public String getReviewLink() {
        return mReviewLink;
    }

    public Float getReviewStarRating() {
        return mReviewStarRating;
    }

    public String getReviewDate() {
        return mReviewDate;
    }

    public String getReviewSourceLogo() {
        return mReviewSourceLogo;
    }
}
