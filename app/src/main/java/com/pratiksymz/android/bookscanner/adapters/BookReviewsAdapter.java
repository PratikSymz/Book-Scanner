package com.pratiksymz.android.bookscanner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pratiksymz.android.bookscanner.R;
import com.pratiksymz.android.bookscanner.model.BookReview;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookReviewsAdapter extends RecyclerView.Adapter<BookReviewsAdapter.ViewHolder> {
    private List<BookReview> mBookReviews;
    private Context mContext;
    private String mURL;

    /*
     * Tag for the log messages
     */
    public static final String LOG_TAG = BookReviewsAdapter.class.getSimpleName();

    /**
     * INNER ViewHolder Class
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // The ViewHolder holds a variable for every View that will be used
        private Context context;
        private TextView reviewSource, reviewSnippet, reviewDate, reviewRatingText;
        private RatingBar reviewRating;
        private ImageView reviewSourceIcon;

        public ViewHolder(Context context, View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            this.context = context;
            itemView.setOnClickListener(this);

            reviewSource = itemView.findViewById(R.id.review_source);
            reviewDate = itemView.findViewById(R.id.review_date);
            reviewSnippet = itemView.findViewById(R.id.review_snippet);
            reviewRating = itemView.findViewById(R.id.review_rating);
            reviewSourceIcon = itemView.findViewById(R.id.review_source_icon);
            reviewRatingText = itemView.findViewById(R.id.review_rating_text);
        }

        @Override
        public void onClick(View view) {
            int viewPosition = getAdapterPosition();

            // BookReview bookReviewItem = mBookReviews.getReviewsList().get(viewPosition);

            // Get the Url from the current Book Item
            //  mURL = bookReviewItem.getReviewLink();

            // Get the News Title from the current NewsItem
            // String newsTitle = newsItem.getTitle();

//            // Create new intent to view the article's Title & URL
//            Intent websiteIntent = new Intent(context, NewsWebView.class);
//            websiteIntent.putExtra("url", mURL);
//            websiteIntent.putExtra("title", newsTitle);
//            // Start the intent
//            context.startActivity(websiteIntent);
        }
    }

    public BookReviewsAdapter(Context context, List<BookReview> bookReviews) {
        this.mContext = context;
        this.mBookReviews = bookReviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View reviewItemView = inflater.inflate(R.layout.layout_book_review_item, parent, false);

        // Return a ViewHolder instance
        ViewHolder viewHolder = new ViewHolder(context, reviewItemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        BookReview currBookReview = mBookReviews.get(position);

        // Set item views based on views and data model
        TextView reviewSource = holder.reviewSource;
        TextView reviewSnippet = holder.reviewSnippet;
        TextView reviewDate = holder.reviewDate;
        RatingBar reviewRating = holder.reviewRating;
        TextView reviewRatingText = holder.reviewRatingText;
        ImageView reviewSourceIcon = holder.reviewSourceIcon;

        reviewSource.setText(currBookReview.getReviewSource().trim());
        reviewSnippet.setText(currBookReview.getReviewSnippet().trim());
        reviewDate.setText(formatDate(currBookReview.getReviewDate()));
        reviewRating.setRating(currBookReview.getReviewStarRating());
        reviewRatingText.setText(String.valueOf(currBookReview.getReviewStarRating()));

        if (currBookReview.getReviewSourceLogo() != null) {
            Picasso.get().load(currBookReview.getReviewSourceLogo()).into(reviewSourceIcon);
        } else {
            reviewSourceIcon.setImageResource(R.drawable.source_icon_placeholder);
        }
    }

    @SuppressLint("WrongConstant")
    private String formatDate(String date) {
        String resultDate = null;
        try {
            //Input Date Format
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            // Output date format
            SimpleDateFormat dFormatFinal = new SimpleDateFormat("dd MMM");
            Date mDate = dFormat.parse(date);
            resultDate = dFormatFinal.format(mDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    @Override
    public int getItemCount() {
        return mBookReviews.size();
    }

    // Adds new items to mNews and refreshes the layout
    public void addAll(List<BookReview> bookReviewList) {
        mBookReviews.clear();
        mBookReviews.addAll(bookReviewList);
        notifyDataSetChanged();
    }

    // Clears mNews
    public void clearAll() {
        mBookReviews.clear();
        notifyDataSetChanged();
    }
}
