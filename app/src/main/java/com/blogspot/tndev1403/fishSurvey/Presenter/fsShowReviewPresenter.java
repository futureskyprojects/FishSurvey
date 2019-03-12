package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsSavedDataActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsShowReviewActivity;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class fsShowReviewPresenter {
    fsShowReviewActivity mContext;

    public fsShowReviewPresenter(fsShowReviewActivity mContext) {
        this.mContext = mContext;
        initArguments();
        initEvents();
        initSlider();
    }

    class LoadListCatchedImages extends AsyncTask<String, Void, Void> {
        SweetAlertDialog sweetAlertDialog;
        Handler viewHander = new Handler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(mContext.getResources().getString(R.string.loading_image));
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            for (String x : strings) {
                final SliderView sliderView = new DefaultSliderView(mContext);
                try {
                    Bitmap bm = TNLib.Using.BitmapFromFilePath(ApplicationConfig.FOLDER.APP_DIR + File.separator + x.trim());
                    if (bm == null)
                        return null;
                    sliderView.setImageByte(TNLib.Using.BitmapToBytes(
                            bm
                    ));
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    viewHander.post(new Runnable() {
                        @Override
                        public void run() {
                            if (sliderView != null)
                                mContext.slSlideReview.addSliderView(sliderView);
                        }
                    });
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage() + "");
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aSliderView) {
            super.onPostExecute(aSliderView);
            sweetAlertDialog.cancel();
        }
    }

    private void initSlider() {
        new LoadListCatchedImages().execute(fsSavedDataActivity.REVIEW_CATCHED.getImagePath().split(" "));//TNLib.Using.StringArrayListToStringArray(LIST_SAVED_FILES));
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
        if (fsSavedDataActivity.REVIEW_ELEMENT == null)
            mContext.tvName.setText("Other families - Các loài khác");
        else
            mContext.tvName.setText(fsSavedDataActivity.REVIEW_ELEMENT.getName());
        mContext.tvLength.setText(fsSavedDataActivity.REVIEW_CATCHED.getLength() + " (cm)");
        mContext.tvWeight.setText(fsSavedDataActivity.REVIEW_CATCHED.getWeight() + " (kg)");
        mContext.tvPosition.setText(mContext.getResources().getString(R.string.longitude) + ": " + fsSavedDataActivity.REVIEW_CATCHED.getLatitude() +
                "\n" + mContext.getResources().getString(R.string.latitude) + ": " + fsSavedDataActivity.REVIEW_CATCHED.getLongitude());
        mContext.tvCatchedTime.setText(fsSavedDataActivity.REVIEW_CATCHED.getCatchedTime());
    }
}
