package com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.R;

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
        fsElement element = elements.get(position);
        convertView = layoutInflater.inflate(R.layout.item_element, parent, false);
        ImageView FeatureImageShow = (ImageView) convertView.findViewById(R.id.item_element);
        if (element.getFeatureImage() != null)
            FeatureImageShow.setImageBitmap(element.getFeatureImage());
        else
            FeatureImageShow.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_error_404));
        return convertView;
    }
}
