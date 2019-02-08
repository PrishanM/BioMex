package com.prishanm.biometrixpoc.viewModel;

import android.app.Application;

import com.prishanm.biometrixpoc.service.model.LivenessDetectionRequest;
import com.prishanm.biometrixpoc.service.model.LivenessDetectionResponse;
import com.prishanm.biometrixpoc.service.repository.BiometricRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * Created by Prishan Maduka on 07,February,2019
 */
public class CameraStepThreeViewModel extends AndroidViewModel {

    private BiometricRepository repository;
    private LiveData<LivenessDetectionResponse> livenessDetectionResponseLiveData;

    @Inject
    public CameraStepThreeViewModel(@NonNull Application application, BiometricRepository repository) {

        super(application);
        this.repository = repository;
    }

    public LiveData<LivenessDetectionResponse> getLivenessDetectionObservable(){
        return livenessDetectionResponseLiveData;
    }

    public LiveData<LivenessDetectionResponse> checkLivenessDetection(LivenessDetectionRequest request){

        livenessDetectionResponseLiveData = repository.checkLivenessDetection(request);

        return livenessDetectionResponseLiveData;
    }
}
