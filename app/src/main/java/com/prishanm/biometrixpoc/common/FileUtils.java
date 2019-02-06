package com.prishanm.biometrixpoc.common;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

import static com.prishanm.biometrixpoc.common.ApplicationMessages.FOLDER_NAME;

/**
 * Created by Prishan Maduka on 28,January,2019
 */
public class FileUtils {

    public static File getTempCreatedFile(){

        final String imageFileName = "IMG_" + System.currentTimeMillis();
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME);
        storageDir.mkdirs();
        File file = null;
        try {
            file = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;

    }
}
