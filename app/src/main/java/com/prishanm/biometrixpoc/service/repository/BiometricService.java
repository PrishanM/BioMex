package com.prishanm.biometrixpoc.service.repository;

import com.prishanm.biometrixpoc.service.model.IdDetectionRequest;
import com.prishanm.biometrixpoc.service.model.IdDetectionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Prishan Maduka on 29,January,2019
 */
public interface BiometricService {

    String HTTPS_API_GITHUB_URL = "http://13.58.177.44/";

    @POST("/biomex.php/id_detection")
    Call<IdDetectionResponse> checkIdDetection(@Body IdDetectionRequest request);
}
