package com.prishanm.biometrixpoc.viewModel;

import android.app.Application;

import com.prishanm.biometrixpoc.service.model.IdDetectionRequest;
import com.prishanm.biometrixpoc.service.model.IdDetectionResponse;
import com.prishanm.biometrixpoc.service.repository.BiometricRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by Prishan Maduka on 29,January,2019
 */
public class CameraViewModel extends AndroidViewModel {

    private BiometricRepository repository;
    private final MutableLiveData<String> encodedImage;

    private LiveData<IdDetectionResponse> idDetectionResponseObservable;
    //public ObservableField<IdDetectionResponse> detectionObservableField = new ObservableField<>();

    @Inject
    public CameraViewModel(@NonNull Application application, BiometricRepository repository) {
        super(application);
        this.repository = repository;
        encodedImage = new MutableLiveData<>();
    }

    public LiveData<IdDetectionResponse> getIdDetectionResponseObservable(){
        return idDetectionResponseObservable;
    }

    public LiveData<IdDetectionResponse> checkIdValidity(String base64String){
        this.encodedImage.setValue(base64String);
        IdDetectionRequest detectionRequest = new IdDetectionRequest();
        detectionRequest.setImage(encodedImage.getValue());
        idDetectionResponseObservable = repository.checkIdStatus(detectionRequest);

        return idDetectionResponseObservable;

        //Log.d("TEST",base64String);
    }


}
