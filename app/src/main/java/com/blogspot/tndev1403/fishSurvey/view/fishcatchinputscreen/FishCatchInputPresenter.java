package com.blogspot.tndev1403.fishSurvey.view.fishcatchinputscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.view.fishscreen.FishScreenPresenter;
import com.blogspot.tndev1403.fishSurvey.view.homescreen.HomeScreenPresenter;
import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.model.FishCatch;
import com.blogspot.tndev1403.fishSurvey.data.db.FishCatchHandler;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.utils.ProcessingLibrary;
import com.blogspot.tndev1403.fishSurvey.view.savesuccessscreen.SaveSuccessScreenActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

import static android.content.Context.LOCATION_SERVICE;

public class FishCatchInputPresenter implements LocationListener {
    public static FishCatch fishCatch;

    GoogleApiClient mGoogleApiClient;

    /* private LocationRequest mLocationRequest; */
    private Location mLastLocation;
    private FishCatchInputActivity mContext;

    /* Declare varriable */
    public static ArrayList<Bitmap> LIST_CATCHED_IMAGES;
    FishCatchInputAdapter adapter;
    static Bitmap CURRENT_BITMAP = null;
    private FishCatchHandler handler;
    private Calendar calendar;
    private SweetAlertDialog saving;
    private ArrayList<String> FileNames;

    FishCatchInputPresenter(FishCatchInputActivity mContext) {
        this.mContext = mContext;
        handler = new FishCatchHandler(mContext);
        calendar = Calendar.getInstance();
        initGPSLocation();
        initDefaultValues();
        initEvent();
        initRecycleView();
    }

    private void initRecycleView() {
        LIST_CATCHED_IMAGES = new ArrayList<>();
        adapter = new FishCatchInputAdapter(LIST_CATCHED_IMAGES);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mContext.rvCatchedListImages.setLayoutManager(layoutManager);
        mContext.rvCatchedListImages.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initDefaultValues() {
        mContext.tvClock.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        mContext.tvCalendar.setText(
                calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                        (calendar.get(Calendar.MONTH) + 1) + "/" +
                        calendar.get(Calendar.YEAR)
        );
    }

    private void initGPSLocation() {
        requestLocationPermissions();
        if (!isGpsOn()) {
            Toasty.error(mContext, mContext.getResources().getString(R.string.please_turn_on_GPS), Toast.LENGTH_SHORT, true).show();
            mContext.onBackPressed();
        }
    }

    private void initEvent() {
        previewThumbnailEvents();
        btnCameraClick();
        btnGalleryClick();
        btnFinishClick();
        tvClockClick();
        tvCalendarClick();
    }

    private void tvCalendarClick() {
        mContext.tvCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                mContext.tvCalendar.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    datePickerDialog.create();
                }
                datePickerDialog.show();
            }
        });
    }

    private void tvClockClick() {
        mContext.tvClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                        hourOfDay, minute);
                                mContext.tvClock.setText(hourOfDay + ":" + minute);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    timePickerDialog.create();
                }
                timePickerDialog.show();
            }
        });
    }

    private void UnLocatedAlert() {
        SweetAlertDialog warning = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(mContext.getResources().getString(R.string.not_located_yet))
                .setContentText(mContext.getResources().getString(R.string.not_located_plz_try_again))
                .setConfirmButton(mContext.getResources().getString(R.string.close), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        sweetAlertDialog.cancel();
                    }
                });
        initGPSLocation();
        warning.show();
    }

    private void btnFinishClick() {
        mContext.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region SAVE
                //startLocationUpdates();
                if (mLastLocation == null) {
                    UnLocatedAlert();
                    return;
                }
                // -------------------------
                String Length = mContext.etLength.getText().toString();
                String Weight = mContext.etWeight.getText().toString();
                String CatchedDate = mContext.tvCalendar.getText().toString();
                String CatchedTimeX = mContext.tvClock.getText().toString();
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
                        mContext.btnFinish.setEnabled(false);
                        mContext.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Global.PERMISSION.WRITE_EXTERNAL_STORAGE);
                        return;
                    }
                }
                // Tiến hành tách và xử lý thời gian bắt
                String[] dates = CatchedDate.split("/");
                String[] times = CatchedTimeX.split(":");

                @SuppressLint("DefaultLocale") String catchedTime =
                        String.format("%02d/%02d/%02d %02d:%02d:%02d",
                                Integer.parseInt(dates[2]),
                                Integer.parseInt(dates[1]),
                                Integer.parseInt(dates[0]),
                                Integer.parseInt(times[0]),
                                Integer.parseInt(times[1]),
                                0);
                // Save iamge to app dir
                if (Global.CoppaFiles.checkAndCreateCoppaDirectory(mContext)) {
                    try {
                        new SaveFiles().execute(String.valueOf((int) (System.currentTimeMillis() / 1000)),
                                Length, Weight, catchedTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //endregion
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class SaveFiles extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (FileNames != null) {
                FileNames.clear();
                FileNames = null;
            }
            FileNames = new ArrayList<>();
            saving = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            saving.setCancelable(false);
            saving.setTitle(mContext.getResources().getString(R.string.saving));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                saving.create();
            }
            saving.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            int ID = (Integer.parseInt(strings[0]) + 1);
            for (int k = 0; k < LIST_CATCHED_IMAGES.size(); k++) {
                String FileName = "Capture_" + ID + "_" + k + "." + Global.CoppaFiles.APP_EXTENSION;
                if (ProcessingLibrary.Using.SaveImage(LIST_CATCHED_IMAGES.get(k), FileName, Global.CoppaFiles.APP_DIR))
                    FileNames.add(FileName);
            }
            fishCatch = new FishCatch(ID, FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getId(), ProcessingLibrary.Using.GetNowTimeString(),
                    strings[1], strings[2], strings[3], mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "", ProcessingLibrary.Using.StringListToSingalString(FileNames), HomeScreenPresenter.CURRENT_TRIP_ID, "");
            try {
                handler.add(fishCatch);
                return true;
            } catch (Exception ignore) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                if (saving.isShowing())
                    saving.cancel();
                Toasty.success(mContext, mContext.getResources().getString(R.string.save_success), Toast.LENGTH_SHORT, true).show();
                mContext.startActivity(new Intent(mContext, SaveSuccessScreenActivity.class));
                mContext.finish();
            } else {
                if (saving.isShowing())
                    saving.cancel();
                SweetAlertDialog alertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.can_not_save))
                        .setContentText(mContext.getResources().getString(R.string.have_problem_when_save_plz_try))
                        .setConfirmButton(mContext.getResources().getString(R.string.close), null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alertDialog.create();
                }
                alertDialog.show();
            }
        }
    }

    private void btnGalleryClick() {
        mContext.btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mContext.startActivityForResult(intent, Global.CODE.GALLERY_SELECT_REQUEST_CODE);
            }
        });
    }

    private void btnCameraClick() {
        mContext.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Check camera permission */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mContext.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        mContext.requestPermissions(new String[]{Manifest.permission.CAMERA}, Global.PERMISSION.CAMERA);
                        return;
                    }
                }
                /* Start camera intent */
                startCamera();
            }
        });
    }

    private String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        }
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
                        "com.blogspot.tndev1403.Coppa.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mContext.startActivityForResult(takePictureIntent, Global.CODE.IMAGE_CAPTURE_REQUEST_CODE);
            }
        }
    }

    private void previewThumbnailEvents() {
        // When user long press to preview thumbnail, show big in screen to user can view
        mContext.imgIhumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                @SuppressLint("InflateParams") View full_preview = layoutInflater.inflate(R.layout.dialog_preview, null);
                builder.setView(full_preview);
                ImageView imageView = full_preview.findViewById(R.id.dialog_full_preview);
                ImageView ivCloseIcon = full_preview.findViewById(R.id.dialog_close_preview);


                final AlertDialog zoomableThumnail = builder.create();
                if (zoomableThumnail.getWindow() != null)
                    zoomableThumnail.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                zoomableThumnail.show();

                imageView.setImageDrawable(mContext.imgIhumbnail.getDrawable());
                ivCloseIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zoomableThumnail.cancel();
                    }
                });
                return true;
            }
        });

        // When user click to preview thumbnail, show change image options
        mContext.imgIhumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);

                /* Init view */
                View view = layoutInflater.inflate(R.layout.dialog_thumbnail_layout, null);
                RelativeLayout camera = view.findViewById(R.id.dialog_camera);
                RelativeLayout gallery = view.findViewById(R.id.dialog_gallery);
                RelativeLayout defaultBitmap = view.findViewById(R.id.dialog_default);
                RelativeLayout close = view.findViewById(R.id.dialog_close);

                /* Init dialog */
                final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setView(view);
                final AlertDialog changeOptionDialog = dialog.create();
                if (changeOptionDialog.getWindow() != null)
                    changeOptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if (!changeOptionDialog.isShowing())
                    changeOptionDialog.show();

                /* init data and event */
                ImageView imageView = view.findViewById(R.id.item3_ic);
                if (FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage() != null)
                    imageView.setImageBitmap(FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage());
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_404));

                /* Init event */
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeOptionDialog.cancel();
                        mContext.btnCamera.performClick();
                    }
                });
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeOptionDialog.cancel();
                        mContext.btnGallery.performClick();
                    }
                });

                if (CURRENT_BITMAP == null) {
                    // If user not select image yet, hide Default image in option (Current bit map, which user selected will show)
                    defaultBitmap.setVisibility(View.GONE);
                    view.findViewById(R.id.lineOK).setVisibility(View.GONE);
                } else {
                    // If user was seleced, show dialog to ask user delete this fish image or not
                    defaultBitmap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeOptionDialog.cancel();
                            SweetAlertDialog ask = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(mContext.getResources().getString(R.string.sure_delete))
                                    .setContentText(mContext.getResources().getString(R.string.current_image_will_be_replace_by_default))
                                    .setCancelButton(mContext.getResources().getString(R.string.cancle), null)
                                    .setConfirmButton(mContext.getResources().getString(R.string.delete), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            // remove preview thumbnal now
                                            CURRENT_BITMAP = null;
                                            if (FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage() != null) {
                                                // If fish have default image in system, show it
                                                mContext.imgIhumbnail.setImageBitmap(FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage());
                                            } else {
                                                // else show not found image
                                                mContext.imgIhumbnail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_404));
                                            }
                                            sweetAlertDialog.cancel();
                                        }
                                    });
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ask.create();
                            }
                            ask.show();
                        }
                    });
                }

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeOptionDialog.cancel();
                    }
                });
            }
        });
    }


    /* GPS LOCATION permission */
    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Global.PERMISSION.LOCATION_GPS);
        }
    }

    /* Result processing */
    void resultProcessing(int reqCode, int resCode, Intent data) {
        if (reqCode == Global.CODE.GALLERY_SELECT_REQUEST_CODE && resCode == Activity.RESULT_OK) {
            // If user select image of fish from gallery
            if (data != null) {
                try {
                    // Add to list and show
                    InputStream inputStream = mContext.getContentResolver().openInputStream(data.getData());
                    setImageToListAndShowIt(BitmapFactory.decodeStream(inputStream));
                } catch (Exception e) {
                    fail();
                }
            }
        } else if (reqCode == Global.CODE.IMAGE_CAPTURE_REQUEST_CODE && resCode == Activity.RESULT_OK) {
            // If user capture image from camera
            if (currentPhotoPath != null && !currentPhotoPath.isEmpty()) {
                // Add to list and show
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                setImageToListAndShowIt(bitmap);
            }
        }
    }

    /* Permission result */
    void permissionResultProcessing(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Global.PERMISSION.CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(mContext, mContext.getResources().getString(R.string.camera_permission_success), Toast.LENGTH_SHORT, true).show();
                startCamera();
            } else {
                Toasty.error(mContext, mContext.getResources().getString(R.string.camera_permission_denied), Toast.LENGTH_SHORT, true).show();
            }
        } else if (requestCode == Global.PERMISSION.LOCATION_GPS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(mContext, mContext.getResources().getString(R.string.locate_permission_success), Toast.LENGTH_SHORT, true).show();
            } else {
                requestLocationPermissions();
            }
        } else if (requestCode == Global.PERMISSION.WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(mContext, mContext.getResources().getString(R.string.write_external_permission_success), Toast.LENGTH_SHORT, true).show();
                mContext.btnFinish.setEnabled(true);
            } else {
                requestLocationPermissions();
            }
        }
    }

    /* For location setting */
    private boolean isPlayServicesAvailable() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext)
                == ConnectionResult.SUCCESS;
    }

    /* Method for check gps is on or off */
    private boolean isGpsOn() {
        LocationManager manager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (manager != null) {
                    manager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 5000, 10, this);
                    return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /* Fail toast */
    private void fail() {
        Toasty.error(mContext, mContext.getResources().getString(R.string.operation_fail), Toast.LENGTH_SHORT, true).show();
    }

    // Method for set image to list and show
    private void setImageToListAndShowIt(Bitmap bm) {
        LIST_CATCHED_IMAGES.add(bm);
        adapter.notifyDataSetChanged();
        CURRENT_BITMAP = bm;
        setImageToShow();
    }

    // only for show
    void setImageToShow() {
        if (CURRENT_BITMAP != null)
            mContext.imgIhumbnail.setImageBitmap(CURRENT_BITMAP);
        else
            mContext.imgIhumbnail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_404));
    }
}
