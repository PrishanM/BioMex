package com.prishanm.biometrixpoc.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import android.util.Log;

import com.prishanm.biometrixpoc.viewModel.CameraViewModel;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

/**
 * Created by Prishan Maduka on 29,January,2019
 */
public class ApplicationViewModelFactory implements ViewModelProvider.Factory {

    private final ArrayMap<Class, Callable<? extends ViewModel>> creators;

    @Inject
    public ApplicationViewModelFactory(final ViewModelSubComponent viewModelSubComponent) {

        creators = new ArrayMap<>();

        creators.put(CameraViewModel.class, viewModelSubComponent::cameraViewModel);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Callable<?extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class, Callable<? extends ViewModel>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("Unknown model class " + modelClass);
        }
        try {
            return (T) creator.call();
        } catch (Exception e) {
            Log.d("ERROR",e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
}
