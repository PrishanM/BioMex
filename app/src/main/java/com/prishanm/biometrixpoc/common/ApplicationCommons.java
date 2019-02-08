package com.prishanm.biometrixpoc.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.List;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Prishan Maduka on 29,January,2019
 */
public class ApplicationCommons {

    public static void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void processTextBlock(FirebaseVisionText result) {
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

    public static ProgressDialog showProgressDialog(Context context, String message, int style){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(style);

        return progressDialog;
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message, String positiveButtonText, String negativeButtonText){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);

        dialogBuilder.setMessage(message);

        dialogBuilder.setPositiveButton(positiveButtonText, (dialog, which) -> {

        });

        if(negativeButtonText!= null && !negativeButtonText.isEmpty()){

            dialogBuilder.setNegativeButton(negativeButtonText, (dialog, which) -> {

            });
        }

        AlertDialog alertDialog = dialogBuilder.create();

        return alertDialog;

    }

}
