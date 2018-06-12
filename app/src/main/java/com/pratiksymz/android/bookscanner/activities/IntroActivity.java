package com.pratiksymz.android.bookscanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pratiksymz.android.bookscanner.R;

public class IntroActivity extends AppCompatActivity {
    private static final String KEY = "query";

    private CardView mBarcodeCard, mOCRCard, mTypeYourselfCard, mFindBookstoreCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mBarcodeCard = findViewById(R.id.card_barcode);
        mOCRCard = findViewById(R.id.card_ocr);
        mTypeYourselfCard = findViewById(R.id.card_type_yourself);
        mFindBookstoreCard = findViewById(R.id.card_find_bookstore);

        mBarcodeCard.setOnClickListener(view -> {
            Intent barcodeActivityIntent = new Intent(this, BarcodeScannerActivity.class);
            startActivity(barcodeActivityIntent);
        });

        mOCRCard.setOnClickListener(view -> {
            Intent ocrScannerActivityIntent = new Intent(this, OCRScannerActivity.class);
            startActivity(ocrScannerActivityIntent);
        });

        mTypeYourselfCard.setOnClickListener(view -> {
            runOnUiThread(() -> {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = LayoutInflater.from(this);
                View alertView = inflater.inflate(
                        R.layout.layout_type_yourself_dialog,
                        findViewById(R.id.ltyd_root_view)
                );

                alertBuilder.setView(alertView);

                final AlertDialog alertDialog = alertBuilder.create();
                alertDialog.setCancelable(false);
                EditText bookQuery = alertView.findViewById(R.id.ltyd_book_query);
                Button searchQuery = alertView.findViewById(R.id.ltyd_button_search);
                searchQuery.setOnClickListener(v -> {
                    // Send the query data to BookReviewsActivity
                    Intent bookReviewsIntent = new Intent(this, BookReviewsActivity.class);
                    bookReviewsIntent.putExtra(KEY, bookQuery.getText().toString().trim());
                    startActivity(bookReviewsIntent);

                    // Dismiss the dialog
                    alertDialog.dismiss();
                });

                alertDialog.show();
            });
        });
    }
}
