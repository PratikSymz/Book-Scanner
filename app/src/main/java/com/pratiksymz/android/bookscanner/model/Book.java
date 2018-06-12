package com.pratiksymz.android.bookscanner.model;

import java.util.List;

public class Book {
    private String mTitle;
    private String mAuthor;
    private int mReviewCount;
    private int mRating;
    private String mDetailedLink;
    private String mGenre;
    private int mPages;
    private String mReleaseDate;
    private List<BookReview> mReviewsList;

    // Empty Constructor
    public Book() {}

    /* SETTERS */
    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public void setReviewCount(int reviewCount) {
        this.mReviewCount = reviewCount;
    }

    public void setRating(int rating) {
        this.mRating = rating;
    }

    public void setDetailedLink(String detailedLink) {
        this.mDetailedLink = detailedLink;
    }

    public void setGenre(String genre) {
        this.mGenre = genre;
    }

    public void setPages(int pages) {
        this.mPages = pages;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public void setReviewsList(List<BookReview> reviewsList) {
        this.mReviewsList = reviewsList;
    }

    /* GETTERS */
    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public int getReviewCount() {
        return mReviewCount;
    }

    public int getRating() {
        return mRating;
    }

    public String getDetailedLink() {
        return mDetailedLink;
    }

    public String getGenre() {
        return mGenre;
    }

    public int getPages() {
        return mPages;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public List<BookReview> getReviewsList() {
        return mReviewsList;
    }
}
