package com.prishanm.biometrixpoc.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prishanm.biometrixpoc.R;
import com.prishanm.biometrixpoc.common.ApplicationConstants;
import com.prishanm.biometrixpoc.common.CameraUtils;
import com.prishanm.biometrixpoc.common.FileUtils;
import com.prishanm.biometrixpoc.databinding.ActivityCameraStepTwoBinding;
import com.prishanm.biometrixpoc.di.Injectable;
import com.prishanm.biometrixpoc.service.parcelable.CustomerDetailsModel;
import com.prishanm.biometrixpoc.viewModel.CameraStepTwoViewModel;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import ly.img.android.pesdk.backend.model.state.EditorLoadSettings;
import ly.img.android.pesdk.backend.model.state.manager.SettingsList;
import ly.img.android.pesdk.ui.activity.ImgLyIntent;
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder;

/**
 * Created by Prishan Maduka on 01,February,2019
 */
public class CameraStepTwoActivity extends AppCompatActivity implements Injectable {

    @Nullable
    @Inject
    ViewModelProvider.Factory factory;

    @BindView(R.id.btnCapture)
    ImageButton btnCapture;

    @BindView(R.id.btnCheck) ImageButton btnCheck;

    @BindView(R.id.btnNext) ImageButton btnNext;

    @BindView(R.id.imgCapture)
    ImageView imgImage;

    private ActivityCameraStepTwoBinding cameraStepTwoBinding;
    private CameraStepTwoViewModel viewModel;
    private Context context;

    private String customerDetailsGson;
    private Uri resultURI;
    private CustomerDetailsModel customerDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);

        cameraStepTwoBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera_step_two);

        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this, factory)
                .get(CameraStepTwoViewModel.class);

        cameraStepTwoBinding.setStepTwoViewModel(viewModel);
        cameraStepTwoBinding.setIsImageCaptured(false);

        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        customerDetailsGson = getIntent().getStringExtra(ApplicationConstants.TAG_INTENT_CUSTOMER_DATA);
        if(customerDetailsGson != null){
            Gson gson = new Gson();
            customerDetails = gson.fromJson(customerDetailsGson, CustomerDetailsModel.class);

        }


    }

    @OnClick({R.id.btnCapture,R.id.btnCheck,R.id.btnNext})
    public void setButtonOnClickEvent(View view){

        if(view.getId() == R.id.btnCapture){

            captureImage();

        } else if(view.getId() == R.id.btnCheck){

            if( resultURI!= null ){

            } else{
                Toast.makeText(context,ApplicationConstants.CAPTURE_SELFIE_VALIDATE_ERROR,Toast.LENGTH_SHORT).show();
            }

        } else if(view.getId() == R.id.btnNext){

        }
    }

    private void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        resultURI = FileUtils.tempURI(context);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultURI);

        startActivityForResult(intent, ApplicationConstants.CAPTURE_IMAGE);


    }

    private void openEditor(Uri inputImage) {
        SettingsList settingsList = FileUtils.createPesdkSettingsList();

        // Set input image
        settingsList.getSettingsModel(EditorLoadSettings.class)
                .setImageSource(inputImage);

        new PhotoEditorBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, ApplicationConstants.PESDK_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ApplicationConstants.CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {

                openEditor(resultURI);

            }
        } else if(resultCode == RESULT_OK && requestCode == ApplicationConstants.PESDK_RESULT){

            Uri resultUri = data.getParcelableExtra(ImgLyIntent.RESULT_IMAGE_URI);

            try {
                if(resultUri != null){
                    Bitmap bitmap;
                    resultURI = resultUri;
                    bitmap = CameraUtils.handleSamplingAndRotationBitmap(context,resultUri);
                    imgImage.setImageBitmap(bitmap);
                    cameraStepTwoBinding.setIsImageCaptured(true);
                }
            }catch (Exception e){

            }

        }
    }
}
