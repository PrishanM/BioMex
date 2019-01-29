package com.prishanm.biometrixpoc;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Prishan Maduka on 28,January,2019
 */
public class FileUtils {

    public static final String FOLDER_NAME = "Biomex";

    public static File createFolders(){

        File baseDir;

        baseDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (baseDir == null)
            return Environment.getExternalStorageDirectory();
        File aviaryFolder = new File(baseDir, FOLDER_NAME);
        if (aviaryFolder.exists())
            return aviaryFolder;
        if (aviaryFolder.isFile())
            aviaryFolder.delete();
        if (aviaryFolder.mkdirs())
            return aviaryFolder;
        return Environment.getExternalStorageDirectory();
    }

    public static File genEditFile(){
        return FileUtils.getEmptyFile("IMG_"
                + System.currentTimeMillis() + ".png");
    }

    public static File getEmptyFile(String name) {
        File folder = FileUtils.createFolders();
        if (folder != null) {
            if (folder.exists()) {
                File file = new File(folder, name);
                return file;
            }
        }
        return null;
    }

    public static boolean deleteFileNoThrow(String path) {
        File file;
        try {
            file = new File(path);
        } catch (NullPointerException e) {
            return false;
        }

        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static String saveBitmap(String bitName, Bitmap mBitmap) {
        File baseFolder = createFolders();
        File f = new File(baseFolder.getAbsolutePath(), bitName);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    public static Uri getOutputImageUri (Context context){

        Uri outputImgUri = null;
        try {

            // Create a random image file name.
            String imageFileName = "IMG_" + System.currentTimeMillis() + ".jpg";

            // Construct a output file to save camera taken picture temporary.
            File outputImageFile = new File(context.getExternalCacheDir(), imageFileName);

            // If cached temporary file exist then delete it.
            if (outputImageFile.exists()) {
                outputImageFile.delete();
            }

            // Create a new temporary file.
            outputImageFile.createNewFile();

            // Get the output image file Uri wrapper object.
            outputImgUri = CameraUtils.getImageFileUriByOsVersion(outputImageFile,context);

        }catch(IOException ex)
        {
            Log.e("ERROR", ex.getMessage(), ex);
        }

        return outputImgUri;
    }

    public static boolean copyFileFromUri(Uri sourceFileUri, Uri destinationFileUri)
    {
        String sourcePath = sourceFileUri.getPath();
        File sourceFile = new File(sourcePath);

        String destinationPath = destinationFileUri.getPath();
        File destinationFile = new File(destinationPath);

        try
        {
            if(destinationFile.exists()){
                destinationFile.delete();
            }
            org.apache.commons.io.FileUtils.moveFile(sourceFile,destinationFile);

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();

            return false;
        }

    }
}
