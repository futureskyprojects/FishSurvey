package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCatched;
import com.blogspot.tndev1403.fishSurvey.Model.fsCatchedHandler;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsHome;
import com.blogspot.tndev1403.fishSurvey.View.fsSaveSuccessfulActivity;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class fsSaveSuccessfulPresenter {
    fsSaveSuccessfulActivity mContext;

    public fsSaveSuccessfulPresenter(fsSaveSuccessfulActivity mContext) {
        this.mContext = mContext;
        initArguments();
        initEvents();
        initSlider();
    }

    private void initSlider() {
        for (int i = 0; i < fsCatchedInputPresenter.LIST_CATCHED_IMAGES.size(); i++) {
            SliderView sliderView = new DefaultSliderView(mContext);
            sliderView.setImageByte(TNLib.Using.BitmapToBytes(
                    fsCatchedInputPresenter.LIST_CATCHED_IMAGES.get(i)
            ));
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            mContext.slSlideReview.addSliderView(sliderView);
        }
    }

    private void initEvents() {
        initNextButton();
    }


    private void initNextButton() {
        mContext.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.onBackPressed();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initArguments() {
//        mContext.ivPreview.setImageBitmap((fsCatchedInputPresenter.CURRENT_BITMAP==null?fsElementPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage():fsCatchedInputPresenter.CURRENT_BITMAP));
        mContext.tvName.setText(fsElementPresenter.CURRENT_SELECTED_ELEMENT.getName());
        mContext.tvLength.setText(fsCatchedInputPresenter.catched.getLength() + " (cm)");
        mContext.tvWeight.setText(fsCatchedInputPresenter.catched.getWeight() + " (kg)");
        mContext.tvPosition.setText(mContext.getResources().getString(R.string.longitude) + ": " + fsCatchedInputPresenter.catched.getLatitude() +
                "\n" + mContext.getResources().getString(R.string.latitude) + ": " + fsCatchedInputPresenter.catched.getLongitude());
        mContext.tvCatchedTime.setText(fsCatchedInputPresenter.catched.getCatchedTime());
    }
}
