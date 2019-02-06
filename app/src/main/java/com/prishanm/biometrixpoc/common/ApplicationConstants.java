package com.prishanm.biometrixpoc.common;

/**
 * Created by Prishan Maduka on 05,February,2019
 */
public class ApplicationConstants {

    public static final int requestPermissionID = 101;
    public static int PESDK_RESULT = 1;

    //API Response Codes
    public static final String SUCCESS_RESPONSE_CODE = "00";
    public static final String UNABLE_TO_FIND_ID_NUMBER_RESPONSE_CODE = "12";

    //Capture Image Request Code
    public static final int CAPTURE_IMAGE = 1000;

    public static final String FOLDER_NAME = "Biomex";

    //File provider
    public static final String APPLICATION_FILE_PROVIDER = "com.prishanm.biometrixpoc.fileprovider";

    //ID Types
    public static final String ID_DRIVING_LICENSE = "DRIVING LICENCE";
    public static final String ID_NIC_NEW = "NATIONAL IDENTITY CARD NEW";
    public static final String ID_NIC_OLD = "NATIONAL IDENTITY CARD OLD";
    public static final String ID_PASSPORT = "PASSPORT";

    //Text Constants and Messages
    public static final String TAG_INTENT_CUSTOMER_DATA = "CUSTOMER_DATA";
    public static final String TITLE_CUSTOMER_DETAILS = "Customer Details";
    public static final String TEXT_CORRECT = "Correct";
    public static final String TEXT_WRONG = "Wrong";
    public static final String TEXT_DONE = "DONE";
    public static final String TEXT_VALIDATING = "Validating ...";
    public static final String ADD_MISSING_DATA = "Please add data manually.";

    /** ERROR MESSAGES **/
    public static final String DATA_MISMATCH = "Scaned data doesn't match with the ID details.";
    public static final String TITLE_ERROR = "Error";
    public static final String CAPTURE_IMAGE_VALIDATE_ERROR = "Capture the image to validate.";
    public static final String CAPTURE_SELFIE_VALIDATE_ERROR = "Capture the selfie to validate.";
    public static final String CUSTOMER_DATA_MISSING_ERROR = "Some data are missing! Please check and proceed again.";
    public static final String EMPTY_NIC_NUMBER_ERROR = "NIC number cannot be empty.";
    public static final String EMPTY_NAME_ERROR = "Name cannot be empty.";

}
