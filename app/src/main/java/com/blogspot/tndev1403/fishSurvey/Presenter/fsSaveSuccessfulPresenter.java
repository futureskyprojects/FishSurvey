package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsSaveSuccessfulActivity;

public class fsSaveSuccessfulPresenter {
    fsSaveSuccessfulActivity mContext;

    public fsSaveSuccessfulPresenter(fsSaveSuccessfulActivity mContext) {
        this.mContext = mContext;
        initArguments();
        initEvents();
    }

    private void initEvents() {
        initNextButton();
    }

    private void initNextButton() {
        mContext.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initArguments() {
        mContext.ivPreview.setImageBitmap((fsCatchedInputPresenter.CURRENT_BITMAP==null?fsElementPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage():fsCatchedInputPresenter.CURRENT_BITMAP));
        mContext.tvName.setText(fsElementPresenter.CURRENT_SELECTED_ELEMENT.getName());
        mContext.tvLength.setText(fsCatchedInputPresenter.catched.getLength() + " (cm)");
        mContext.tvWeight.setText(fsCatchedInputPresenter.catched.getWeight() + " (kg)");
        mContext.tvPosition.setText("Kinh độ: " + fsCatchedInputPresenter.catched.getLatitude() +
                "\nVĩ độ: " + fsCatchedInputPresenter.catched.getLongitude());
        mContext.tvCatchedTime.setText(fsCatchedInputPresenter.catched.getCatchedTime());
    }
}
