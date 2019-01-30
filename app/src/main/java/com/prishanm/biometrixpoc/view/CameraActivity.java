package com.prishanm.biometrixpoc.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.prishanm.biometrixpoc.R;
import com.prishanm.biometrixpoc.common.ApplicationCommons;
import com.prishanm.biometrixpoc.common.CameraUtils;
import com.prishanm.biometrixpoc.common.FileUtils;
import com.prishanm.biometrixpoc.databinding.ActivityCameraBinding;
import com.prishanm.biometrixpoc.di.Injectable;
import com.prishanm.biometrixpoc.viewModel.CameraViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import ly.img.android.pesdk.assets.filter.basic.FilterPackBasic;
import ly.img.android.pesdk.backend.model.constant.Directory;
import ly.img.android.pesdk.backend.model.state.EditorLoadSettings;
import ly.img.android.pesdk.backend.model.state.EditorSaveSettings;
import ly.img.android.pesdk.backend.model.state.manager.SettingsList;
import ly.img.android.pesdk.ui.activity.ImgLyIntent;
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder;
import ly.img.android.pesdk.ui.model.state.UiConfigFilter;

/**
 * Created by Prishan Maduka on 28,January,2019
 */
public class CameraActivity extends AppCompatActivity implements Injectable {

    @BindView(R.id.btnCapture) Button btnCapture;

    @BindView(R.id.btnCheck) Button btnCheck;

    @BindView(R.id.btnEnhance) Button btnEnhance;

    @BindView(R.id.imgCapture) ImageView imgImage;

    @BindView(R.id.txtCapture) TextView txtData;

    private Context _Context;
    private Uri resultURI;

    private static final int requestPermissionID = 101;

    //Capture Image Request Code
    private static final int CAPTURE_IMAGE = 1000;

    public static int PESDK_RESULT = 1;

    private ActivityCameraBinding activityCameraBinding;

    @Nullable
    @Inject
    ViewModelProvider.Factory factory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);

        activityCameraBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera);

        ButterKnife.bind(this);

        final CameraViewModel cameraViewModel;

        cameraViewModel = ViewModelProviders.of(this, factory)
                .get(CameraViewModel.class);


        activityCameraBinding.setCameraViewModel(cameraViewModel);
        _Context = getApplicationContext();
    }

    @OnClick({R.id.btnCapture,R.id.btnCheck,R.id.btnEnhance})
    public void setButtonOnClickEvent(View view){
        if (view.getId() == R.id.btnCapture) {

            if (CameraUtils.checkPermissions(_Context)) {
                captureImage();
            } else {
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestPermissionID);

                return;
            }

        } if (view.getId() == R.id.btnCheck) {

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
                                        ApplicationCommons.processTextBlock(firebaseVisionText);
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

        } else if(view.getId() == R.id.btnEnhance){
            openEditor(resultURI);
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

        /*SettingsList settingsList = createPesdkSettingsList();

        new CameraPreviewBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, PESDK_RESULT);*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        resultURI = FileUtils.getOutputImageUri(_Context);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultURI);

        startActivityForResult(intent, CAPTURE_IMAGE);


    }

    private SettingsList createPesdkSettingsList() {

        // Create a empty new SettingsList and apply the changes on this referance.
        SettingsList settingsList = new SettingsList();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        // If you include our asset Packs and you use our UI you also need to add them to the UI,
        // otherwise they are only available for the backend
        // See the specific feature sections of our guides if you want to know how to add our own Assets.

        settingsList.getSettingsModel(UiConfigFilter.class).setFilterList(
                FilterPackBasic.getFilterPack()
        );


        // Set custom editor image export settings
        settingsList.getSettingsModel(EditorSaveSettings.class)
                .setExportDir(Directory.DCIM, FileUtils.FOLDER_NAME)
                .setExportPrefix("IMGR_"+timeStamp)
                .setSavePolicy(EditorSaveSettings.SavePolicy.RETURN_ALWAYS_ONLY_OUTPUT);

        return settingsList;
    }

    private void openEditor(Uri inputImage) {
        SettingsList settingsList = createPesdkSettingsList();

        // Set input image
        settingsList.getSettingsModel(EditorLoadSettings.class)
                .setImageSource(inputImage);

        new PhotoEditorBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, PESDK_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {/*
        if (resultCode == RESULT_OK && requestCode == PESDK_RESULT) {
            // Editor has saved an Image.
            resultURI = data.getParcelableExtra(ImgLyIntent.RESULT_IMAGE_URI);
            Uri sourceURI = data.getParcelableExtra(ImgLyIntent.SOURCE_IMAGE_URI);

            // Scan result uri to show it up in the Gallery
            if (resultURI != null) {
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(resultURI));
                try {
                    Bitmap bitmap = CameraUtils.handleSamplingAndRotationBitmap(_Context,resultURI);
                    imgImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Scan source uri to show it up in the Gallery
            if (sourceURI != null) {
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(sourceURI));
            }

            Log.i("PESDK", "Source image is located here " + sourceURI);
            Log.i("PESDK", "Result image is located here " + resultURI);

            // TODO: Do something with the result image

            // OPTIONAL: read the latest state to save it as a serialisation
            SettingsList lastState = data.getParcelableExtra(ImgLyIntent.SETTINGS_LIST);
            try {
                new PESDKFileWriter(lastState).writeJson(new File(
                        Environment.getExternalStorageDirectory(),
                        "serialisationReadyToReadWithPESDKFileReader.json"
                ));
            } catch (IOException e) { e.printStackTrace(); }

        } else if (resultCode == RESULT_CANCELED && requestCode == PESDK_RESULT) {

            // Editor was canceled
            Uri sourceURI = data.getParcelableExtra(ImgLyIntent.SOURCE_IMAGE_URI);
            // TODO: Do something...
        }*/

        if (requestCode == CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {

                try {

                    //bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
                    /*try {
                        bitmap = CameraUtils.handleSamplingAndRotationBitmap(_Context,resultURI);
                        imgImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    openEditor(resultURI);


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        } else if(resultCode == RESULT_OK && requestCode == PESDK_RESULT){

            Uri resultUri = data.getParcelableExtra(ImgLyIntent.RESULT_IMAGE_URI);

            try {
                if(resultUri != null){
                    Bitmap bitmap;
                    if(FileUtils.copyFileFromUri(resultUri,resultURI)){
                        bitmap = CameraUtils.handleSamplingAndRotationBitmap(_Context,resultURI);
                    } else {
                        bitmap = CameraUtils.handleSamplingAndRotationBitmap(_Context,resultUri);

                    }
                    imgImage.setImageBitmap(bitmap);
                }
            }catch (Exception e){

            }

        }
    }

}
