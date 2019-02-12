package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.view.View;

import com.blogspot.tndev1403.fishSurvey.View.fsSavedDataActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsShowReviewActivity;

public class fsShowReviewPresenter {
    fsShowReviewActivity mContext;

    public fsShowReviewPresenter(fsShowReviewActivity mContext) {
        this.mContext = mContext;
        initArguments();
        initEvents();
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
        mContext.ivPreview.setImageBitmap(fsSavedDataActivity.CURRENT_BITMAP);
        mContext.tvName.setText(fsSavedDataActivity.REVIEW_ELEMENT.getName());
        mContext.tvLength.setText(fsSavedDataActivity.REVIEW_CATCHED.getLength() + " (cm)");
        mContext.tvWeight.setText(fsSavedDataActivity.REVIEW_CATCHED.getWeight() + " (kg)");
        mContext.tvPosition.setText("Kinh độ: " + fsSavedDataActivity.REVIEW_CATCHED.getLatitude() +
                "\nVĩ độ: " + fsSavedDataActivity.REVIEW_CATCHED.getLongitude());
        mContext.tvCatchedTime.setText(fsSavedDataActivity.REVIEW_CATCHED.getCatchedTime());
    }
}
