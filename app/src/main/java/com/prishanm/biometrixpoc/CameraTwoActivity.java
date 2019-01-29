package com.prishanm.biometrixpoc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ly.img.android.pesdk.assets.filter.basic.FilterPackBasic;
import ly.img.android.pesdk.backend.model.constant.Directory;
import ly.img.android.pesdk.backend.model.state.CameraSettings;
import ly.img.android.pesdk.backend.model.state.EditorLoadSettings;
import ly.img.android.pesdk.backend.model.state.EditorSaveSettings;
import ly.img.android.pesdk.backend.model.state.manager.SettingsList;
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder;
import ly.img.android.pesdk.ui.model.state.UiConfigFilter;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by Prishan Maduka on 28,January,2019
 */
public class CameraTwoActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnCapture, btnCheck, btnEnhance;
    private ImageView imgImage;
    private TextView txtData;
    private Context _Context;

    private Uri resultURI;
    private String imageStoragePath;
    public Bitmap bitmap;

    private static final int requestPermissionID = 101;

    //Capture Image Request Code
    private static final int CAPTURE_IMAGE = 1000;

    public static int PESDK_RESULT = 1;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnCapture) {

            if (CameraUtils.checkPermissions(_Context)) {
                captureImage();
            } else {
                ActivityCompat.requestPermissions(CameraTwoActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestPermissionID);

                return;
            }

        } if (v.getId() == R.id.btnCheck) {

            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(_Context, resultURI);

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

        } else if(v.getId() == R.id.btnEnhance){
            openEditor(resultURI);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCapture = findViewById(R.id.btnCapture);
        btnCheck = findViewById(R.id.btnCheck);
        btnEnhance = findViewById(R.id.btnEnhance);
        imgImage = findViewById(R.id.imgCapture);
        txtData = findViewById(R.id.txtCapture);

        btnCapture.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        btnEnhance.setOnClickListener(this);

        _Context = getApplicationContext();
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

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        resultURI = FileUtils.getOutputImageUri(_Context);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultURI);

        startActivityForResult(intent, CAPTURE_IMAGE);


    }


    private void openEditor(Uri inputImage) {
        UCrop.of(inputImage, inputImage)
                .withAspectRatio(5, 4)
                .withMaxResultSize(1024, 1024)
                .start(CameraTwoActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {

                try {

                    //bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
                    try {
                        bitmap = CameraUtils.handleSamplingAndRotationBitmap(_Context,resultURI);
                        imgImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }




                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        } else if(resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP){
            final Uri resultUri = UCrop.getOutput(data);
            try {
                bitmap = CameraUtils.handleSamplingAndRotationBitmap(_Context,resultUri);
                imgImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
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
    }
}
