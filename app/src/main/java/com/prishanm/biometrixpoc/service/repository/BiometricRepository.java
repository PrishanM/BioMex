package com.prishanm.biometrixpoc.service.repository;

import com.google.gson.Gson;
import com.prishanm.biometrixpoc.common.ApplicationCommons;
import com.prishanm.biometrixpoc.common.ApplicationConstants;
import com.prishanm.biometrixpoc.service.model.FaceDetectionRequest;
import com.prishanm.biometrixpoc.service.model.FaceDetectionResponse;
import com.prishanm.biometrixpoc.service.model.IdDetectionRequest;
import com.prishanm.biometrixpoc.service.model.IdDetectionResponse;
import com.prishanm.biometrixpoc.service.model.LiveActionIdResponse;
import com.prishanm.biometrixpoc.service.model.LivenessDetectionRequest;
import com.prishanm.biometrixpoc.service.model.LivenessDetectionResponse;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prishan Maduka on 29,January,2019
 */
public class BiometricRepository {

    private BiometricService biometricService;

    @Inject
    public BiometricRepository(BiometricService biometricService) {
        this.biometricService = biometricService;
    }

    public LiveData<IdDetectionResponse> checkIdStatus(IdDetectionRequest request){
        final MutableLiveData<IdDetectionResponse> data = new MutableLiveData<>();

        biometricService.checkIdDetection(request).enqueue(new Callback<IdDetectionResponse>() {
            @Override
            public void onResponse(Call<IdDetectionResponse> call, Response<IdDetectionResponse> response) {

                ApplicationCommons.simulateDelay();
                if( response.code() == 200 ){
                    data.setValue(response.body());
                } else{
                    Gson gson = new Gson();
                    IdDetectionResponse sampleResponse = gson.fromJson(response.errorBody().charStream(),IdDetectionResponse.class);
                    data.setValue(sampleResponse);
                }
                /*if( response.code() == 200 ){
                    data.setValue(response.body());
                } else if( response.code() == 404 ){

                } else if( response.code() == 405 ){

                } else if( response.code() == 400 ){
                    Gson gson = new Gson();
                    IdDetection sampleResponse = gson.fromJson(response.errorBody().charStream(),IdDetection.class);
                    data.setValue(sampleResponse);
                    //data.setValue(response.errorBody());
                } else if( response.code() == 405 ){

                }*/
            }

            @Override
            public void onFailure(Call<IdDetectionResponse> call, Throwable t) {

                IdDetectionResponse detectionResponse = new IdDetectionResponse();
                detectionResponse.setResultcode(ApplicationConstants.NETWORK_FAILURE_RESPONSE_CODE);
                detectionResponse.setResult(ApplicationConstants.NETWORK_ERROR);
                data.setValue(detectionResponse);
            }
        });

        return data;
    }

    public LiveData<FaceDetectionResponse> checkMatchingFace(FaceDetectionRequest request){

        final MutableLiveData<FaceDetectionResponse> faceDetectionResponseData = new MutableLiveData<>();

        biometricService.checkMatchingFace(request).enqueue(new Callback<FaceDetectionResponse>() {

            @Override
            public void onResponse(Call<FaceDetectionResponse> call, Response<FaceDetectionResponse> response) {

                ApplicationCommons.simulateDelay();
                if(response.code() == 200){
                    faceDetectionResponseData.setValue(response.body());
                } else {
                    Gson gson = new Gson();
                    FaceDetectionResponse sampleResponse = gson.fromJson(response.errorBody().charStream(),FaceDetectionResponse.class);
                    faceDetectionResponseData.setValue(sampleResponse);
                }
            }

            @Override
            public void onFailure(Call<FaceDetectionResponse> call, Throwable t) {

                FaceDetectionResponse faceDetectionResponse = new FaceDetectionResponse();
                faceDetectionResponse.setResultcode(ApplicationConstants.NETWORK_FAILURE_RESPONSE_CODE);
                faceDetectionResponse.setResult(ApplicationConstants.NETWORK_ERROR);
                faceDetectionResponseData.setValue(faceDetectionResponse);
            }
        });

        return faceDetectionResponseData;
    }

    public LiveData<LiveActionIdResponse> getLiveActionId(String sessionId){

        final MutableLiveData<LiveActionIdResponse> mutableLiveData = new MutableLiveData<>();

        biometricService.getRandomAction(sessionId).enqueue(new Callback<LiveActionIdResponse>() {
            @Override
            public void onResponse(Call<LiveActionIdResponse> call, Response<LiveActionIdResponse> response) {

                ApplicationCommons.simulateDelay();

                if(response.code() == 200){

                    mutableLiveData.setValue(response.body());

                } else {

                    Gson gson = new Gson();
                    LiveActionIdResponse liveActionIdResponse = gson.fromJson(response.errorBody().charStream(),
                            LiveActionIdResponse.class);
                    mutableLiveData.setValue(liveActionIdResponse);

                }
            }

            @Override
            public void onFailure(Call<LiveActionIdResponse> call, Throwable t) {
                LiveActionIdResponse response = new LiveActionIdResponse();
                response.setResultcode(ApplicationConstants.NETWORK_FAILURE_RESPONSE_CODE);
                response.setResult(ApplicationConstants.NETWORK_ERROR);
                mutableLiveData.setValue(response);
            }
        });

        return mutableLiveData;
    }

    public LiveData<LivenessDetectionResponse> checkLivenessDetection(LivenessDetectionRequest request){

        final MutableLiveData<LivenessDetectionResponse> mutableLiveData = new MutableLiveData<>();

        biometricService.checkLivenesDetection(request).enqueue(new Callback<LivenessDetectionResponse>() {
            @Override
            public void onResponse(Call<LivenessDetectionResponse> call, Response<LivenessDetectionResponse> response) {

                ApplicationCommons.simulateDelay();

                if(response.code() == 200){
                    mutableLiveData.setValue(response.body());
                } else {
                    Gson gson = new Gson();
                    LivenessDetectionResponse liveActionIdResponse = gson.fromJson(response.errorBody().charStream(),
                            LivenessDetectionResponse.class);
                    mutableLiveData.setValue(liveActionIdResponse);
                }
            }

            @Override
            public void onFailure(Call<LivenessDetectionResponse> call, Throwable t) {

                LivenessDetectionResponse liveActionIdResponse = new LivenessDetectionResponse();
                liveActionIdResponse.setResultcode(ApplicationConstants.NETWORK_FAILURE_RESPONSE_CODE);
                liveActionIdResponse.setResult(ApplicationConstants.NETWORK_ERROR);
                mutableLiveData.setValue(liveActionIdResponse);
            }
        });

        return mutableLiveData;
    }
}
