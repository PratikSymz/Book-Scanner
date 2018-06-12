package com.pratiksymz.android.bookscanner.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.pratiksymz.android.bookscanner.R;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class BarcodeScannerActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private static final String LOG_TAG = BarcodeScannerActivity.class.getSimpleName();
    public static final String BARCODE_OBJECT = "String";
    private static final String KEY = "query";
    private Context mContext = BarcodeScannerActivity.this;
    private BarcodeReader mBarcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        // Initialize the Barcode
        mBarcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(
                R.id.barcode_fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBarcodeReader.resumeScanning();
    }

    @Override
    public void onScanned(final Barcode barcode) {
        mBarcodeReader.playBeep();
        Log.v(LOG_TAG, "Barcode Data Read: " + barcode.displayValue);
        runOnUiThread(() -> {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View alertDialogView = inflater.inflate(R.layout.layout_barcode_dialog, null);
            alertBuilder.setView(alertDialogView);

            TextView barcodeText = alertDialogView.findViewById(R.id.barcode_text);
            Button rescanButton = alertDialogView.findViewById(R.id.button_rescan);
            Button submitButton = alertDialogView.findViewById(R.id.button_submit);

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.setCancelable(true);
            alertDialog.show();

            if (barcode.rawValue != null) {
                barcodeText.setText(barcode.rawValue);
                rescanButton.setOnClickListener(view -> mBarcodeReader.resumeScanning());
                submitButton.setOnClickListener(view -> {
                    alertDialog.dismiss();

                    Intent bookReviewsIntent = new Intent(mContext, BookReviewsActivity.class);
                    bookReviewsIntent.putExtra(KEY, barcode.rawValue);
                    startActivity(bookReviewsIntent);
                });
            }
        });

        mBarcodeReader.pauseScanning();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        // NO IMPLEMENTATION
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        // NO IMPLEMENTATION
    }

    @Override
    public void onScanError(String errorMessage) {
        DynamicToast.makeError(this, errorMessage).show();
        Log.e(LOG_TAG, errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {
        // NO IMPLEMENTATION
    }
}
