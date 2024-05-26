package com.example.itisconnect.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Size;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.itisconnect.R;
import com.example.itisconnect.fragments.BottomDialogFragment;
import com.example.itisconnect.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QrCodeActivity extends AppCompatActivity
{
    private ListenableFuture cameraProviderFuture;
    private ExecutorService executor;
    private PreviewView previewView;
    private MyImageAnalyzer imageAnalyzer;
    private ImageView closeButton;
    private Boolean isCheckIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        previewView = findViewById(R.id.camera_preview_view);
        this.getWindow().setFlags(1024, 1024);

        executor = Executors.newSingleThreadExecutor();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        closeButton = findViewById(R.id.close_button_camera);

        imageAnalyzer = new MyImageAnalyzer(getSupportFragmentManager());

        cameraProviderFuture.addListener(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (ActivityCompat.checkSelfPermission(QrCodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(QrCodeActivity.this, new String[]{Manifest.permission.CAMERA}, 101);
                    }
                    else
                    {
                        ProcessCameraProvider cameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                        bindPreview(cameraProvider);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));

        closeButton.setOnClickListener(v -> finish());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101 && grantResults.length > 0)
        {
            ProcessCameraProvider cameraProvider = null;

            try
            {
                cameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            bindPreview(cameraProvider);
        }
    }

    private void bindPreview(ProcessCameraProvider cameraProvider)
    {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageCapture imageCapture = new ImageCapture.Builder().build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720)).setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        imageAnalysis.setAnalyzer(executor, imageAnalyzer);
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);
    }

    public class MyImageAnalyzer implements ImageAnalysis.Analyzer
    {
        private FragmentManager fragmentManager;
        private BottomDialogFragment bottomDialogFragment;

        public MyImageAnalyzer(FragmentManager supportFragmentManager)
        {
            fragmentManager = supportFragmentManager;
            bottomDialogFragment = new BottomDialogFragment();
        }

        @Override
        public void analyze(@NonNull ImageProxy image)
        {
            scanBarcode(image);
        }

        private void scanBarcode(ImageProxy image)
        {
            @SuppressLint("UnsafeOptInUsageError") Image image1 = image.getImage();

            assert image1 != null;
            InputImage inputImage = InputImage.fromMediaImage(image1, image.getImageInfo().getRotationDegrees());

            BarcodeScannerOptions options = new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC).build();
            BarcodeScanner scanner = BarcodeScanning.getClient(options);

            Task<List<Barcode>> result = scanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Barcode>>()
            {
                @Override
                public void onSuccess(List<Barcode> barcodes)
                {
                    ReaderBarCodeData(barcodes);
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(QrCodeActivity.this, "Đọc QR thất bại!", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>()
            {
                @Override
                public void onComplete(@NonNull Task<List<Barcode>> task)
                {
                    image.close();
                }
            });
        }

        private void ReaderBarCodeData(List<Barcode> barcodes)
        {
            for (Barcode barcode : barcodes)
            {
                Rect bounds = barcode.getBoundingBox();
                Point[] corners = barcode.getCornerPoints();

                String rawValue = barcode.getRawValue();
                int valueType = barcode.getValueType();

                if (valueType == Barcode.TYPE_TEXT && rawValue != null && rawValue.contains("&") && !isCheckIn)
                {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String[] data = rawValue.split("&");
                    String idEvent = data[0].trim();
                    String code = data[1].trim();

                    database.getReference("Events").child(idEvent).child("currentCode").addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            String curCode = snapshot.getValue(String.class);
                            if (code.equals(curCode))
                            {
                                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                                FirebaseUser currentUser = fAuth.getCurrentUser();

                                database.getReference("Events").child(idEvent).child("participants").addValueEventListener(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot)
                                    {
                                        List<String> participants = (List<String>) snapshot.getValue();
                                        if (participants != null)
                                        {
                                            if (!participants.contains(currentUser.getUid()) && !participants.contains(currentUser.getEmail()))
                                            {
                                                participants.add(currentUser.getEmail());
                                                database.getReference("Events").child(idEvent).child("participants").setValue(participants);
                                                Toast.makeText(QrCodeActivity.this, "Check in thành công!", Toast.LENGTH_SHORT).show();
                                                isCheckIn = true;
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(QrCodeActivity.this, "Bạn đã check in rồi!", Toast.LENGTH_SHORT).show();
                                                finish();
                                                isCheckIn = true;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error)
                                    {

                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(QrCodeActivity.this, "Mã QR không hợp lệ!", Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });
                }
            }
        }
    }
}