package com.prishanm.biometrixpoc.common;

/**
 * Created by Prishan Maduka on 05,February,2019
 */
public class ApplicationConstants {

    public static final int requestPermissionID = 101;
    public static final int PESDK_RESULT = 1;

    //API Response Codes
    public static final String SUCCESS_RESPONSE_CODE = "00";
    public static final String UNABLE_TO_FIND_ID_NUMBER_RESPONSE_CODE = "12";
    public static final String NETWORK_FAILURE_RESPONSE_CODE = "1000";

    //Capture Image & Video Request Code
    public static final int CAPTURE_IMAGE = 1000;
    public static final int CAPTURE_VIDEO = 1010;

    public static final int VIDEO_DURATION = 5;
    public static final int FILE_TYPE_IMAGE = 1;
    public static final int FILE_TYPE_VIDEO = 2;
    public static final String IMAGE_PREFIX = "IMG_";
    public static final String VIDEO_PREFIX = "VID_";
    public static final String IMAGE_SUFIX = ".jpg";
    public static final String VIDEO_SUFIX = ".mp4";

    public static final String FOLDER_NAME = "Biomex";

    //File provider
    public static final String APPLICATION_FILE_PROVIDER = "com.prishanm.biometrixpoc.fileprovider";

    //ID Types
    public static final String ID_DRIVING_LICENSE = "DRIVING LICENCE";
    public static final String ID_NIC_NEW = "NATIONAL IDENTITY CARD NEW";
    public static final String ID_NIC_OLD = "NATIONAL IDENTITY CARD OLD";
    public static final String ID_PASSPORT = "PASSPORT";

    /** Random Action IDS **/
    public static final String ACTION_ID_1 = "Change the Pan Angle of the Face";
    public static final String ACTION_ID_2 = "Change the Roll Angle of the Face";
    public static final String ACTION_ID_3 = "Change the Tilt Angle of the Face";
    public static final String ACTION_ID_4 = "Change the smile of the Face";
    public static final String ACTION_ID_5 = "Change the Pan Angle and Roll Angle of the Face";
    public static final String ACTION_ID_6 = "Change the Pan Angle and Tilt Angle of the Face";
    public static final String ACTION_ID_7 = "Change the Pan Angle and the Smile of the Face";
    public static final String ACTION_ID_8 = "Change the Roll Angle and Tilt Angle of the Face";
    public static final String ACTION_ID_9 = "Change the Roll Angle and the Smile of the Face";
    public static final String ACTION_ID_10 = "Change the Tilt Angle and the Smile of the Face";



    //Text Constants and Messages
    public static final String TAG_INTENT_CUSTOMER_DATA = "CUSTOMER_DATA";
    public static final String TAG_INTENT_ACTION_ID = "ACTION_ID";
    public static final String TITLE_CUSTOMER_DETAILS = "Customer Details";
    public static final String TITLE_SUCCESSFUL = "Successful";
    public static final String TITLE_CONGRATULATIONS = "Congratulations";
    public static final String TITLE_RANDOM_ACTION = "Random Action";
    public static final String TEXT_CORRECT = "Correct";
    public static final String TEXT_WRONG = "Wrong";
    public static final String TEXT_DONE = "DONE";
    public static final String TEXT_VALIDATING = "Validating ...";
    public static final String ADD_MISSING_DATA = "Please add data manually.";

    //Success Messages
    public static final String TEXT_SUCCESSFULLY_FACE_VERIFIED = "Your face is successfully verified with the ID";
    public static final String TEXT_PROCEED_FINAL_STEP = "Proceed the final step";

    /** ERROR MESSAGES **/
    public static final String DATA_MISMATCH = "Scaned data doesn't match with the ID details.";
    public static final String TITLE_ERROR = "Error";
    public static final String CAPTURE_IMAGE_VALIDATE_ERROR = "Capture the image to validate.";
    public static final String CAPTURE_SELFIE_VALIDATE_ERROR = "Capture the selfie to validate.";
    public static final String CUSTOMER_DATA_MISSING_ERROR = "Some data are missing! Please check and proceed again.";
    public static final String EMPTY_NIC_NUMBER_ERROR = "NIC number cannot be empty.";
    public static final String EMPTY_NAME_ERROR = "Name cannot be empty.";
    public static final String NETWORK_ERROR = "Please check your network and try later";

}
