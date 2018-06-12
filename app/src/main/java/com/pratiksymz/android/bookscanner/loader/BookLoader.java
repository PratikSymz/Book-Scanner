package com.pratiksymz.android.bookscanner.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.pratiksymz.android.bookscanner.model.Book;
import com.pratiksymz.android.bookscanner.utils.QueryUtils;

public class BookLoader extends AsyncTaskLoader<Book> {
    private static final String LOG_TAG = BookLoader.class.getSimpleName();
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Book loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchBookData(mUrl, getContext());
    }
}
