package com.example.rxjava_retrofit.adapters;

import android.graphics.Paint;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

/// could not make it work
public class TextViewBindingAdapter {
    @BindingAdapter("strikeThrough")
    public static void strikeThrough(TextView textView, Boolean strikeThrough) {
        if (strikeThrough) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}

