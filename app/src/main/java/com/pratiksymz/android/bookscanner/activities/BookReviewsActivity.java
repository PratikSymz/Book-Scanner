package com.pratiksymz.android.bookscanner.activities;

import android.Manifest;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.pratiksymz.android.bookscanner.R;
import com.pratiksymz.android.bookscanner.adapters.BookReviewsAdapter;
import com.pratiksymz.android.bookscanner.loader.BookLoader;
import com.pratiksymz.android.bookscanner.model.Book;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

public class BookReviewsActivity extends AppCompatActivity implements LoaderCallbacks<Book> {
    private static final String LOG_TAG = BookReviewsActivity.class.getSimpleName();

    private static final String BOOK_API_QUERY = "https://idreambooks.com/api/books/reviews.json?";
    private BookReviewsAdapter mBookReviewsAdapter;
    private SlidingUpPanelLayout mSlidingPanel;

    private TextView mMessageTextView;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;
    private ImageView mNoInternetIcon;
    private static final int REQUEST_ACCESS_NETWORK_STATE = 1;
    private ProgressBar mProgressBar;

    private TextView mBookTitle, mBookAuthor, mBookReviewCount, mBookGenre,
            mBookNumPages, mBookReleaseDate, mBookRatingText;
    private FloatingActionButton mBookReviewLink;
    private RatingBar mBookRating;

    private static final int BOOK_LOADER_ID = 100;

    // Get the user mSearchButton query
    String mSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reviews);

        mBookTitle = findViewById(R.id.book_info_title);
        mBookAuthor = findViewById(R.id.book_info_author);
        mBookReviewCount = findViewById(R.id.book_info_review_count);
        mBookGenre = findViewById(R.id.book_info_genre);
        mBookNumPages = findViewById(R.id.book_info_num_pages);
        mBookReleaseDate = findViewById(R.id.book_info_release_date);
        mBookReviewLink = findViewById(R.id.book_info_reviews_link);
        mBookRating = findViewById(R.id.book_info_rating);
        mBookRatingText = findViewById(R.id.book_info_rating_text);

        // Hide the panel while the data is loading
        mSlidingPanel = findViewById(R.id.sliding_layout);
        mSlidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        // TextView to show the user information about that data retrieval status
        mMessageTextView = findViewById(R.id.message_text_view);

        mProgressBar = findViewById(R.id.progress_bar);

        mConnectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        checkAccessNetworkPermission();
        mNoInternetIcon = findViewById(R.id.icon_no_internet);

        // If there is a network connection, fetch data
        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
            // Show the message for Fetching Data
            mMessageTextView.setText(getString(R.string.message_fetching));

            // TODO: Get Search query
            mSearchQuery = getIntent().getStringExtra("query");

            // Initialize Loader and Book Reviews Adapter
            initializeLoaderAndAdapter();

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mProgressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            // mMessageTextView.setText(getString(R.string.message_no_internet));
            mNoInternetIcon.setVisibility(View.VISIBLE);
        }
    }

    private void initializeLoaderAndAdapter() {
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        getLoaderManager().initLoader(BOOK_LOADER_ID, null, BookReviewsActivity.this);

        // Lookup the recyclerView in activity layout
        RecyclerView recyclerView = findViewById(R.id.book_reviews_recycler_view);
        // Create adapter passing the book review review data
        mBookReviewsAdapter = new BookReviewsAdapter(this, new ArrayList<>());
        // Attach the adapter to the recyclerView to populate items
        recyclerView.setAdapter(mBookReviewsAdapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a List Divider
        DividerItemDecoration dividerDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerDecoration);
    }

    @Override
    public Loader<Book> onCreateLoader(int i, Bundle bundle) {
        // Build the Uri based on the preferences
        Uri baseUri = Uri.parse(BOOK_API_QUERY);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // TODO: Change Query
        uriBuilder.appendQueryParameter("q", mSearchQuery);
        uriBuilder.appendQueryParameter("key", getString(R.string.API_KEY));
        Log.v(LOG_TAG, "Uri: " + uriBuilder);

        // Create a new loader with the supplied Url
        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<Book> loader, Book book) {
        // If there is a valid object of {@link Book}.
        if (book != null && book.getReviewsList() != null && book.getReviewsList().size() > 0) {
            mBookReviewsAdapter.addAll(book.getReviewsList());
            // Hide loading indicator because the data has been loaded
            mProgressBar.setVisibility(View.GONE);

            // Show the panel when the data is loaded
            mSlidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            // Check if the values received are valid or not and set the values of the items in the panel
            mBookTitle.setText(book.getTitle() != null ? book.getTitle() : getString(R.string.no_data_available));
            mBookAuthor.setText(book.getAuthor() != null ? book.getAuthor() : getString(R.string.no_data_available));
            mBookReviewCount.setText(book.getReviewCount() != -1 ?
                    String.valueOf(book.getReviewCount()) + " Reviews" : getString(R.string.no_data_available));

            // TODO: Set a textview if no rating is given:
            //
            mBookRating.setRating(book.getRating() != -1 ? getFormattedRating(book.getRating()) : 0f);
            mBookRatingText.setText(String.valueOf(book.getRating() != -1 ? getFormattedRating(book.getRating()) : 0f));

            mBookReviewLink.setOnClickListener(view -> {
                String url = book.getDetailedLink();
                boolean isLinkNull = false;
                if (url == null) isLinkNull = true;

                if (!isLinkNull) {
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(webIntent);
                } else {
                    DynamicToast.make(BookReviewsActivity.this,
                            "No Review Link for this Book",
                            Toast.LENGTH_SHORT).show();
                }
            });

            mBookGenre.setText(book.getGenre() != null ? book.getGenre() : getString(R.string.API_KEY));
            mBookNumPages.setText(book.getPages() != -1 ? String.valueOf(book.getPages()) : "No Pages");
            mBookReleaseDate.setText(book.getReleaseDate() != null ? book.getReleaseDate() : getString(R.string.API_KEY));

            // Hide message text
            mMessageTextView.setText("");
        } else {
            // Hide loading indicator because the data has been loaded
            mProgressBar.setVisibility(View.GONE);
            mBookReviewsAdapter.clearAll();
            // Set message text to display "No reviews found!"
            mMessageTextView.setText(getString(R.string.message_no_data));
        }
        Log.v(LOG_TAG, "Loader completed operation!");
    }

    private float getFormattedRating(float rating) {
        return ((rating * 5) / 100);
    }

    @Override
    public void onLoaderReset(Loader<Book> loader) {

    }

    private void checkAccessNetworkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    REQUEST_ACCESS_NETWORK_STATE
            );
        } else {    // Already have permission
            if (mConnectivityManager.getActiveNetworkInfo() == null ||
                    !mConnectivityManager.getActiveNetworkInfo().isAvailable() ||
                    !mConnectivityManager.getActiveNetworkInfo().isConnected()) {
                DynamicToast.makeError(this, "Internet Connection Turned Off").show();
                // finish();
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ACCESS_NETWORK_STATE:
                // Permission Granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mConnectivityManager.getActiveNetworkInfo() == null ||
                            !mConnectivityManager.getActiveNetworkInfo().isAvailable() ||
                            !mConnectivityManager.getActiveNetworkInfo().isConnected()) {
                        // DynamicToast.makeError(this, "Internet Connection Turned Off").show();
                    }
                } else {
                    finish();
                }
        }

    }
}
