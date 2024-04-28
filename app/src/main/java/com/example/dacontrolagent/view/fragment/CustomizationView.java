package com.example.dacontrolagent.view.fragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.Collection;

public class CustomizationView {

    public final View viewToCustomize;
    private ViewGroup.LayoutParams layoutParams;

    public CustomizationView(View viewToCustomize) {
        this.viewToCustomize = viewToCustomize;
        this.layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
    }

    public static CustomizationView of(View viewToCustomize) {
        return new CustomizationView(viewToCustomize);
    }

    public CustomizationView width(int width) {
        layoutParams.width = width;
        viewToCustomize.setLayoutParams(layoutParams);
        return this;
    }

    public CustomizationView height(int height) {
        layoutParams.height = height;
        viewToCustomize.setLayoutParams(layoutParams);
        return this;
    }

    public CustomizationView padding(int left, int top, int right, int bottom) {
        viewToCustomize.setPadding(left, top, right, bottom);
        return this;
    }

    public CustomizationView orientation(int orientation) {
        ((LinearLayout)viewToCustomize).setOrientation(orientation);
        return this;
    }

    public CustomizationView text(String text) {
        ((TextView)viewToCustomize).setText(text);
        return this;
    }

    public CustomizationView text(int text) {
        ((TextView)viewToCustomize).setText(text);
        return this;
    }

    public CustomizationView font(Context context, int font) {
        Typeface typeface = ResourcesCompat.getFont(context, font);
        ((TextView)viewToCustomize).setTypeface(typeface);
        return this;
    }

    public CustomizationView size(int size) {
        ((TextView)viewToCustomize).setTextSize(size);
        return this;
    }

    public void withChild(View ...viewsToAdd) {
        for(View view : viewsToAdd) {
            ((ViewGroup)viewToCustomize).addView(view);
        }
    }

    public CustomizationView margins(int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(layoutParams.width, layoutParams.height);
        relativeLayoutParams.setMargins(left, top, right, bottom);
        viewToCustomize.setLayoutParams(relativeLayoutParams);
        return this;
    }

    public CustomizationView background(int background) {
        viewToCustomize.setBackgroundResource(background);
        return this;
    }
}
