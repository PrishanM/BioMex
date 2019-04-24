package com.prishanm.biometrixpoc.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private static File getTempCreatedFile(int fileType){

        String imageFileName = "";
        String extension = "";

        if(fileType == ApplicationConstants.FILE_TYPE_IMAGE){
            imageFileName = ApplicationConstants.IMAGE_PREFIX + System.currentTimeMillis();
            extension = ApplicationConstants.IMAGE_SUFIX;
        } else {
            imageFileName = ApplicationConstants.VIDEO_PREFIX + System.currentTimeMillis();
            extension = ApplicationConstants.VIDEO_SUFIX;
        }

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME);
        storageDir.mkdirs();
        File file = null;
        try {
            file = File.createTempFile(imageFileName, extension, storageDir);
            //file = new File(storageDir.getPath()+File.separator+imageFileName+extension);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;

        /*File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

        File file = new File(storageDir.getAbsolutePath()+"/"+imageFileName+extension);*/

    }

    public static Uri tempURI(Context context, int fileType){

        File tempFile = getTempCreatedFile(fileType);

        return getUriFromFile(context,tempFile);

    }


    private static Uri getUriFromFile(Context context, File file){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return FileProvider.getUriForFile(context,
                    ApplicationConstants.APPLICATION_FILE_PROVIDER,
                    file);

        } else {

            return Uri.fromFile(file);

        }

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

    public static Bitmap mergeBitmaps(ArrayList<Bitmap> bitmaps){

        Bitmap result = Bitmap.createBitmap(bitmaps.get(0).getWidth() * 2, bitmaps.get(0).getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < bitmaps.size(); i++) {
            canvas.drawBitmap(bitmaps.get(i), bitmaps.get(i).getWidth() * (i % 2), bitmaps.get(i).getHeight() * (i / 2), paint);
        }
        return result;
    }

    public static File saveBitmap(Bitmap bitmap){

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME);
        storageDir.mkdirs();
        File file = null;
        try {
            file = File.createTempFile("MEGR_"+ System.currentTimeMillis(), ".jpg", storageDir);
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}
