package com.prishanm.biometrixpoc.service.repository;

import com.prishanm.biometrixpoc.service.model.FaceDetectionRequest;
import com.prishanm.biometrixpoc.service.model.FaceDetectionResponse;
import com.prishanm.biometrixpoc.service.model.IdDetectionRequest;
import com.prishanm.biometrixpoc.service.model.IdDetectionResponse;
import com.prishanm.biometrixpoc.service.model.LiveActionIdResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Prishan Maduka on 29,January,2019
 */
public interface BiometricService {

    String HTTPS_API_BIOMEX_URL = "http://13.58.177.44/";

    @POST("/biomex.php/id_detection")
    Call<IdDetectionResponse> checkIdDetection(@Body IdDetectionRequest request);

    @POST("/biomex.php/matching_faces")
    Call<FaceDetectionResponse> checkMatchingFace(@Body FaceDetectionRequest request);

    @GET("/biomex.php/liveness_detection/{sessionId}")
    Call<LiveActionIdResponse> getRandomAction(@Path("sessionId") String sessionId);
}
