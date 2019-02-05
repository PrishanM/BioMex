package com.prishanm.biometrixpoc.di;

import com.prishanm.biometrixpoc.service.repository.BiometricService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModelProvider;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Prishan Maduka on 29,January,2019
 */


@Module(subcomponents=ViewModelSubComponent.class)
public class AppModule {

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    @Singleton
    @Provides
    BiometricService biometricService(){
        return new Retrofit.Builder()
                .baseUrl(BiometricService.HTTPS_API_BIOMEX_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(BiometricService.class);
    }


    @Singleton
    @Provides
    ViewModelProvider.Factory factory(ViewModelSubComponent.Builder builder){

        return new ApplicationViewModelFactory(builder.build());
    }
}
