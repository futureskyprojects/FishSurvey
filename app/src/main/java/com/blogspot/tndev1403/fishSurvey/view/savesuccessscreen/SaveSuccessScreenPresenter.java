package com.blogspot.tndev1403.fishSurvey.view.savesuccessscreen;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.utils.ProcessingLibrary;
import com.blogspot.tndev1403.fishSurvey.view.fishcatchinputscreen.FishCatchInputPresenter;
import com.blogspot.tndev1403.fishSurvey.view.fishscreen.FishScreenPresenter;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderView;

import cn.pedant.SweetAlert.SweetAlertDialog;

class SaveSuccessScreenPresenter {
    private SaveSuccessScreenActivity mContext;

    SaveSuccessScreenPresenter(SaveSuccessScreenActivity mContext) {
        this.mContext = mContext;
        initArguments();
        initEvents();
        initSlider();
    }

    @SuppressLint("StaticFieldLeak")
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
            for (final Bitmap bm : FishCatchInputPresenter.LIST_CATCHED_IMAGES) {
                final SliderView sliderView = new DefaultSliderView(mContext);
                try {
                    if (bm == null)
                        return null;
                    sliderView.setImageByte(ProcessingLibrary.Using.BitmapToBytes(
                            bm
                    ));
                    viewHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mContext.slSlideReview.addSliderView(sliderView);
                            if (sweetAlertDialog.isShowing())
                                sweetAlertDialog.cancel();
                        }
                    });
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aSliderView) {
            super.onPostExecute(aSliderView);
            if (sweetAlertDialog.isShowing())
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
        mContext.tvName.setText(FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getName());
        mContext.tvLength.setText(FishCatchInputPresenter.fishCatch.getLength() + " (cm)");
        mContext.tvWeight.setText(FishCatchInputPresenter.fishCatch.getWeight() + " (kg)");
        mContext.tvPosition.setText(mContext.getResources().getString(R.string.longitude) + ": " + FishCatchInputPresenter.fishCatch.getLatitude() +
                "\n" + mContext.getResources().getString(R.string.latitude) + ": " + FishCatchInputPresenter.fishCatch.getLongitude());
        mContext.tvCatchedTime.setText(FishCatchInputPresenter.fishCatch.getCatchTime());
    }
}