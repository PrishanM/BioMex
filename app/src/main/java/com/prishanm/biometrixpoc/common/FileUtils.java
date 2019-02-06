package com.prishanm.biometrixpoc.common;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.FileProvider;
import ly.img.android.pesdk.assets.filter.basic.FilterPackBasic;
import ly.img.android.pesdk.backend.model.constant.Directory;
import ly.img.android.pesdk.backend.model.state.EditorSaveSettings;
import ly.img.android.pesdk.backend.model.state.manager.SettingsList;
import ly.img.android.pesdk.ui.model.state.UiConfigFilter;

import static com.prishanm.biometrixpoc.common.ApplicationConstants.FOLDER_NAME;

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

    public static Uri tempURI(Context context){
        Uri resultURI = null;

        File tempFile = getTempCreatedFile();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            resultURI = FileProvider.getUriForFile(context,
                    ApplicationConstants.APPLICATION_FILE_PROVIDER,
                    tempFile);
        } else {
            resultURI = Uri.fromFile(tempFile);
        }

        return resultURI;
    }

    public static SettingsList createPesdkSettingsList() {

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
                .setExportDir(Directory.DCIM, FOLDER_NAME)
                .setExportPrefix("IMGR_"+timeStamp)
                .setSavePolicy(EditorSaveSettings.SavePolicy.RETURN_ALWAYS_ONLY_OUTPUT);

        return settingsList;
    }
}
