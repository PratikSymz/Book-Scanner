package com.pratiksymz.android.bookscanner.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.pratiksymz.android.bookscanner.model.Book;
import com.pratiksymz.android.bookscanner.model.BookReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // Keys for BOOK INFO.
    private static final String KEY_BOOK_RESPONSE = "book";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_REVIEW_COUNT = "review_count";
    private static final String KEY_RATING = "rating";
    private static final String KEY_DETAILED_REVIEW_LINK = "detail_link";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_PAGES = "pages";
    private static final String KEY_RELEASE_DATE = "release_date";

    // Keys for BOOK REVIEWS
    private static final String KEY_BOOK_REVIEWS = "critic_reviews";
    private static final String KEY_SNIPPET = "snippet";
    private static final String KEY_SNIPPET_SOURCE = "source";
    private static final String KEY_SNIPPET_REVIEW_LINK = "review_link";
    private static final String KEY_SNIPPET_STAR_RATING = "star_rating";
    private static final String KEY_SNIPPET_REVIEW_DATE = "review_date";
    private static final String KEY_SNIPPET_SOURCE_LOGO = "source_logo";

    /* Empty Constructor */
    private QueryUtils() {
    }

    /**
     * Returns {@link Book} from the given string URL.
     */
    public static Book fetchBookData(String requestUrl, Context context) {
        // Create a Url Object
        URL url = createUrl(requestUrl, context);

        /// Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url, context);
        } catch (IOException error) {
            Log.e(LOG_TAG, "Error closing the Input Stream!", error);
        }

        // Parse JSON string and create an {@ArrayList<Book>} object
        Book bookDetails = extractBookData(jsonResponse, context);
        return bookDetails;
    }

    /**
     * Returns new URL object from the given string requestUrl.
     */
    private static URL createUrl(String requestUrl, final Context context) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException error) {
            // Use a Handler to create a toast on the UI thread
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Error creating URL!", Toast.LENGTH_SHORT).show();
                }
            });
            Log.e(LOG_TAG, "Error creating URL!", error);
        }

        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url, final Context context) throws IOException {
        // String field to store the API Response
        String jsonResponse = null;

        // If the url is empty, return
        if (url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            // If the request was successful (response code 200), then read the input stream and
            // parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException error) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Problem retrieving the book results", Toast
                            .LENGTH_SHORT)
                            .show();
                }
            });
            Log.e(LOG_TAG, "Problem retrieving the API JSON results", error);
        } finally {
            // Close the Url connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            // Close Stream
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Read input stream as received from API and convert to String.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder streamOutput = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                streamOutput.append(line);
                line = bufferedReader.readLine();
            }
        }

        return streamOutput.toString();
    }

    private static Book extractBookData(String jsonResponse, final Context context) {
        // If JSONResponse is empty, return
        if (TextUtils.isEmpty(jsonResponse)) return new Book();

        Book mBookObject = null;
        List<BookReview> bookReviewsList = new ArrayList<>();

        // If there's a problem with the way the JSON is formatted,
        // a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Build a list of Book objects
            JSONObject baseJSONObject = new JSONObject(jsonResponse);
            JSONObject bookResponseJSONObject = baseJSONObject.getJSONObject(KEY_BOOK_RESPONSE);
            JSONArray bookReviewsJSONArray = bookResponseJSONObject.getJSONArray(KEY_BOOK_REVIEWS);

            /* Parse the Book Info */
            // 1. Variables relating to Book info.
            String bookTitle = null;
            if (bookResponseJSONObject.has(KEY_TITLE)) {
                bookTitle = bookResponseJSONObject.getString(KEY_TITLE);
            }

            String bookAuthor = null;
            if (bookResponseJSONObject.has(KEY_AUTHOR)) {
                bookAuthor = bookResponseJSONObject.getString(KEY_AUTHOR);
            }

            int bookReviewCount = -1;
            if (bookResponseJSONObject.has(KEY_REVIEW_COUNT)) {
                bookReviewCount = bookResponseJSONObject.getInt(KEY_REVIEW_COUNT);
            }

            int bookRating = -1;
            if (bookResponseJSONObject.has(KEY_RATING)) {
                bookRating = bookResponseJSONObject.getInt(KEY_RATING);
            }

            String bookReviewDetailedLink = null;
            if (bookResponseJSONObject.has(KEY_DETAILED_REVIEW_LINK)) {
                bookReviewDetailedLink = bookResponseJSONObject.getString(KEY_DETAILED_REVIEW_LINK);
            }

            String bookGenre = null;
            if (bookResponseJSONObject.has(KEY_GENRE)) {
                bookGenre = bookResponseJSONObject.getString(KEY_GENRE);
            }

            int bookNumPages = -1;
            if (bookResponseJSONObject.has(KEY_PAGES)) {
                bookNumPages = bookResponseJSONObject.getInt(KEY_PAGES);
            }

            String bookReleaseDate = null;
            if (bookResponseJSONObject.has(KEY_RELEASE_DATE)) {
                bookReleaseDate = bookResponseJSONObject.getString(KEY_RELEASE_DATE);
            }

            /* Parse the Book Reviews */
            // 2. Variables relating to Book Reviews: -
            String reviewSnippet = null;
            String reviewSource = null;
            String reviewLink = null;
            float reviewStarRating = -1f;
            String reviewDate = null;
            String reviewSourceLogo = null;

            for (int i = 0; i < bookReviewsJSONArray.length(); i++) {
                JSONObject reviewArticle = bookReviewsJSONArray.getJSONObject(i);

                if (reviewArticle.has(KEY_SNIPPET)) {
                    reviewSnippet = reviewArticle.getString(KEY_SNIPPET);
                }

                if (reviewArticle.has(KEY_SNIPPET_SOURCE)) {
                    reviewSource = reviewArticle.getString(KEY_SNIPPET_SOURCE);
                }

                if (reviewArticle.has(KEY_SNIPPET_REVIEW_LINK)) {
                    reviewLink = reviewArticle.getString(KEY_SNIPPET_REVIEW_LINK);
                }

                if (reviewArticle.has(KEY_SNIPPET_STAR_RATING)) {
                    reviewStarRating = reviewArticle.getInt(KEY_SNIPPET_STAR_RATING);
                }

                if (reviewArticle.has(KEY_SNIPPET_REVIEW_DATE)) {
                    reviewDate = reviewArticle.getString(KEY_SNIPPET_REVIEW_DATE);
                }

                if (reviewArticle.has(KEY_SNIPPET_SOURCE_LOGO)) {
                    reviewSourceLogo = reviewArticle.getString(KEY_SNIPPET_SOURCE_LOGO);
                }

                // Create a new BookReview object
                BookReview mBookReviewObject = new BookReview();
                // Set the values:
                mBookReviewObject.setReviewSnippet(reviewSnippet);
                mBookReviewObject.setReviewSource(reviewSource);
                mBookReviewObject.setReviewLink(reviewLink);
                mBookReviewObject.setReviewStarRating(reviewStarRating);
                mBookReviewObject.setReviewDate(reviewDate);
                mBookReviewObject.setReviewSourceLogo(reviewSourceLogo);
                // Add to Review List:
                bookReviewsList.add(mBookReviewObject);
            }

            // Create the Book object.
            mBookObject = new Book();
            // Set the values:
            mBookObject.setTitle(bookTitle);
            mBookObject.setAuthor(bookAuthor);
            mBookObject.setReviewCount(bookReviewCount);
            mBookObject.setRating(bookRating);
            mBookObject.setDetailedLink(bookReviewDetailedLink);
            mBookObject.setGenre(bookGenre);
            mBookObject.setPages(bookNumPages);
            mBookObject.setReleaseDate(bookReleaseDate);
            mBookObject.setReviewsList(bookReviewsList);

        } catch (JSONException error) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> DynamicToast.make(context, "The Book doen't exist in the Database", Toast
                    .LENGTH_SHORT)
                    .show());
            Log.e(LOG_TAG, "Problem parsing the JSON results", error);
        }

        return mBookObject;
    }
}
