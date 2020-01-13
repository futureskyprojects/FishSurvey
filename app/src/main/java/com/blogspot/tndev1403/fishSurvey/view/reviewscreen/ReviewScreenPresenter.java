package com.blogspot.tndev1403.fishSurvey.view.reviewscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.data.db.FishCatchHandler;
import com.blogspot.tndev1403.fishSurvey.utils.ProcessingLibrary;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.view.storagesceen.StorageScreenActivity;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

class ReviewScreenPresenter {
    private ReviewScreenActivity mContext;

    ReviewScreenPresenter(ReviewScreenActivity mContext) {
        this.mContext = mContext;
        initArguments();
        initEvents();
        initSlider();
    }

    @SuppressLint("StaticFieldLeak")
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
                    Bitmap bm = ProcessingLibrary.Using.BitmapFromFilePath(Global.CoppaFiles.APP_DIR + File.separator + x.trim());
                    if (bm == null)
                        return null;
                    sliderView.setImageByte(ProcessingLibrary.Using.BitmapToBytes(
                            bm
                    ));
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    viewHander.post(new Runnable() {
                        @Override
                        public void run() {
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
        new LoadListCatchedImages().execute(StorageScreenActivity.REVIEW_CATCHED.getImagePath().split(" "));//ProcessingLibrary.Using.StringArrayListToStringArray(LIST_SAVED_FILES));
    }

    private void initEvents() {
        initCloseButton();
        initDeleteButton();
        initEditButton();
    }

    private void initEditButton() {
        mContext.btnEdit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View zv) {
                final Calendar calendar = Calendar.getInstance();
                /////////////Calendar.getInstance();
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                @SuppressLint("InflateParams") View v = layoutInflater.inflate(R.layout.edit_review, null);
                final EditText etLength = v.findViewById(R.id.fsct_lengthZ);
                final EditText etWeight = v.findViewById(R.id.fsct_weightZ);
                final TextView tvClock = v.findViewById(R.id.fsct_timeZ);
                final TextView tvCalendar = v.findViewById(R.id.fsct_dateZ);
                final Button btnFinish = v.findViewById(R.id.fsct_finishZ);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(v);
                builder.setCancelable(false);
                final AlertDialog alertDialog = builder.create();
                if (alertDialog.getWindow() != null)
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ///////////////// INIT
                v.findViewById(R.id.fsct_backZ).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                etLength.setText(StorageScreenActivity.REVIEW_CATCHED.getLength());
                etWeight.setText(StorageScreenActivity.REVIEW_CATCHED.getWeight());
                tvClock.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                tvCalendar.setText(
                        calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                                (calendar.get(Calendar.MONTH) + 1) + "/" +
                                calendar.get(Calendar.YEAR)
                );
                //
                tvCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        calendar.set(year, month, dayOfMonth);
                                        tvCalendar.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                    }
                                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            datePickerDialog.create();
                        }
                        datePickerDialog.show();
                    }
                });
                //
                tvClock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                                hourOfDay, minute);
                                        tvClock.setText(hourOfDay + ":" + minute);
                                    }
                                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            timePickerDialog.create();
                        }
                        timePickerDialog.show();
                    }
                });
                //
                btnFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //region EDIT
                        String Length = etLength.getText().toString();
                        String Weight = etWeight.getText().toString();
                        String CatchedDate = tvCalendar.getText().toString();
                        String CatchedTimeX = tvClock.getText().toString();
                        // Check
                        if (Length.isEmpty() || Weight.isEmpty() || CatchedDate.isEmpty() || CatchedTimeX.isEmpty()) {
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SweetAlertDialog alertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText(mContext.getResources().getString(R.string.not_enough))
                                            .setContentText(mContext.getResources().getString(R.string.plz_insert_full_info))
                                            .setConfirmButton(mContext.getResources().getString(R.string.close), null);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        alertDialog.create();
                                    }
                                    alertDialog.show();
                                }
                            });
                            return;
                        }
                        // Check permission
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                btnFinish.setEnabled(false);
                                mContext.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Global.PERMISSION.WRITE_EXTERNAL_STORAGE);
                                return;
                            }
                        }
                        // Update
                        StorageScreenActivity.REVIEW_CATCHED.setWeight(etWeight.getText().toString());
                        StorageScreenActivity.REVIEW_CATCHED.setLength(etLength.getText().toString());
                        StorageScreenActivity.REVIEW_CATCHED.setCatchTime(ProcessingLibrary.Using.MyCalendarToReverseString(calendar));
                        if (new FishCatchHandler(mContext).update(StorageScreenActivity.REVIEW_CATCHED) > 0) {
                            new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(mContext.getResources().getString(R.string.update_success))
                                    .show();
                            mContext.startActivity(new Intent(mContext, ReviewScreenActivity.class));
                            mContext.finish();
                        } else
                            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(mContext.getResources().getString(R.string.update_fail))
                                    .show();
                        //endregion
                        alertDialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void initDeleteButton() {
        mContext.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog ask = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.sure_delete))
                        .setConfirmButton(mContext.getResources().getString(R.string.delete), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                if (new FishCatchHandler(mContext).deleteEntry(StorageScreenActivity.REVIEW_CATCHED.getId()) > 0) {
                                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(mContext.getResources().getString(R.string.deleted))
                                            .show();
                                    mContext.onBackPressed();
                                }
                            }
                        })
                        .setCancelButton(mContext.getResources().getString(R.string.cancle), null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ask.create();
                }
                ask.show();
            }
        });
    }

    private void initCloseButton() {
        mContext.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.onBackPressed();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initArguments() {
        if (StorageScreenActivity.REVIEW_ELEMENT == null)
            mContext.tvName.setText("Other families - Các loài khác");
        else
            mContext.tvName.setText(StorageScreenActivity.REVIEW_ELEMENT.getName());
        mContext.tvLength.setText(StorageScreenActivity.REVIEW_CATCHED.getLength() + " (cm)");
        mContext.tvWeight.setText(StorageScreenActivity.REVIEW_CATCHED.getWeight() + " (kg)");
        mContext.tvPosition.setText(mContext.getResources().getString(R.string.longitude) + ": " + StorageScreenActivity.REVIEW_CATCHED.getLatitude() +
                "\n" + mContext.getResources().getString(R.string.latitude) + ": " + StorageScreenActivity.REVIEW_CATCHED.getLongitude());
        mContext.tvCatchedTime.setText(StorageScreenActivity.REVIEW_CATCHED.getCatchTime());
    }
}
