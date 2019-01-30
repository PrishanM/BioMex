package com.prishanm.biometrixpoc.di;

import androidx.lifecycle.ViewModelProvider;

import com.prishanm.biometrixpoc.service.repository.BiometricService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Prishan Maduka on 29,January,2019
 */


@Module(subcomponents=ViewModelSubComponent.class)
public class AppModule {

    @Singleton
    @Provides
    BiometricService biometricService(){
        return new Retrofit.Builder()
                .baseUrl(BiometricService.HTTPS_API_GITHUB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BiometricService.class);
    }


    @Singleton
    @Provides
    ViewModelProvider.Factory factory(ViewModelSubComponent.Builder builder){

        return new ApplicationViewModelFactory(builder.build());
    }
}
