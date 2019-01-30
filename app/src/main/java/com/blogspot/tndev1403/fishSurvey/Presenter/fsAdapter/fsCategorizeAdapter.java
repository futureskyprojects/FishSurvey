package com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCategorize;
import com.blogspot.tndev1403.fishSurvey.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

public class fsCategorizeAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    ArrayList<fsCategorize> categorizes;

    public fsCategorizeAdapter(Context mContext, ArrayList<fsCategorize> categorizes) {
        this.categorizes = categorizes;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return categorizes.size();
    }

    @Override
    public Object getItem(int position) {
        return categorizes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categorizes.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final fsCategorize categorize = categorizes.get(position);
        convertView = layoutInflater.inflate(R.layout.item_categorize, parent, false);
        final ImageView FeatureImageShow = (ImageView) convertView.findViewById(R.id.item_categorize);
        final View finalConvertView = convertView;
        Glide.with(convertView)
                .asBitmap()
                .load(categorize.getFeatureImageLink())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource != null)
                            FeatureImageShow.setImageBitmap(resource);
                        else
                            FeatureImageShow.setImageDrawable(finalConvertView.getResources().getDrawable(R.drawable.ic_error_404));
                    }
                });
        return convertView;
    }
}
