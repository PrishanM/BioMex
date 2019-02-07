package com.prishanm.biometrixpoc.viewModel;

import android.app.Application;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * Created by Prishan Maduka on 07,February,2019
 */
public class CameraStepThreeViewModel extends AndroidViewModel {

    @Inject
    public CameraStepThreeViewModel(@NonNull Application application) {
        super(application);
    }
}
