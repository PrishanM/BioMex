package com.prishanm.biometrixpoc.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prishanm.biometrixpoc.R;
import com.prishanm.biometrixpoc.common.ApplicationCommons;
import com.prishanm.biometrixpoc.common.ApplicationConstants;
import com.prishanm.biometrixpoc.common.CameraUtils;
import com.prishanm.biometrixpoc.common.FileUtils;
import com.prishanm.biometrixpoc.databinding.ActivityCameraStepThreeBinding;
import com.prishanm.biometrixpoc.di.Injectable;
import com.prishanm.biometrixpoc.service.model.LivenessDetectionRequest;
import com.prishanm.biometrixpoc.service.parcelable.CustomerDetailsModel;
import com.prishanm.biometrixpoc.viewModel.CameraStepThreeViewModel;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    @BindView(R.id.txtRandomAction)
    TextView txtRandomAction;

    @BindView(R.id.btnCheck) ImageButton btnCheck;

    @BindView(R.id.imgCapture)
    ImageView imgImage;

    private ActivityCameraStepThreeBinding cameraStepThreeBinding;
    private CameraStepThreeViewModel stepThreeViewModel;
    private ProgressDialog progressDialog;
    private MediaMetadataRetriever mediaMetadataRetriever;

    private Context context;
    private Uri resultURI;
    private int actionId;
    private File savedFile;
    private CustomerDetailsModel customerDetailsModel;

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
        mediaMetadataRetriever = new MediaMetadataRetriever();

        progressDialog = ApplicationCommons.showProgressDialog(context, ApplicationConstants.TEXT_VALIDATING, ProgressDialog.STYLE_SPINNER);

        actionId = getIntent().getIntExtra(ApplicationConstants.TAG_INTENT_ACTION_ID,1);

        String customerDetailsString = getIntent().getStringExtra(ApplicationConstants.TAG_INTENT_CUSTOMER_DATA);
        if(customerDetailsString != null){
            Gson gson = new Gson();
            customerDetailsModel = gson.fromJson(customerDetailsString, CustomerDetailsModel.class);

        }

        showActionId();

    }

    private void showActionId(){

        String message = "";
        if (actionId == 1) {
            message = ApplicationConstants.ACTION_ID_1;

        } else if (actionId == 2) {
            message = ApplicationConstants.ACTION_ID_2;

        } else if (actionId == 3) {
            message = ApplicationConstants.ACTION_ID_3;

        } else if (actionId == 4) {
            message = ApplicationConstants.ACTION_ID_4;

        } else if (actionId == 5) {
            message = ApplicationConstants.ACTION_ID_5;

        } else if (actionId == 6) {
            message = ApplicationConstants.ACTION_ID_6;

        } else if (actionId == 7) {
            message = ApplicationConstants.ACTION_ID_7;

        } else if (actionId == 8) {
            message = ApplicationConstants.ACTION_ID_8;

        } else if (actionId == 9) {
            message = ApplicationConstants.ACTION_ID_9;

        } else if (actionId == 10) {
            message = ApplicationConstants.ACTION_ID_10;

        }

        txtRandomAction.setText(message);

        /*AlertDialog randomActionDialog = ApplicationCommons.showAlertDialog(context,
                ApplicationConstants.TITLE_RANDOM_ACTION,
                message,
                "OK",
                null);

        randomActionDialog.show();

        randomActionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            randomActionDialog.dismiss();
        });*/
    }

    @OnClick({R.id.btnCapture,R.id.btnCheck})
    public void setOnButtonClick(View view){

        if(view.getId() == R.id.btnCapture){

            captureVideo();

        } else {

            if(savedFile != null){

                progressDialog.show();

                LivenessDetectionRequest request = new LivenessDetectionRequest();

                request.setSessionId(customerDetailsModel.getSessionId());
                request.setActionId(actionId);
                request.setImage(CameraUtils.convertToBase64(savedFile.getPath()));

                stepThreeViewModel.checkLivenessDetection(request);

                observeViewModel();


            } else {
                Toast.makeText(context,ApplicationConstants.CAPTURE_IMAGE_VALIDATE_ERROR,Toast.LENGTH_SHORT).show();
            }

        }
        
    }

    private void observeViewModel() {

        stepThreeViewModel.getLivenessDetectionObservable().observe(this,livenessDetectionResponse -> {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if(livenessDetectionResponse!=null){

                AlertDialog alertDialog = ApplicationCommons.showAlertDialog(context,
                        livenessDetectionResponse.getResultcode().equalsIgnoreCase(ApplicationConstants.SUCCESS_RESPONSE_CODE)? ApplicationConstants.TITLE_CONGRATULATIONS : ApplicationConstants.TITLE_ERROR,
                        livenessDetectionResponse.getResult(),
                        "OK",
                        null);

                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

                    alertDialog.dismiss();
                });
            }
        });
    }

    private void captureVideo() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        resultURI = FileUtils.tempURI(context,ApplicationConstants.FILE_TYPE_VIDEO);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultURI);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, ApplicationConstants.VIDEO_DURATION);

        startActivityForResult(intent, ApplicationConstants.CAPTURE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ApplicationConstants.CAPTURE_VIDEO && resultCode == Activity.RESULT_OK) {

            mediaMetadataRetriever.setDataSource(resultURI.getPath());

            ArrayList<Bitmap> frameList = new ArrayList<>();
            int looper = 2000000;
            for(int i=0; i<4 ; i++){

                frameList.add(mediaMetadataRetriever.getFrameAtTime(looper));
                looper += 1000000;

            }

            Bitmap bitmap = FileUtils.mergeBitmaps(frameList);

            imgImage.setImageBitmap(bitmap);
            cameraStepThreeBinding.setIsImageCaptured(true);

            savedFile = FileUtils.saveBitmap(bitmap);
            //mergedImageUri = FileUtils.getUriFromFile(context,savedFile);

            /*Log.d("FROM FILE ----",savedFile.getPath());
            Log.d("FROM FILE ABSOLUTE ----",savedFile.getAbsolutePath());
            Log.d("FROM FILE URI ----",mergedImageUri.getPath());*/

        }
    }
}
