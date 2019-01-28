package com.prishanm.biometrixpoc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Capture Image Request Code
    private static final int CAPTURE_IMAGE = 1000;

    private static final int requestPermissionID = 101;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 2;

    private Button btnCapture, btnCheck;
    private ImageView imgImage;
    private TextView txtData;
    private Context _Context;

    //Image SourcePath
    private static String imageStoragePath;
    Uri fileUri;
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCapture = findViewById(R.id.btnCapture);
        btnCheck = findViewById(R.id.btnCheck);
        imgImage = findViewById(R.id.imgCapture);
        txtData = findViewById(R.id.txtCapture);

        btnCapture.setOnClickListener(this);
        btnCheck.setOnClickListener(this);

        _Context = getApplicationContext();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCapture) {

            if (CameraUtils.checkPermissions(_Context)) {
                captureImage();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestPermissionID);

                return;
            }


        } else if (v.getId() == R.id.btnCheck) {

            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(_Context, fileUri);

                FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                        .getOnDeviceTextRecognizer();


                Task<FirebaseVisionText> result =
                        detector.processImage(image)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                        processTextBlock(firebaseVisionText);
                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                            }
                                        });


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.d("ERROR", "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (!CameraUtils.checkPermissions(_Context)) {
                return;
            }
            captureImage();
        }
    }

    private void captureImage() {

        //Capturing Images Using Camera Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);

        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        fileUri = CameraUtils.getOutputMediaFileUri(_Context, file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAPTURE_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {

                try {

                    //bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
                    try {
                        bitmap = CameraUtils.handleSamplingAndRotationBitmap(_Context,fileUri);
                        imgImage.setImageBitmap(CameraUtils.rotateImage(bitmap,90));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }




                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void processTextBlock(FirebaseVisionText result) {
        // [START mlkit_process_text_block]
        String resultText = result.getText();
        int x = 0;
        for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {


            String blockText = block.getText();
            Float blockConfidence = block.getConfidence();
            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();


            for (FirebaseVisionText.Line line: block.getLines()) {

                String lineText = line.getText();
                Log.d("++++"+x,lineText);
                x++;

            }
        }
        // [END mlkit_process_text_block]
    }
}
