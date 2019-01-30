package com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

public class fsElementAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    ArrayList<fsElement> elements;

    public fsElementAdapter(Context mContext, ArrayList<fsElement> elements) {
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
        return elements.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final fsElement element = elements.get(position);
        convertView = layoutInflater.inflate(R.layout.item_element, parent, false);
        final ImageView FeatureImageShow = (ImageView) convertView.findViewById(R.id.item_element);
        final View finalConvertView = convertView;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.dontTransform();
        Glide.with(convertView)
                .setDefaultRequestOptions(requestOptions)
                .asBitmap()
                .load(element.getFeatureImageLink())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        fsElement element1 = element;
                        if (resource != null)
                        {
                            element1.setFeatureImage(resource);
                            FeatureImageShow.setImageBitmap(resource);
                        }
                        else
                            FeatureImageShow.setImageDrawable(finalConvertView.getResources().getDrawable(R.drawable.ic_error_404));
                        // Upload to database here
                    }
                });
        return convertView;
    }
}
