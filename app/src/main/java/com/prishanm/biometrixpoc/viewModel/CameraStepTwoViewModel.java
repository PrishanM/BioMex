package com.prishanm.biometrixpoc.viewModel;

import android.app.Application;

import com.prishanm.biometrixpoc.service.repository.BiometricRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * Created by Prishan Maduka on 05,February,2019
 */
public class CameraStepTwoViewModel extends AndroidViewModel {

    private BiometricRepository repository;

    @Inject
    public CameraStepTwoViewModel(@NonNull Application application, BiometricRepository repository) {
        super(application);
        this.repository = repository;
    }
}
