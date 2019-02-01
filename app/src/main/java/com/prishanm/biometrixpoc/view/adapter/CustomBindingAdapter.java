package com.prishanm.biometrixpoc.view.adapter;

import android.view.View;

import androidx.databinding.BindingAdapter;

/**
 * Created by Prishan Maduka on 31,January,2019
 */
public class CustomBindingAdapter {

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
