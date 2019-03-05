package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;
import com.blogspot.tndev1403.fishSurvey.Model.Services.API;
import com.blogspot.tndev1403.fishSurvey.Model.Services.SyncDataService;
import com.blogspot.tndev1403.fishSurvey.Model.fsCatchedHandler;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsElementActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsHome;
import com.blogspot.tndev1403.fishSurvey.View.fsNewUserActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsSavedDataActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class fsHomePresenter {
    public final static String ID_KEY = "ID";
    public static String CURRENT_TRIP_ID = "";
    public static int GID = -1;
    public static int PREVIEW_COUNT = 0;
    fsHome mContext;
    fsUser currentUser;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    fsCatchedHandler catchedHandler;
    /* Data store declare */

    public fsHomePresenter(fsHome fshome) {
        this.mContext = fshome;
        currentUser = new fsUser(mContext);
        initSharedPreferences();
        initDataHandler();
        initUserInfo();
        initGirdView();
        initTrips();
        initEvent();
    }

    private void initDataHandler() {
        catchedHandler = new fsCatchedHandler(mContext);
    }

    @SuppressLint("CommitPrefEdits")
    private void initSharedPreferences() {
        // Setting for preferences
        preferences = mContext.getSharedPreferences("TRIP", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void initTrips() {
        CURRENT_TRIP_ID = preferences.getString(ID_KEY, "");
        // Setting for view of trip
        if (!CURRENT_TRIP_ID.isEmpty()) {
            mContext.CreateEndTripButton();
        } else {
            mContext.CreateNewTripButton();
        }
    }


    private void initUserInfo() {
        currentUser.get();
        mContext.tvUsername.setText(currentUser.getUserName());
        mContext.tvPhone.setText(currentUser.getPhoneNumber());
        mContext.tvBoatCode.setText(currentUser.getBoatCode());
    }

    private void initEvent() {
        initButtonOther();
        initButtonReview();
        initButtonEditProfile();
        initEndTripsButton();
    }

    private void initEndTripsButton() {
        mContext.lnEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CURRENT_TRIP_ID.isEmpty()) {
                    if (TNLib.Using.IsMyServiceRunning(mContext, SyncDataService.class)) {
                        SweetAlertDialog alert = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("KHÔNG THỂ KẾT THÚC!")
                                .setContentText("Đồng bộ trước đó chưa xong! Vui lòng kết nối mạng để thục hiện tiếp.");
                        alert.setConfirmButton("Đóng", null);
                        alert.show();
                        return;
                    } else {
                        EndtripButton();
                    }
                } else {
                    NewTripEvent();
                }
            }
        });
    }

    void EndtripButton() {
        SweetAlertDialog swt = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("KẾT THÚC CHUYẾN?")
                .setContentText("ID: " + CURRENT_TRIP_ID)
                .setConfirmButton("Kết thúc", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        editor.putString(ID_KEY, "");
                        editor.commit();
                        initTrips();
                        initButtonReview();
                        final SweetAlertDialog al = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                                .setTitleText("ĐANG XỬ LÝ");
                        al.setCancelable(false);
                        al.show();
                        // Thực hiện quét CSDL ở đây
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (catchedHandler.UpdateAllFinishedTimeOfATrip(CURRENT_TRIP_ID) >= 0){
                                    al.cancel();
                                    mContext.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SweetAlertDialog sw = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("HOÀN TẤT!")
                                                    .setConfirmButton("Đóng", null);
                                            sw.show();
                                        }
                                    });
                                }
                            }
                        }).start();
                        API.Captain.CreateNew.Send(mContext);
                        // Khởi động services để đồng bộ tại đây
                        mContext.startService(new Intent(mContext, SyncDataService.class));
                        // Start send captain here

                    }
                })
                .setCancelButton("Hủy", null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            swt.create();
        }
        swt.show();
    }

    void NewTripEvent() {
        final String CurrentTimeStamp = TNLib.Using.GetCurrentTimeStamp();
        SweetAlertDialog swt = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("TẠO CHUYẾN MỚI?")
                .setContentText("ID: " + CurrentTimeStamp)
                .setConfirmButton("Tạo", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        editor.putString(ID_KEY, CurrentTimeStamp);
                        editor.commit();
                        initTrips();
                        SweetAlertDialog ok = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("THÀNH CÔNG!")
                                .setContentText("ID: " + CurrentTimeStamp)
                                .setConfirmButton("Đóng", null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ok.create();
                        }
                        ok.show();
                    }
                })
                .setCancelButton("Hủy", null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            swt.create();
        }
        swt.show();
    }


    private void initButtonEditProfile() {
        mContext.ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog alert = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("SỬA THÔNG TIN?")
                        .setConfirmButton("Sửa", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mContext.startActivity(new Intent(mContext, fsNewUserActivity.class));
                                mContext.finish();
                            }
                        })
                        .setCancelButton("Đóng", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alert.create();
                }
                alert.show();
            }
        });
    }

    private void initButtonReview() {
        mContext.lnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, fsSavedDataActivity.class));
            }
        });
        PREVIEW_COUNT = catchedHandler.CountAllEntry(fsHomePresenter.CURRENT_TRIP_ID);
        mContext.tvPreviewCount.setText("(" + PREVIEW_COUNT + ")");
    }

    private void initGirdView() {
        initCategozieClick();
    }


    private void initCategozieClick() {
        for (int i = 0; i < mContext.categozies.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mContext.categozies[i].setClipToOutline(true);
            }
            final int temp = i;
            mContext.categozies[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GID = temp;
                    Intent elementIntent = new Intent(mContext, fsElementActivity.class);
                    elementIntent.putExtra(ApplicationConfig.CategorizeAPI.ID, temp + 1);
                    elementIntent.putExtra(ApplicationConfig.CategorizeAPI.Name, "G" + temp);
                    mContext.startActivity(elementIntent);
                    mContext.finish();
                }
            });
            final int finalI = i;
            mContext.categozies[i].setOnLongClickListener(new View.OnLongClickListener() {
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
                    imageView.setImageDrawable(mContext.getResources().getDrawable(fsHome.FishList[finalI]));
                    imageView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Ok.cancel();
                        }
                    });
                    return true;
                }
            });
        }
    }

    private void initButtonOther() {
        mContext.btnOtherCategorizes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.info(mContext, "Hiện tại chưa hỗ trợ!", Toast.LENGTH_SHORT, true).show();
            }
        });
    }

}
