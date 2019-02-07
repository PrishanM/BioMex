package com.prishanm.biometrixpoc.di;

import com.prishanm.biometrixpoc.viewModel.CameraStepThreeViewModel;
import com.prishanm.biometrixpoc.viewModel.CameraStepTwoViewModel;
import com.prishanm.biometrixpoc.viewModel.CameraViewModel;

import dagger.Subcomponent;

/**
 * Created by Prishan Maduka on 29,January,2019
 */

@Subcomponent
public interface ViewModelSubComponent {

    @Subcomponent.Builder
    interface Builder{
        ViewModelSubComponent build();
    }

    CameraViewModel cameraViewModel();

    CameraStepTwoViewModel cameraStepTwoViewModel();

    CameraStepThreeViewModel cameraStepThreeViewModel();
}
