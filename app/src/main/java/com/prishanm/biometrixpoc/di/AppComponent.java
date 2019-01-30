package com.prishanm.biometrixpoc.di;

import android.app.Application;

import com.prishanm.biometrixpoc.common.ApplicationController;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Prishan Maduka on 29,January,2019
 */

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        MainActivityModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
    void inject(ApplicationController mvvmApplication);
}
