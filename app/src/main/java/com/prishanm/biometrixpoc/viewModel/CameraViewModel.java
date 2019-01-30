package com.prishanm.biometrixpoc.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import javax.inject.Inject;

/**
 * Created by Prishan Maduka on 29,January,2019
 */
public class CameraViewModel extends AndroidViewModel {

    @Inject
    public CameraViewModel(@NonNull Application application) {
        super(application);
    }
}
