package com.prishanm.biometrixpoc.viewModel;

import android.app.Application;

import com.prishanm.biometrixpoc.service.model.FaceDetectionRequest;
import com.prishanm.biometrixpoc.service.model.FaceDetectionResponse;
import com.prishanm.biometrixpoc.service.model.LiveActionIdResponse;
import com.prishanm.biometrixpoc.service.repository.BiometricRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * Created by Prishan Maduka on 05,February,2019
 */
public class CameraStepTwoViewModel extends AndroidViewModel {

    private BiometricRepository repository;
    private LiveData<FaceDetectionResponse> faceDetectionResponseLiveData;
    private LiveData<LiveActionIdResponse> randomLiveActionIdResponse;

    @Inject
    public CameraStepTwoViewModel(@NonNull Application application, BiometricRepository repository) {
        super(application);
        this.repository = repository;
    }

    public LiveData<FaceDetectionResponse> getFaceDetectionResponseObservable(){
        return faceDetectionResponseLiveData;
    }

    public LiveData<LiveActionIdResponse> getRandomLiveActionIdObservable(){
        return randomLiveActionIdResponse;
    }

    public LiveData<FaceDetectionResponse> checkMatchingFace(FaceDetectionRequest request) {

        faceDetectionResponseLiveData = repository.checkMatchingFace(request);

        return faceDetectionResponseLiveData;
    }

    public LiveData<LiveActionIdResponse> getRandomLiveAction(String sessionId){

        randomLiveActionIdResponse = repository.getLiveActionId(sessionId);

        return randomLiveActionIdResponse;

    }
}
