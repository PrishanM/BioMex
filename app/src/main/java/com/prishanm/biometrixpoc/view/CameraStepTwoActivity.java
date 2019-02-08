package com.prishanm.biometrixpoc.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prishanm.biometrixpoc.R;
import com.prishanm.biometrixpoc.common.ApplicationCommons;
import com.prishanm.biometrixpoc.common.ApplicationConstants;
import com.prishanm.biometrixpoc.common.CameraUtils;
import com.prishanm.biometrixpoc.common.FileUtils;
import com.prishanm.biometrixpoc.databinding.ActivityCameraStepTwoBinding;
import com.prishanm.biometrixpoc.di.Injectable;
import com.prishanm.biometrixpoc.service.model.FaceDetectionRequest;
import com.prishanm.biometrixpoc.service.model.FaceDetectionResponse;
import com.prishanm.biometrixpoc.service.parcelable.CustomerDetailsModel;
import com.prishanm.biometrixpoc.viewModel.CameraStepTwoViewModel;

import java.io.ByteArrayOutputStream;

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
    private boolean isValid = false;
    private int actionId = 1;
    private Uri resultURI;
    private CustomerDetailsModel customerDetails;
    private ProgressDialog progressDialog;

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

        progressDialog = ApplicationCommons.showProgressDialog(context,ApplicationConstants.TEXT_VALIDATING, ProgressDialog.STYLE_SPINNER);

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

                progressDialog.show();

                FaceDetectionRequest faceDetectionRequest = new FaceDetectionRequest();

                /** testing code **/

                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pic_selfie);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                byte[] byteArrayImage = baos.toByteArray();

                String base64Image = "";

                base64Image = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

                faceDetectionRequest.setImage(base64Image);

                /** End of testing code **/

                //faceDetectionRequest.setImage(CameraUtils.convertToBase64(resultURI.getPath()));
                faceDetectionRequest.setSessionId(customerDetails.getSessionId());

                viewModel.checkMatchingFace(faceDetectionRequest);

                observeViewModel();

            } else{
                Toast.makeText(context,ApplicationConstants.CAPTURE_SELFIE_VALIDATE_ERROR,Toast.LENGTH_SHORT).show();
            }

        } else if(view.getId() == R.id.btnNext){

            if(isValid){

                Gson gson = new Gson();
                String customerDataObjectAsAString = gson.toJson(customerDetails);

                Intent intent= new Intent(CameraStepTwoActivity.this, CameraStepThreeActivity.class);
                intent.putExtra(ApplicationConstants.TAG_INTENT_ACTION_ID,actionId);
                intent.putExtra(ApplicationConstants.TAG_INTENT_CUSTOMER_DATA,customerDataObjectAsAString);
                startActivity(intent);

            } else {

            }
        }
    }

    private void observeViewModel() {
        viewModel.getFaceDetectionResponseObservable().observe(this,faceDetectionResponse -> {

            if(progressDialog!=null)
                progressDialog.dismiss();

            if(faceDetectionResponse!= null){
                proceedWithResponse(faceDetectionResponse);
            }
        });
    }

    private void proceedWithResponse(FaceDetectionResponse faceDetectionResponse){

        if(faceDetectionResponse.getResultcode().equalsIgnoreCase(ApplicationConstants.SUCCESS_RESPONSE_CODE)){

            customerDetails.setSessionId(faceDetectionResponse.getSessionId());

            progressDialog.show();

            viewModel.getRandomLiveAction(faceDetectionResponse.getSessionId());

            observeLiveAction();

        } else {

            isValid = false;

            AlertDialog errorDialog = ApplicationCommons.showAlertDialog(context,
                    ApplicationConstants.TITLE_ERROR,
                    faceDetectionResponse.getResult(),
                    "OK",
                    null);

            errorDialog.show();

            errorDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> errorDialog.dismiss());
        }

    }

    private void observeLiveAction() {

        viewModel.getRandomLiveActionIdObservable().observe(this,liveActionIdResponse -> {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if(liveActionIdResponse.getResultcode().equalsIgnoreCase(ApplicationConstants.SUCCESS_RESPONSE_CODE)){

                isValid = true;
                actionId = liveActionIdResponse.getActionId();

                AlertDialog successDialog = ApplicationCommons.showAlertDialog(context,
                        ApplicationConstants.TITLE_CONGRATULATIONS,
                        ApplicationConstants.TEXT_SUCCESSFULLY_FACE_VERIFIED+"\n"+ApplicationConstants.TEXT_PROCEED_FINAL_STEP,
                        "OK",
                        null);

                successDialog.show();

                successDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> successDialog.dismiss());

            } else {

                isValid = false;

                AlertDialog errorDialog = ApplicationCommons.showAlertDialog(context,
                        ApplicationConstants.TITLE_ERROR,
                        liveActionIdResponse.getResult(),
                        "OK",
                        null);

                errorDialog.show();

                errorDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> errorDialog.dismiss());

            }



        });
    }


    private void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        resultURI = FileUtils.tempURI(context,ApplicationConstants.FILE_TYPE_IMAGE);

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
