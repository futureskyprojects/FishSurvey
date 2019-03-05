package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.view.View;
import android.widget.ImageView;

import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsSavedDataActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsShowReviewActivity;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderView;

public class fsShowReviewPresenter {
    fsShowReviewActivity mContext;

    public fsShowReviewPresenter(fsShowReviewActivity mContext) {
        this.mContext = mContext;
        initArguments();
        initEvents();
        initSlider();
    }

    private void initSlider() {
        for (int i = 0; i < fsCatchedInputPresenter.LIST_CATCHED_IMAGES.size(); i++)
        {
            SliderView sliderView = new DefaultSliderView(mContext);
            try {
                sliderView.setImageByte(TNLib.Using.BitmapToBytes(
                        fsCatchedInputPresenter.LIST_CATCHED_IMAGES.get(i)
                ));
                sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                mContext.slSlideReview.addSliderView(sliderView);
            } catch (Exception e) {

            }
        }
    }
    private void initEvents() {
        initCloseButton();
    }

    private void initCloseButton() {
        mContext.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.finish();
            }
        });
    }

    private void initArguments() {
//        mContext.ivPreview.setImageBitmap(fsSavedDataActivity.CURRENT_BITMAP);
        mContext.tvName.setText(fsSavedDataActivity.REVIEW_ELEMENT.getName());
        mContext.tvLength.setText(fsSavedDataActivity.REVIEW_CATCHED.getLength() + " (cm)");
        mContext.tvWeight.setText(fsSavedDataActivity.REVIEW_CATCHED.getWeight() + " (kg)");
        mContext.tvPosition.setText(mContext.getResources().getString(R.string.longitude) + ": " + fsCatchedInputPresenter.catched.getLatitude() +
                "\n" + mContext.getResources().getString(R.string.latitude) + ": " + fsCatchedInputPresenter.catched.getLongitude());
        mContext.tvCatchedTime.setText(fsSavedDataActivity.REVIEW_CATCHED.getCatchedTime());
    }
}
