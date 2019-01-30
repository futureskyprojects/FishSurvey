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
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsCatchedInputActivity;
import com.bumptech.glide.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

import static android.content.Context.LOCATION_SERVICE;

public class fsCatchedInputPresenter implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;

    public GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    fsCatchedInputActivity mContext;
    /* Declare varriable */
    public Bitmap CURRENT_BITMAP = null;

    public fsCatchedInputPresenter(fsCatchedInputActivity mContext) {
        this.mContext = mContext;
        initGPSLocation();
        /* Init event */
        initEvent();
    }

    private void initGPSLocation() {
        requestLocationPermissions();
        if (isPlayServicesAvailable()) {
            setUpLocationClientIfNeeded();
            buildLocationRequest();
        } else {
            Toasty.error(mContext, "Xin lỗi, Thiết bị này không hỗ trợ dịch vụ của Google", Toast.LENGTH_SHORT, true).show();
            mContext.finish();
        }
        if (!isGpsOn()) {
            Toasty.error(mContext, "Vui lòng bật định vị GPS lên trước!", Toast.LENGTH_SHORT, true).show();
            mContext.finish();
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
                                mContext.tvCalendar.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
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
                                mContext.tvClock.setText(hourOfDay + ":" + minute);
                            }
                        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    timePickerDialog.create();
                }
                timePickerDialog.show();
            }
        });
    }

    void UnLocatedAlert() {
        SweetAlertDialog warning = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("CHƯA ĐỊNH VỊ ĐƯỢC")
                .setContentText("Hiện tại vẫn chưa định vị được vị trí, hãy thử lại!")
                .setConfirmButton("Đóng", new SweetAlertDialog.OnSweetClickListener() {
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
                startLocationUpdates();
                if (mLastLocation == null) {
                    UnLocatedAlert();
                    return;
                }
                Address m = TNLib.Location.getAddress(mContext, mLastLocation.getLatitude(), mLastLocation.getLongitude());
                String result = "";
                result = String.format(Locale.getDefault(), "Kinh độ:%f\nVĩ độ: %f",
                        mLastLocation.getLatitude(), mLastLocation.getLongitude());
                if (m != null) {
                    result += "\n--------------\n" +
                            "Quốc gia: " + m.getCountryName() +
                            "\nĐịa điểm: " + m.getLocality() +
                            "\n(" + m.getSubLocality() + ")";
                }
                Toasty.info(mContext, result, Toast.LENGTH_SHORT, true).show();
                Log.d(TAG, result);
            }
        });
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

    private void startCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mContext.startActivityForResult(camera, ApplicationConfig.CODE.IMAGE_CAPTURE_REQUEST_CODE);
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
                                    .setTitleText("CHẮC CHẮN XÓA?")
                                    .setContentText("Ảnh hiện tại sẽ bị xóa và thay thế bằng ảnh mặc định của phần mềm!")
                                    .setCancelButton("Hủy", null)
                                    .setConfirmButton("Xóa", new SweetAlertDialog.OnSweetClickListener() {
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
                    CURRENT_BITMAP = null;
                    InputStream inputStream = mContext.getContentResolver().openInputStream(data.getData());
                    CURRENT_BITMAP = BitmapFactory.decodeStream(inputStream);
                    setImageToShow();
                } catch (Exception e) {
                    Log.e(TAG, "resultProcessing: " + e.getMessage());
                    fail();
                }
            }
        } else if (reqCode == ApplicationConfig.CODE.IMAGE_CAPTURE_REQUEST_CODE && resCode == Activity.RESULT_OK) {
            CURRENT_BITMAP = null;
            CURRENT_BITMAP = (Bitmap) data.getExtras().get("data");
            setImageToShow();
        }
    }

    /* Permission result */
    public void permissionResultProcessing(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ApplicationConfig.PERMISSION.CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(mContext, "Cấp quyền sử dụng máy ảnh thành công!", Toast.LENGTH_SHORT, true).show();
                startCamera();
            } else {
                Toasty.error(mContext, "Đã từ chối cấp quyền sử dụng máy ảnh", Toast.LENGTH_SHORT, true).show();
            }
        } else if (requestCode == ApplicationConfig.PERMISSION.LOCATION_GPS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(mContext, "Cấp quyền truy cập vị trí thành công!", Toast.LENGTH_SHORT, true).show();
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
        Log.e(TAG, "onConnectionFailed:  ĐM nó fail cmnr!");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, String.format(Locale.getDefault(), "onLocationChanged : %f, %f",
                location.getLatitude(), location.getLongitude()));
        mLastLocation = location;
    }

    //endregion
    /* Annoucement */
    void fail() {
        Toasty.error(mContext, "Xin lỗi! Thao tác không thành công.", Toast.LENGTH_SHORT, true);
    }

    void setImageToShow() {
        if (CURRENT_BITMAP != null)
            mContext.imgIhumbnail.setImageBitmap(CURRENT_BITMAP);
        else
            mContext.imgIhumbnail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_404));
    }
}
