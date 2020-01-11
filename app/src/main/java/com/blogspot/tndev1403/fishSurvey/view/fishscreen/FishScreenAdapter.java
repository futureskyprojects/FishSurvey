package com.blogspot.tndev1403.fishSurvey.view.fishscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blogspot.tndev1403.fishSurvey.model.Fish;
import com.blogspot.tndev1403.fishSurvey.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Same in home screen presenter coding.
 */

public class FishScreenAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Fish> elements;

    FishScreenAdapter(Context mContext, ArrayList<Fish> elements) {
        this.elements = elements;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return elements.get(position).getId();
    }

    @SuppressLint({"ViewHolder", "CheckResult"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Fish element = elements.get(position); // Get current fish

        convertView = layoutInflater.inflate(R.layout.item_element, parent, false);

        final ImageView FeatureImageShow = convertView.findViewById(R.id.item_element);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FeatureImageShow.setClipToOutline(true);
        }

        if (element.getFeatureImage() == null) {
            // Show image about that fish, if it have image from link?
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.dontTransform();
            Glide.with(convertView)
                    .setDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(element.getFeatureImageLink())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NotNull Bitmap resource, Transition<? super Bitmap> transition) {
                            element.setFeatureImage(resource);
                            FeatureImageShow.setImageBitmap(resource);
                            // Upload to database here
                        }
                    });
        } else {
            // Show default image in system. (Now, always here)
            FeatureImageShow.setImageBitmap(element.getFeatureImage());
        }
        return convertView;
    }
}
