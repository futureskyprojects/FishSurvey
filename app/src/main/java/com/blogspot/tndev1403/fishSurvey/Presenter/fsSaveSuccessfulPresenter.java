package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCatched;
import com.blogspot.tndev1403.fishSurvey.Model.fsCatchedHandler;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsHome;
import com.blogspot.tndev1403.fishSurvey.View.fsSaveSuccessfulActivity;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class fsSaveSuccessfulPresenter {
    fsSaveSuccessfulActivity mContext;

    public fsSaveSuccessfulPresenter(fsSaveSuccessfulActivity mContext) {
        this.mContext = mContext;
        initArguments();
        initEvents();
        initSlider();
    }

    class LoadListCatchedImages extends AsyncTask<String, Void, Void> {
        SweetAlertDialog sweetAlertDialog;
        Handler viewHandler = new Handler();

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
            for (Bitmap bm : fsCatchedInputPresenter.LIST_CATCHED_IMAGES) {
                final SliderView sliderView = new DefaultSliderView(mContext);
                try {
                    if (bm == null)
                        return null;
                    sliderView.setImageByte(TNLib.Using.BitmapToBytes(
                            bm
                    ));
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    viewHandler.post(new Runnable() {
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
        new LoadListCatchedImages().execute();
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
