package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.Manifest;
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
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.MainActivity;
import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCatched;
import com.blogspot.tndev1403.fishSurvey.Model.fsCatchedHandler;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter.fsCatchedPreviewAdapter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsCatchedInputActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsSaveSuccessfulActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

import static android.content.Context.LOCATION_SERVICE;

public class fsCatchedInputPresenter implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static fsCatched catched;
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;

    public GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private fsCatchedInputActivity mContext;
    /* Declare varriable */
    public static ArrayList<Bitmap> LIST_CATCHED_IMAGES;
    public fsCatchedPreviewAdapter adapter;
    public static Bitmap CURRENT_BITMAP = null;
    private fsCatchedHandler handler;
    private Calendar calendar;
    private SweetAlertDialog saving;
    ArrayList<String> FileNames;

    public fsCatchedInputPresenter(fsCatchedInputActivity mContext) {
        this.mContext = mContext;
        handler = new fsCatchedHandler(mContext);
        calendar = Calendar.getInstance();
        initGPSLocation();
        initDefaultValues();
        /* Init event */
        initEvent();
        /* Config recycle view */
        initRecycleView();
    }

    private void initRecycleView() {
        LIST_CATCHED_IMAGES = new ArrayList<>();
        adapter = new fsCatchedPreviewAdapter(LIST_CATCHED_IMAGES);
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
        if (isPlayServicesAvailable()) {
            setUpLocationClientIfNeeded();
            buildLocationRequest();
        } else {
            Toasty.error(mContext, mContext.getResources().getString(R.string.device_not_sp_gg_services), Toast.LENGTH_SHORT, true).show();
            mContext.onBackPressed();
        }
        if (!isGpsOn()) {
            Toasty.error(mContext, mContext.getResources().getString(R.string.please_turn_on_GPS), Toast.LENGTH_SHORT, true).show();
            mContext.onBackPressed();
        }
    }

    private void initEvent() {
        thumbnailClick();
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
                startLocationUpdates();
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
                        mContext.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ApplicationConfig.PERMISSION.WRITE_EXTERNAL_STORAGE);
                        return;
                    }
                }
                // Save iamge to app dir
                if (ApplicationConfig.FOLDER.CheckAndCreate()) {
                    try {
                        new SaveFiles().execute(String.valueOf((int) (System.currentTimeMillis() / 1000)),
                                Length, Weight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //endregion
            }
        });
    }

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
                String FileName = "Capture_" + ID + "_" + k + "." + ApplicationConfig.FOLDER.APP_EXTENSION;
                if (TNLib.Using.SaveImage(LIST_CATCHED_IMAGES.get(k), FileName, ApplicationConfig.FOLDER.APP_DIR))
                    FileNames.add(FileName);
            }
            catched = new fsCatched(ID, fsElementPresenter.CURRENT_SELECTED_ELEMENT.getID(), TNLib.Using.GetNowTimeString(),
                    strings[1], strings[2], TNLib.Using.MyCalendarToReverseString(calendar), mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "", TNLib.Using.StringListToSingalString(FileNames), fsHomePresenter.CURRENT_TRIP_ID, "");
            try {
                handler.addEntry(catched);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "onClick: " + e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                if (saving.isShowing())
                    saving.cancel();
                Toasty.success(mContext, mContext.getResources().getString(R.string.save_success), Toast.LENGTH_SHORT, true);
                mContext.startActivity(new Intent(mContext, fsSaveSuccessfulActivity.class));
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
                mContext.startActivityForResult(intent, ApplicationConfig.CODE.GALLERY_SELECT_REQUEST_CODE);
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
                        mContext.requestPermissions(new String[]{Manifest.permission.CAMERA}, ApplicationConfig.PERMISSION.CAMERA);
                        return;
                    }
                }
                /* Start camera intent */
                startCamera();
            }
        });
    }

    String currentPhotoPath;

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
                mContext.startActivityForResult(takePictureIntent, ApplicationConfig.CODE.IMAGE_CAPTURE_REQUEST_CODE);
            }
        }
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        mContext.startActivityForResult(camera, ApplicationConfig.CODE.IMAGE_CAPTURE_REQUEST_CODE);
    }

    private void thumbnailClick() {
        mContext.imgIhumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                View full_preview = layoutInflater.inflate(R.layout.dialog_preview, null);
                builder.setView(full_preview);
                ImageView imageView = (ImageView) full_preview.findViewById(R.id.dialog_full_preview);
                ImageView imageView1 = (ImageView) full_preview.findViewById(R.id.dialog_close_preview);


                final AlertDialog Ok = builder.create();
                Ok.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Ok.show();

                imageView.setImageDrawable(mContext.imgIhumbnail.getDrawable());
                imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Ok.cancel();
                    }
                });
                return true;
            }
        });
        mContext.imgIhumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                /* Init view */
                View view = layoutInflater.inflate(R.layout.dialog_thumbnail_layout, null);
                RelativeLayout camera = (RelativeLayout) view.findViewById(R.id.dialog_camera);
                RelativeLayout gallery = (RelativeLayout) view.findViewById(R.id.dialog_gallery);
                RelativeLayout defaultBitmap = (RelativeLayout) view.findViewById(R.id.dialog_default);
                RelativeLayout close = (RelativeLayout) view.findViewById(R.id.dialog_close);
                /* Init dialog */
                final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setView(view);
                final AlertDialog ok = dialog.create();
                ok.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if (!ok.isShowing())
                    ok.show();
                /* init data and event */
                ImageView imageView = (ImageView) view.findViewById(R.id.item3_ic);
                if (fsElementPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage() != null)
                    imageView.setImageBitmap(fsElementPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage());
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_404));
                /* Init event */
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ok.cancel();
                        mContext.btnCamera.performClick();
                    }
                });
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ok.cancel();
                        mContext.btnGallery.performClick();
                    }
                });
                if (CURRENT_BITMAP == null) {
                    defaultBitmap.setVisibility(View.GONE);
                    ((LinearLayout) view.findViewById(R.id.lineOK)).setVisibility(View.GONE);
                } else {
                    defaultBitmap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ok.cancel();
                            SweetAlertDialog ask = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(mContext.getResources().getString(R.string.sure_delete))
                                    .setContentText(mContext.getResources().getString(R.string.current_image_will_be_replace_by_default))
                                    .setCancelButton(mContext.getResources().getString(R.string.cancle), null)
                                    .setConfirmButton(mContext.getResources().getString(R.string.delete), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            CURRENT_BITMAP = null;
                                            if (fsElementPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage() != null)
                                                mContext.imgIhumbnail.setImageBitmap(fsElementPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage());
                                            else
                                                mContext.imgIhumbnail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_404));
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
                        ok.cancel();
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
                    ApplicationConfig.PERMISSION.LOCATION_GPS);
        }
    }

    /* Result processing */
    public void resultProcessing(int reqCode, int resCode, Intent data) {
        if (reqCode == ApplicationConfig.CODE.GALLERY_SELECT_REQUEST_CODE && resCode == Activity.RESULT_OK) {
            if (data == null) {
                // User don't choose anything
            } else {
                try {
                    InputStream inputStream = mContext.getContentResolver().openInputStream(data.getData());
                    setImageToListAndShowIt(BitmapFactory.decodeStream(inputStream));
                } catch (Exception e) {
                    Log.e(TAG, "resultProcessing: " + e.getMessage());
                    fail();
                }
            }
        } else if (reqCode == ApplicationConfig.CODE.IMAGE_CAPTURE_REQUEST_CODE && resCode == Activity.RESULT_OK) {
            if (currentPhotoPath != null && !currentPhotoPath.isEmpty())
            {
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                setImageToListAndShowIt(bitmap);
            }
        }
    }

    /* Permission result */
    public void permissionResultProcessing(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ApplicationConfig.PERMISSION.CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(mContext, mContext.getResources().getString(R.string.camera_permission_success), Toast.LENGTH_SHORT, true).show();
                startCamera();
            } else {
                Toasty.error(mContext, mContext.getResources().getString(R.string.camera_permission_denied), Toast.LENGTH_SHORT, true).show();
            }
        } else if (requestCode == ApplicationConfig.PERMISSION.LOCATION_GPS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(mContext, mContext.getResources().getString(R.string.locate_permission_success), Toast.LENGTH_SHORT, true).show();
            } else {
                requestLocationPermissions();
            }
        } else if (requestCode == ApplicationConfig.PERMISSION.WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(mContext, mContext.getResources().getString(R.string.write_external_permission_success), Toast.LENGTH_SHORT, true).show();
                mContext.btnFinish.setEnabled(true);
            } else {
                requestLocationPermissions();
            }
        }
    }

    /* For location setting */
    //region Location setting
    private boolean isPlayServicesAvailable() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext)
                == ConnectionResult.SUCCESS;
    }

    private boolean isGpsOn() {
        LocationManager manager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void setUpLocationClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void buildLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    }

    public void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
            return;
        }
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            mLastLocation = lastLocation;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    //endregion
    /* Annoucement */
    void fail() {
        Toasty.error(mContext, mContext.getResources().getString(R.string.operation_fail), Toast.LENGTH_SHORT, true);
    }

    public void setImageToListAndShowIt(Bitmap bm) {
//        bm = TNLib.Using.ResizeBitmap(bm, (int) ApplicationConfig.MAX_IMAGE_SIZE);
        LIST_CATCHED_IMAGES.add(bm);
        adapter.notifyDataSetChanged();
        CURRENT_BITMAP = bm;
        setImageToShow();
    }

    public void setImageToShow() {
        if (CURRENT_BITMAP != null)
            mContext.imgIhumbnail.setImageBitmap(CURRENT_BITMAP);
        else
            mContext.imgIhumbnail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_404));
    }
}
