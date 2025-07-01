package com.hackathon.unicurr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 1;

    private FrameLayout qrScannerSection;
    private ImageButton qrButton;
    private CompoundBarcodeView barcodeView;  // View for QR scanning

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Edge-to-edge system bar padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        qrScannerSection = findViewById(R.id.qrScannerSection);
        qrButton = findViewById(R.id.QRButton);

        // Set up click listener
        qrButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            } else {
                startQRScanner();
            }
        });
    }

    private void startQRScanner() {
        qrScannerSection.setVisibility(View.VISIBLE);

        barcodeView = new CompoundBarcodeView(this);

        // Make scanner view square (e.g., 300x300dp)
        int sizeInDp = 300;
        float scale = getResources().getDisplayMetrics().density;
        int sizeInPx = (int) (sizeInDp * scale + 0.5f);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(sizeInPx, sizeInPx);
        params.gravity = Gravity.CENTER; // center the scanner view in qrScannerSection
        barcodeView.setLayoutParams(params);

        qrScannerSection.removeAllViews();
        qrScannerSection.addView(barcodeView);

        // (Optional) Keep all formats to scan both barcodes & QR codes
        barcodeView.decodeContinuous(result -> {
            Toast.makeText(MainActivity.this, "Scanned: " + result.getText(), Toast.LENGTH_SHORT).show();
            barcodeView.pause();
            qrScannerSection.setVisibility(View.GONE);
        });

        barcodeView.resume();
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQRScanner();
            } else {
                Toast.makeText(this, "Camera permission is required for scanning", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
