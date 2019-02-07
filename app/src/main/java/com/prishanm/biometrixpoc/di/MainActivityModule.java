package com.prishanm.biometrixpoc.di;

import com.prishanm.biometrixpoc.view.CameraActivity;
import com.prishanm.biometrixpoc.view.CameraStepThreeActivity;
import com.prishanm.biometrixpoc.view.CameraStepTwoActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Prishan Maduka on 29,January,2019
 */

@Module
public abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract CameraActivity contributeProjectDetailActivity();

    @ContributesAndroidInjector
    abstract CameraStepTwoActivity contributeCameraStepTwoActivity();

    @ContributesAndroidInjector
    abstract CameraStepThreeActivity contributeCameraStepThreeActivity();
}
