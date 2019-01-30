package com.prishanm.biometrixpoc.common;

import android.app.Activity;
import android.app.Application;

import com.prishanm.biometrixpoc.di.AppInjector;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by Prishan Maduka on 29,January,2019
 */
public class ApplicationController extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
