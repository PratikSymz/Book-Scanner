package com.pratiksymz.android.bookscanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.pratiksymz.android.bookscanner.R;
import com.pratiksymz.android.bookscanner.activities.ocr_misc.OcrCaptureActivity;

public class OCRScannerActivity extends AppCompatActivity {
    private static final String LOG_TAG = OCRScannerActivity.class.getSimpleName();

    private EditText textValue;
    Button Camera, mSearchButton, Barcode;
    private static final int RC_OCR_CAPTURE = 9003;
    private static final int RC_BAR_CAPTURE = 9005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_scanner);

        Barcode = findViewById(R.id.barcode);
        textValue = findViewById(R.id.text_value);
        Camera = findViewById(R.id.camera);
        mSearchButton = findViewById(R.id.search);
        mSearchButton.setOnClickListener(view -> {
            Intent bookIntent = new Intent(this, BookReviewsActivity.class);
            bookIntent.putExtra("query", textValue.getText().toString().trim());
            startActivity(bookIntent);
        });

        Camera.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.flash_required)
                    .setMessage("Yes or No")
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                        intent.putExtra(OcrCaptureActivity.AUTO_FOCUS, true);
                        intent.putExtra(OcrCaptureActivity.USE_FLASH, true);
                        startActivityForResult(intent, RC_OCR_CAPTURE);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> {
                        Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                        intent.putExtra(OcrCaptureActivity.AUTO_FOCUS, true);
                        startActivityForResult(intent, RC_OCR_CAPTURE);
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        });

        Barcode.setOnClickListener(v -> {
            // launch Ocr capture activity.
            Intent intent = new Intent(this, BarcodeScannerActivity.class);
            startActivityForResult(intent, RC_BAR_CAPTURE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textValue.getText().toString().isEmpty()) {
            textValue.setVisibility(View.GONE);
        } else {
            textValue.setVisibility(View.VISIBLE);
            mSearchButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TEXT_BLOCK_OBJECT);

                    textValue.setText(text);
                    Log.d(LOG_TAG, "Text read: " + text);
                } else {

                    Log.d(LOG_TAG, "No Text captured, intent data is null");
                }
            }
        } else if (requestCode == RC_BAR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(BarcodeScannerActivity.BARCODE_OBJECT);
                    textValue.setText(text);
                    Log.d(LOG_TAG, "Text read: " + text);
                } else {

                    Log.d(LOG_TAG, "No Text captured, intent data is null");
                }
            }
        }
    }
}
