package com.prishanm.biometrixpoc.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.prishanm.biometrixpoc.R;
import com.prishanm.biometrixpoc.common.ApplicationCommons;
import com.prishanm.biometrixpoc.common.ApplicationConstants;
import com.prishanm.biometrixpoc.common.CameraUtils;
import com.prishanm.biometrixpoc.common.FileUtils;
import com.prishanm.biometrixpoc.databinding.ActivityCameraBinding;
import com.prishanm.biometrixpoc.di.Injectable;
import com.prishanm.biometrixpoc.service.model.IdDetectionResponse;
import com.prishanm.biometrixpoc.service.parcelable.CustomerDetailsModel;
import com.prishanm.biometrixpoc.viewModel.CameraViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

import static com.prishanm.biometrixpoc.common.ApplicationConstants.CAPTURE_IMAGE;
import static com.prishanm.biometrixpoc.common.ApplicationConstants.PESDK_RESULT;

/**
 * Created by Prishan Maduka on 28,January,2019
 */
public class CameraActivity extends AppCompatActivity implements Injectable {

    @BindView(R.id.btnCapture) ImageButton btnCapture;

    @BindView(R.id.btnCheck) ImageButton btnCheck;

    @BindView(R.id.btnNext) ImageButton btnNext;

    @BindView(R.id.imgCapture) ImageView imgImage;

    private Context _Context;
    private Uri resultURI;
    private ProgressDialog progressDialog;
    private boolean isDataValid,isNewID = false;

    private CameraViewModel cameraViewModel;
    private CustomerDetailsModel detailsModel;
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

        cameraViewModel = ViewModelProviders.of(this, factory)
                .get(CameraViewModel.class);



        activityCameraBinding.setCameraViewModel(cameraViewModel);
        activityCameraBinding.setIsImageCaptured(false);
        _Context = this;

        progressDialog = ApplicationCommons.showProgressDialog(_Context,ApplicationConstants.TEXT_VALIDATING, ProgressDialog.STYLE_SPINNER);

    }

    @OnClick({R.id.btnCapture,R.id.btnCheck,R.id.btnNext})
    public void setButtonOnClickEvent(View view){
        if (view.getId() == R.id.btnCapture) {

            if (CameraUtils.checkPermissions(_Context)) {
                captureImage();
            } else {
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        ApplicationConstants.requestPermissionID);

                return;
            }

        } else if (view.getId() == R.id.btnCheck) {

            if( resultURI!= null ){
                //showBarcodeScanner();

                progressDialog.show();



                try {
                    Bitmap bitmap;
                    bitmap = CameraUtils.handleSamplingAndRotationBitmap(_Context,resultURI);
                    String encodedImage = CameraUtils.convertToBase64Bitmap(bitmap);

                    cameraViewModel.checkIdValidity(encodedImage); /* Commented in Dev Mode */



                    //Observe ViewModel changes
                    observeViewModel(cameraViewModel);

                    Log.d("XXXXXXXXXXXXXXXXXXXXXX",resultURI.toString());
                } catch (IOException e) {
                    Log.d("XXXXXXXXXXXXXXXXXXXXXX",resultURI.toString());
                }




                //Call ViewModel Repository Method to check the ID validity

                /** testing code **/

                /*Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pic_id);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                byte[] byteArrayImage = baos.toByteArray();

                String base64Image = "";

                base64Image = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

                cameraViewModel.checkIdValidity(base64Image);*/

                /** End of testing code **/



            } else {
                Toast.makeText(_Context,ApplicationConstants.CAPTURE_IMAGE_VALIDATE_ERROR,Toast.LENGTH_SHORT).show();
            }




        } else if(view.getId() == R.id.btnNext){

            if(isDataValid){

                Gson gson = new Gson();
                String customerDataObjectAsAString = gson.toJson(detailsModel);

                Intent intent = null;

                if(isNewID){
                    intent = new Intent(CameraActivity.this,NewIdActivity.class);

                } else {
                    intent = new Intent(CameraActivity.this,CameraStepTwoActivity.class);

                }

                intent.putExtra(ApplicationConstants.TAG_INTENT_CUSTOMER_DATA,customerDataObjectAsAString);
                startActivity(intent);


            } else {

                AlertDialog errorDialog = ApplicationCommons.showAlertDialog(_Context,
                        ApplicationConstants.TITLE_ERROR,
                        ApplicationConstants.CUSTOMER_DATA_MISSING_ERROR,
                        "OK",
                        null);

                errorDialog.show();

            }
        }
    }

    private void observeViewModel(CameraViewModel cameraViewModel) {

        cameraViewModel.getIdDetectionResponseObservable().observe(this, idDetectionResponse -> {

            if(progressDialog!=null)
                progressDialog.dismiss();
            if (idDetectionResponse != null) {

                showResponseDetails(idDetectionResponse);
            }
        });
    }

    private void showResponseDetails(IdDetectionResponse idDetectionResponse){

        detailsModel = new CustomerDetailsModel();


        if(idDetectionResponse.getResultcode().equalsIgnoreCase(ApplicationConstants.SUCCESS_RESPONSE_CODE) ){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(ApplicationConstants.TITLE_CUSTOMER_DETAILS);

            View customLayout = getLayoutInflater().inflate(R.layout.layout_cutomer_info, null);
            builder.setView(customLayout);

            TextView txtIdType,txtIdNumber,txtName,txtDLNumber;

            txtIdType = customLayout.findViewById(R.id.txtIdType);
            txtIdNumber = customLayout.findViewById(R.id.txtIdNumber);
            txtName = customLayout.findViewById(R.id.txtName);
            txtDLNumber = customLayout.findViewById(R.id.txtDLNumber);
            LinearLayout layoutDL = customLayout.findViewById(R.id.layoutDL);
            LinearLayout layoutNameText = customLayout.findViewById(R.id.layoutNameText);
            LinearLayout layoutName = customLayout.findViewById(R.id.layoutName);
            TextInputEditText inputName = customLayout.findViewById(R.id.inputName);

            txtIdType.setText(idDetectionResponse.getIdType());
            txtIdNumber.setText(idDetectionResponse.getIdNumber());
            txtName.setText(idDetectionResponse.getName());

            if( idDetectionResponse.getOtherIdNumber() != null && !idDetectionResponse.getOtherIdNumber().isEmpty()){
                txtDLNumber.setText(idDetectionResponse.getOtherIdNumber());
                layoutDL.setVisibility(View.VISIBLE);
            }

            if( idDetectionResponse.getName() != null && !idDetectionResponse.getName().isEmpty()){
                layoutNameText.setVisibility(View.VISIBLE);
                layoutName.setVisibility(View.GONE);
            } else {
                layoutNameText.setVisibility(View.GONE);
                layoutName.setVisibility(View.VISIBLE);
            }


            builder.setNegativeButton(ApplicationConstants.TEXT_WRONG, (dialog, which) -> {
                isDataValid = false;
                //alertDialog.dismiss();

            });

            builder.setPositiveButton(ApplicationConstants.TEXT_CORRECT, (dialog, which) -> {

            });

            AlertDialog alertDialog = builder.create();

            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

                if((inputName.getText().toString()==null || inputName.getText().toString().isEmpty()) && (idDetectionResponse.getName() == null || idDetectionResponse.getName().isEmpty())){
                    inputName.setError(ApplicationConstants.EMPTY_NAME_ERROR);
                } else {
                    isDataValid = true;
                    detailsModel.setSessionId(idDetectionResponse.getSessionId());
                    detailsModel.setIdentityType(idDetectionResponse.getIdType());
                    detailsModel.setIdNumber(idDetectionResponse.getIdNumber());

                    if( idDetectionResponse.getName() != null && !idDetectionResponse.getName().isEmpty()){
                        detailsModel.setName(idDetectionResponse.getName());
                    }else {
                        detailsModel.setName(inputName.getText().toString());
                    }

                    if( idDetectionResponse.getOtherIdNumber() != null && !idDetectionResponse.getOtherIdNumber().isEmpty()){
                        detailsModel.setOtherIdNumber(idDetectionResponse.getOtherIdNumber());
                    }

                    alertDialog.dismiss();

                    if(idDetectionResponse.getIdType().equalsIgnoreCase(ApplicationConstants.ID_NIC_NEW)){

                        isNewID = true;
                       //showBarcodeScanner();
                    }
                }
            });


        } else if(idDetectionResponse.getResultcode().equalsIgnoreCase(ApplicationConstants.UNABLE_TO_FIND_ID_NUMBER_RESPONSE_CODE) ){

            detailsModel.setSessionId(idDetectionResponse.getSessionId());

            AlertDialog dialogNoData = ApplicationCommons.showAlertDialog(_Context,
                    ApplicationConstants.TITLE_CUSTOMER_DETAILS,
                    idDetectionResponse.getResult()+"\n"+ApplicationConstants.ADD_MISSING_DATA,
                    "OK",
                    null);

            dialogNoData.show();

            dialogNoData.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

                AlertDialog.Builder detailsDialogBuider = new AlertDialog.Builder(_Context);
                detailsDialogBuider.setTitle(ApplicationConstants.TITLE_CUSTOMER_DETAILS);

                View customDataLayout = getLayoutInflater().inflate(R.layout.layout_cutomer_info_add, null);
                detailsDialogBuider.setView(customDataLayout);

                final String[] selectedIdType = {ApplicationConstants.ID_NIC_OLD};

                MaterialSpinner spinnerIdType = customDataLayout.findViewById(R.id.spinnerIdType);
                spinnerIdType.setItems(ApplicationConstants.ID_DRIVING_LICENSE, ApplicationConstants.ID_NIC_OLD, ApplicationConstants.ID_NIC_NEW, ApplicationConstants.ID_PASSPORT);
                spinnerIdType.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> selectedIdType[0] = item);

                TextInputEditText inputNic = customDataLayout.findViewById(R.id.inputIdNumber);
                TextInputEditText inputName = customDataLayout.findViewById(R.id.inputName);

                detailsDialogBuider.setPositiveButton(ApplicationConstants.TEXT_DONE,(dialog1, which1) -> {


                });

                AlertDialog detailAlertDialog = detailsDialogBuider.create();
                detailAlertDialog.show();

                detailAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                    boolean isValid = true;

                    if(inputNic.getText().toString().isEmpty()){
                        inputNic.setError(ApplicationConstants.EMPTY_NIC_NUMBER_ERROR);
                        isValid = false;
                    }
                    if(inputName.getText().toString().isEmpty()){
                        inputName.setError(ApplicationConstants.EMPTY_NAME_ERROR);
                        isValid = false;
                    }

                    if(isValid){
                        detailsModel.setIdentityType(selectedIdType[0]);
                        detailsModel.setIdNumber(inputNic.getText().toString());
                        detailsModel.setName(inputName.getText().toString());

                        isDataValid = true;

                        detailAlertDialog.dismiss();
                    }
                });

                dialogNoData.dismiss();

            });
        } else if(idDetectionResponse.getResultcode()!=null){
            isDataValid = false;

            AlertDialog errorDialog = ApplicationCommons.showAlertDialog(_Context,
                    ApplicationConstants.TITLE_ERROR,
                    idDetectionResponse.getResult(),
                    "OK",
                    null);

            errorDialog.show();

            errorDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> errorDialog.dismiss());



        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != ApplicationConstants.requestPermissionID) {
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
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        resultURI = FileUtils.tempURI(_Context,ApplicationConstants.FILE_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultURI);

        Log.d("PATH",resultURI.getPath());

        startActivityForResult(intent, CAPTURE_IMAGE);


    }



    private void openEditor(Uri inputImage) {


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        try {
            SettingsList settingsList = new SettingsList();
            settingsList.getSettingsModel(EditorLoadSettings.class)
                .setImageSource(inputImage);

        new PhotoEditorBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, PESDK_RESULT);
        }catch (Exception e){
            Log.d("xxxxxxx",e.getLocalizedMessage());
            e.printStackTrace();
        }



        //SettingsList settingsList = FileUtils.createPesdkSettingsList();

        // Set input image
        /*settingsList.getSettingsModel(EditorLoadSettings.class)
                .setImageSource(inputImage);*/

        /*new PhotoEditorBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, PESDK_RESULT);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.d("PATH2",resultURI.getPath());

        if (requestCode == CAPTURE_IMAGE) {
            Log.d("PATH3",resultURI.getPath());
            if (resultCode == Activity.RESULT_OK) {
                Log.d("PATH4",resultURI.getPath());
                openEditor(resultURI);

                /*try {
                    if(resultURI != null){
                        Bitmap bitmap;
                        bitmap = CameraUtils.handleSamplingAndRotationBitmap(_Context,resultURI);
                        imgImage.setImageBitmap(bitmap);
                        activityCameraBinding.setIsImageCaptured(true);
                    }
                }catch (java.io.IOException e){
                    e.printStackTrace();

                    String oldURI = resultURI.getPath();

                    Log.d("xxxxx",oldURI);

                }*/


            }
        } else if(resultCode == RESULT_OK && requestCode == PESDK_RESULT){

            Uri resultUri = data.getParcelableExtra(ImgLyIntent.RESULT_IMAGE_URI);

            try {
                if(resultUri != null){
                    Bitmap bitmap;
                    resultURI = resultUri;
                    bitmap = CameraUtils.handleSamplingAndRotationBitmap(_Context,resultUri);
                    imgImage.setImageBitmap(bitmap);
                    activityCameraBinding.setIsImageCaptured(true);
                }
            }catch (Exception e){

            }

        }
    }

    /*public boolean checkRequestPermissions(){

    }*/



}
