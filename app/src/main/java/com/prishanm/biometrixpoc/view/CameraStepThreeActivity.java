package com.prishanm.biometrixpoc.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.prishanm.biometrixpoc.R;
import com.prishanm.biometrixpoc.common.ApplicationCommons;
import com.prishanm.biometrixpoc.common.ApplicationConstants;
import com.prishanm.biometrixpoc.common.FileUtils;
import com.prishanm.biometrixpoc.databinding.ActivityCameraStepThreeBinding;
import com.prishanm.biometrixpoc.di.Injectable;
import com.prishanm.biometrixpoc.viewModel.CameraStepThreeViewModel;

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

/**
 * Created by Prishan Maduka on 07,February,2019
 */
public class CameraStepThreeActivity extends AppCompatActivity implements Injectable {

    @Nullable
    @Inject
    ViewModelProvider.Factory factory;

    @BindView(R.id.btnCapture)
    ImageButton btnCapture;

    @BindView(R.id.btnCheck) ImageButton btnCheck;

    @BindView(R.id.btnNext) ImageButton btnNext;

    @BindView(R.id.imgCapture)
    ImageView imgImage;

    private ActivityCameraStepThreeBinding cameraStepThreeBinding;
    private CameraStepThreeViewModel stepThreeViewModel;
    private ProgressDialog progressDialog;

    private Context context;
    private Uri resultURI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);

        cameraStepThreeBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera_step_three);

        ButterKnife.bind(this);

        stepThreeViewModel = ViewModelProviders.of(this, factory)
                .get(CameraStepThreeViewModel.class);

        cameraStepThreeBinding.setStepThreeViewModel(stepThreeViewModel);
        cameraStepThreeBinding.setIsImageCaptured(false);
        
        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = ApplicationCommons.showProgressDialog(context, ApplicationConstants.TEXT_VALIDATING, ProgressDialog.STYLE_SPINNER);



    }

    @OnClick({R.id.btnCapture,R.id.btnCheck,R.id.btnNext})
    public void setOnButtonClick(View view){

        if(view.getId() == R.id.btnCapture){

            captureVideo();

        }
        
    }

    private void captureVideo() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        resultURI = FileUtils.tempVideoURI(context);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultURI);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 7);

        startActivityForResult(intent, ApplicationConstants.CAPTURE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ApplicationConstants.CAPTURE_VIDEO && resultCode == Activity.RESULT_OK) {

        }
    }
}
