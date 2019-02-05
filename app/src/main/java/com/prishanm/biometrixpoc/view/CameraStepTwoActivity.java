package com.prishanm.biometrixpoc.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.prishanm.biometrixpoc.R;
import com.prishanm.biometrixpoc.common.ApplicationMessages;
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

import static com.prishanm.biometrixpoc.common.ApplicationMessages.CAPTURE_IMAGE;

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

        customerDetailsGson = getIntent().getStringExtra(ApplicationMessages.TAG_INTENT_CUSTOMER_DATA);
        if(customerDetailsGson != null){
            Gson gson = new Gson();
            customerDetails = gson.fromJson(customerDetailsGson, CustomerDetailsModel.class);

        }


    }

    @OnClick({R.id.btnCapture,R.id.btnCheck,R.id.btnNext})
    public void setButtonOnClickEvent(View view){

        if(view.getId() == R.id.btnCapture){

        } else if(view.getId() == R.id.btnCheck){

        } else if(view.getId() == R.id.btnCheck){

        }
    }

    private void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        resultURI = FileUtils.getOutputImageUri(context);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultURI);

        startActivityForResult(intent, CAPTURE_IMAGE);


    }
}
