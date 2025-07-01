package com.hackathon.unicurr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class HomePage extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 1;

    private FrameLayout qrScannerSection;
    private ImageButton qrButton;
    private CompoundBarcodeView barcodeView;

    // Additional views (optional)
    private TextView nameText, balanceView;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homepage); // Make sure this is your actual XML filename

        // Handle system bars padding on root layout (home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        qrScannerSection = findViewById(R.id.qrScannerSection);
        qrButton = findViewById(R.id.QRButton);
        nameText = findViewById(R.id.nameText);
        balanceView = findViewById(R.id.balanceView);
        sendButton = findViewById(R.id.sendButton);

        // Optional: Set initial text for name and balance if needed
        nameText.setText("John Doe");
        balanceView.setText("4505516");

        // QR button click to request camera permission or start scanner
        qrButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            } else {
                startQRScanner();
            }
        });

        // Optional: sendButton click handler
        sendButton.setOnClickListener(v -> {
            Toast.makeText(this, "Send Money clicked", Toast.LENGTH_SHORT).show();
            // Add your send money logic here
        });
    }

    private void startQRScanner() {
        qrScannerSection.setVisibility(View.VISIBLE);

        barcodeView = new CompoundBarcodeView(this);

        // Set scanner view size (300dp square)
        int sizeInDp = 300;
        float scale = getResources().getDisplayMetrics().density;
        int sizeInPx = (int) (sizeInDp * scale + 0.5f);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(sizeInPx, sizeInPx);
        params.gravity = Gravity.CENTER;
        barcodeView.setLayoutParams(params);

        qrScannerSection.removeAllViews();
        qrScannerSection.addView(barcodeView);

        // Continuous scanning: scan all formats (QR and barcodes)
        barcodeView.decodeContinuous(result -> {
            Toast.makeText(HomePage.this, "Scanned: " + result.getText(), Toast.LENGTH_SHORT).show();
            barcodeView.pause();
            qrScannerSection.setVisibility(View.GONE);
        });

        barcodeView.resume();
    }

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
