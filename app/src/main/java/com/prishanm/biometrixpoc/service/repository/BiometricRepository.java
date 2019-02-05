package com.prishanm.biometrixpoc.service.repository;

import com.google.gson.Gson;
import com.prishanm.biometrixpoc.common.ApplicationCommons;
import com.prishanm.biometrixpoc.service.model.FaceDetectionRequest;
import com.prishanm.biometrixpoc.service.model.FaceDetectionResponse;
import com.prishanm.biometrixpoc.service.model.IdDetectionRequest;
import com.prishanm.biometrixpoc.service.model.IdDetectionResponse;

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
                //Log.e("BIOMETRIC ERROR", t.getLocalizedMessage());
                data.setValue(null);
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
                faceDetectionResponseData.setValue(null);
            }
        });

        return faceDetectionResponseData;
    }
}
