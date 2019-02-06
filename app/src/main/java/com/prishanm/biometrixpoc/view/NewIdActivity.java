package com.prishanm.biometrixpoc.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.gson.Gson;
import com.prishanm.biometrixpoc.R;
import com.prishanm.biometrixpoc.common.ApplicationCommons;
import com.prishanm.biometrixpoc.common.ApplicationConstants;
import com.prishanm.biometrixpoc.service.parcelable.CustomerDetailsModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prishan Maduka on 01,February,2019
 */
public class NewIdActivity extends AppCompatActivity {

    private CodeScanner codeScanner;
    private CodeScannerView codeScannerView;

    /*@BindView(R.id.btnNext)
    ImageButton btnNext;*/

    private Context _Context;
    private boolean isValid = false;
    private String customerDetailsGson;
    private CustomerDetailsModel customerDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_barcode_camera);

        _Context = this;

        ButterKnife.bind(this);

        customerDetailsGson = getIntent().getStringExtra(ApplicationConstants.TAG_INTENT_CUSTOMER_DATA);
        if(customerDetailsGson != null){
            Gson gson = new Gson();
            customerDetails = gson.fromJson(customerDetailsGson, CustomerDetailsModel.class);

        }

        codeScannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(_Context, codeScannerView);
        codeScanner.setAutoFocusEnabled(false);
        codeScanner.setTouchFocusEnabled(true);

        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {

            String lines[] = result.getText().split("\\r?\\n");

            String nicNumber = lines[1];
            String fullName = lines[6];

            codeScanner.stopPreview();
            AlertDialog resultDialog = ApplicationCommons.showAlertDialog(_Context,
                    ApplicationConstants.TITLE_CUSTOMER_DETAILS,
                    result.getText(),
                    ApplicationConstants.TEXT_CORRECT,
                    ApplicationConstants.TEXT_WRONG);

            resultDialog.show();

            resultDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Following part is commented in DEV mode
                    /*if(nicNumber.equalsIgnoreCase(customerDetails.getIdNumber()) && fullName.equalsIgnoreCase(customerDetails.getName())){
                        Toast.makeText(_Context,"Successful",Toast.LENGTH_LONG).show();
                        isValid = true;
                    }*/

                    if(nicNumber.equalsIgnoreCase(customerDetails.getIdNumber())){
                        isValid = true;
                        resultDialog.dismiss();

                        Intent intent = new Intent(NewIdActivity.this,CameraStepTwoActivity.class);
                        intent.putExtra(ApplicationConstants.TAG_INTENT_CUSTOMER_DATA,customerDetailsGson);
                        startActivity(intent);
                        finish();

                    }else {
                        isValid = false;
                        AlertDialog errorDialog = ApplicationCommons.showAlertDialog(_Context,
                                ApplicationConstants.TITLE_ERROR,
                                ApplicationConstants.DATA_MISMATCH,
                                "OK",
                                null);

                        errorDialog.show();
                    }

                }
            });

            resultDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isValid = false;
                }
            });


        }));

        //codeScanner.startPreview();

    }

    @OnClick({R.id.scanner_view})
    public void onButtonsClick(View view){

        //if(view.getId() == R.id.scanner_view){
            codeScanner.startPreview();
        /*} else if(view.getId() == R.id.btnNext){
            if(isValid){
                Intent intent = new Intent(NewIdActivity.this,CameraStepTwoActivity.class);
                intent.putExtra(ApplicationConstants.TAG_INTENT_CUSTOMER_DATA,customerDetailsGson);
                startActivity(intent);
                finish();
            } else {
                AlertDialog errorDialog = ApplicationCommons.showAlertDialog(_Context,
                        ApplicationConstants.TITLE_ERROR,
                        ApplicationConstants.DATA_MISMATCH,
                        "OK",
                        null);

                errorDialog.show();
            }
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();

    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}
