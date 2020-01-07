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
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;
import com.blogspot.tndev1403.fishSurvey.Model.Services.SyncDataService;
import com.blogspot.tndev1403.fishSurvey.Model.fsCatchedHandler;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsCatchedInputActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsElementActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsHome;
import com.blogspot.tndev1403.fishSurvey.View.fsNewUserActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsSavedDataActivity;

import java.util.Timer;
import java.util.TimerTask;

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
    public Timer tmUpdate;
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
        initNotSync();
    }
    private void initNotSync() {
        tmUpdate = new Timer();
        tmUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                final int notSyncNumber = catchedHandler.CountNotSyncRecords();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (notSyncNumber <= 0) {
                            mContext.tvNotSyncShow.setVisibility(View.INVISIBLE);
                        } else {
                            mContext.tvNotSyncShow.setVisibility(View.VISIBLE);
                            if (!TNLib.Using.IsMyServiceRunning(mContext, SyncDataService.class)) {
                                mContext.tvNotSyncShow.setText(mContext.getResources().getString(R.string.still_have) + " " + notSyncNumber + " " + mContext.getResources().getString(R.string.records_not_sync));
                                mContext.startService(new Intent(mContext, SyncDataService.class));
                            } else {
                                mContext.tvNotSyncShow.setText(mContext.getResources().getString(R.string.synchronizing_n_records, notSyncNumber + ""));
                            }
                        }
                    }
                });
            }
        }, 200, 300);
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
        initLanguaeFlagsButton();
        initNorecordShowTextEvent();
    }

    private void initNorecordShowTextEvent() {
        mContext.tvNoTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTripEvent();
            }
        });
    }

    private void initLanguaeFlagsButton() {
        mContext.ivVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.change_language).toUpperCase())
                        .setConfirmButton(mContext.getResources().getString(R.string.change), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                ApplicationConfig.LANGUAGE.UpdateLanguage(mContext, "vi");
                                mContext.startActivity(new Intent(mContext, fsHome.class));
                                mContext.finish();
                            }
                        })
                        .setCancelButton(mContext.getResources().getString(R.string.close), null);
                sweetAlertDialog.show();
            }
        });
        mContext.ivEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.change_language).toUpperCase())
                        .setConfirmButton(mContext.getResources().getString(R.string.change), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                ApplicationConfig.LANGUAGE.UpdateLanguage(mContext, "en");
                                mContext.startActivity(new Intent(mContext, fsHome.class));
                                mContext.finish();
                            }
                        })
                        .setCancelButton(mContext.getResources().getString(R.string.close), null);
                sweetAlertDialog.show();
            }
        });
    }

    private void initEndTripsButton() {
        mContext.lnEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CURRENT_TRIP_ID.isEmpty()) {
                    if (TNLib.Using.IsMyServiceRunning(mContext, SyncDataService.class)) {
                        SweetAlertDialog alert = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(mContext.getResources().getString(R.string.can_not_finish))
                                .setContentText(mContext.getResources().getString(R.string.sync_not_finish_please_connect_internet));
                        alert.setConfirmButton(mContext.getResources().getString(R.string.close), null);
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
                .setTitleText(mContext.getResources().getString(R.string.end_trip).toUpperCase() + "?")
                .setContentText("ID: " + CURRENT_TRIP_ID)
                .setConfirmButton(mContext.getResources().getString(R.string.end), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        final SweetAlertDialog al = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                                .setTitleText(mContext.getResources().getString(R.string.processing));
                        al.setCancelable(false);
                        al.show();
                        // Thực hiện quét CSDL ở đây
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (catchedHandler.UpdateAllFinishedTimeOfATrip(CURRENT_TRIP_ID) >= 0) {
                                    mContext.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SweetAlertDialog sw = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText(mContext.getResources().getString(R.string.finish).toUpperCase())
                                                    .setConfirmButton(mContext.getResources().getString(R.string.close), null);
                                            sw.show();
                                        }
                                    });
                                    editor.putString(ID_KEY, "");
                                    editor.commit();
                                    mContext.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            al.cancel();
                                            initTrips();
                                            initButtonReview();
                                        }
                                    });
                                }
                            }
                        }).start();
                        // Khởi động services để đồng bộ tại đây
                        mContext.startService(new Intent(mContext, SyncDataService.class));
                        // Start send captain here

                    }
                })
                .setCancelButton(mContext.getResources().getString(R.string.cancle), null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            swt.create();
        }
        swt.show();
    }

    void NewTripEvent() {
        final String CurrentTimeStamp = TNLib.Using.GetCurrentTimeStamp();
        SweetAlertDialog swt = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(mContext.getResources().getString(R.string.create_new_trip))
                .setContentText("ID: " + CurrentTimeStamp)
                .setConfirmButton(mContext.getResources().getString(R.string.create), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        editor.putString(ID_KEY, CurrentTimeStamp);
                        editor.commit();
                        initTrips();
                        SweetAlertDialog ok = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText(mContext.getResources().getString(R.string.success).toUpperCase() + "!")
                                .setContentText("ID: " + CurrentTimeStamp)
                                .setConfirmButton(mContext.getResources().getString(R.string.close), null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ok.create();
                        }
                        ok.show();
                    }
                })
                .setCancelButton(mContext.getResources().getString(R.string.cancle), null);
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
                        .setTitleText(mContext.getResources().getString(R.string.edit_user_info))
                        .setConfirmButton(mContext.getResources().getString(R.string.edit), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mContext.startActivity(new Intent(mContext, fsNewUserActivity.class));
                                mContext.finish();
                            }
                        })
                        .setCancelButton(mContext.getResources().getString(R.string.close), new SweetAlertDialog.OnSweetClickListener() {
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
                mContext.finish();
            }
        });
        PREVIEW_COUNT = catchedHandler.CountAllEntry(fsHomePresenter.CURRENT_TRIP_ID);
        mContext.tvPreviewCount.setText("(" + PREVIEW_COUNT + ")");
    }

    private void initGirdView() {
        initCategozieClick();
    }


    private void initCategozieClick() {
        for (int i = 0; i < mContext.categozies.size(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mContext.categozies.get(i).setClipToOutline(true);
            }
            final int finalI = i;
            mContext.categozies.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((finalI + 1) == mContext.categozies.size()) {
                        fsElement element = new fsElement(-1, -1, "Other families - Những loài khác", TNLib.Using.DrawableToBitmap(mContext, R.drawable.f8), "");
                        fsElementPresenter.CURRENT_SELECTED_ELEMENT = element;
                        Intent catchedIntent = new Intent(mContext, fsCatchedInputActivity.class);
                        mContext.startActivity(catchedIntent);
                        mContext.finish();
                    } else {
                        GID = finalI;
                        Intent elementIntent = new Intent(mContext, fsElementActivity.class);
                        elementIntent.putExtra(ApplicationConfig.CategorizeAPI.ID, finalI + 1);
                        elementIntent.putExtra(ApplicationConfig.CategorizeAPI.Name, "G" + finalI);
                        mContext.startActivity(elementIntent);
                        mContext.finish();
                    }
                }
            });
            mContext.categozies.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if ((finalI + 1) == mContext.categozies.size())
                        return true;
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
                Toasty.info(mContext, mContext.getResources().getString(R.string.not_support), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

}
