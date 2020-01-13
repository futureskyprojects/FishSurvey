package com.blogspot.tndev1403.fishSurvey.view.homescreen;

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

import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.model.Captain;
import com.blogspot.tndev1403.fishSurvey.model.Fish;
import com.blogspot.tndev1403.fishSurvey.view.fishscreen.FishScreenPresenter;
import com.blogspot.tndev1403.fishSurvey.services.SyncDataService;
import com.blogspot.tndev1403.fishSurvey.data.db.FishCatchHandler;
import com.blogspot.tndev1403.fishSurvey.utils.ProcessingLibrary;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.view.fishcatchinputscreen.FishCatchInputActivity;
import com.blogspot.tndev1403.fishSurvey.view.fishscreen.FishScreenActivity;
import com.blogspot.tndev1403.fishSurvey.view.newcaptainscreen.NewCaptainScreenActivity;
import com.blogspot.tndev1403.fishSurvey.view.storagesceen.StorageScreenActivity;

import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class HomeScreenPresenter {
    public final static String ID_KEY = "ID";
    public static String CURRENT_TRIP_ID = "";
    public static int GID = -1;
    private HomeScreenActivity mContext;
    private Captain currentUser;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private FishCatchHandler catchedHandler;
    Timer tmUpdate;

    HomeScreenPresenter(HomeScreenActivity mContext) {

        this.mContext = mContext;
        this.currentUser = new Captain(this.mContext);

        initSharedPreferences();
        initDataHandler();
        initUserInfo();
        initCategoryEvent();
        initTrips();
        initEvents();
        continueSync();
    }

    // Continue sync record of previous trips
    private void continueSync() {
        tmUpdate = new Timer();
        tmUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                final int notSyncNumber = catchedHandler.countNotSyncRecords();
                mContext.runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if (notSyncNumber <= 0) {
                            mContext.tvNotSyncShow.setVisibility(View.INVISIBLE);
                        } else {
                            mContext.tvNotSyncShow.setVisibility(View.VISIBLE);
                            if (!ProcessingLibrary.Using.IsMyServiceRunning(mContext, SyncDataService.class)) {
                                mContext.tvNotSyncShow.setText(mContext.getResources().getString(R.string.still_have) + " " + notSyncNumber + " " + mContext.getResources().getString(R.string.records_not_sync));
                                mContext.startService(new Intent(mContext, SyncDataService.class));
                            } else {
                                mContext.tvNotSyncShow.setText(mContext.getResources().getString(R.string.synchronizing_n_records, notSyncNumber + ""));
                            }
                        }
                    }
                });
            }
        }, 1000, 1000);
    }

    private void initDataHandler() {
        catchedHandler = new FishCatchHandler(mContext);
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
            // If current trip is NOT empty, it mean trip created before
            // Then show END trip button to user can sync
            mContext.CreateEndTripButton();
        } else {
            // If current trip is empty, it mean no trip create before
            // Then show trip button to user create new
            mContext.CreateNewTripButton();
        }
    }


    private void initUserInfo() {
        currentUser.get();
        mContext.tvUsername.setText(currentUser.getUserName());
        mContext.tvPhone.setText(currentUser.getPhoneNumber());
        mContext.tvBoatCode.setText(currentUser.getBoatCode());
    }

    private void initEvents() {
        initButtonOther();
        reviewButtonEvent();
        editProfileButtonEvent();
        initEndTripsButton();
        initLanguageFlagsButton();
        initNewTripButtonEvent();
    }

    private void initNewTripButtonEvent() {
        mContext.tvNoTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTripEvent();
            }
        });
    }

    private void initLanguageFlagsButton() {
        mContext.ivVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.change_language).toUpperCase())
                        .setConfirmButton(mContext.getResources().getString(R.string.change), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                Global.CoppaLanguage.updateLanguage(mContext, "vi");
                                mContext.startActivity(new Intent(mContext, HomeScreenActivity.class));
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
                                Global.CoppaLanguage.updateLanguage(mContext, "en");
                                mContext.startActivity(new Intent(mContext, HomeScreenActivity.class));
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
                    if (ProcessingLibrary.Using.IsMyServiceRunning(mContext, SyncDataService.class)) {
                        SweetAlertDialog alert = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(mContext.getResources().getString(R.string.can_not_finish))
                                .setContentText(mContext.getResources().getString(R.string.sync_not_finish_please_connect_internet));
                        alert.setConfirmButton(mContext.getResources().getString(R.string.close), null);
                        alert.show();
                    } else {
                        endTripEvent();
                    }
                } else {
                    newTripEvent();
                }
            }
        });
    }

    private void endTripEvent() {
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
                                if (catchedHandler.updateFishTime(CURRENT_TRIP_ID) >= 0) {
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
                                            reviewButtonEvent();
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

    private void newTripEvent() {
        final String CurrentTimeStamp = ProcessingLibrary.Using.GetCurrentTimeStamp();
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


    private void editProfileButtonEvent() {
        mContext.ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog alert = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.edit_user_info))
                        .setConfirmButton(mContext.getResources().getString(R.string.edit), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mContext.startActivity(new Intent(mContext, NewCaptainScreenActivity.class));
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

    @SuppressLint("SetTextI18n")
    private void reviewButtonEvent() {
        mContext.lnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, StorageScreenActivity.class));
                mContext.finish();
            }
        });
        int PREVIEW_COUNT = catchedHandler.count(HomeScreenPresenter.CURRENT_TRIP_ID);
        mContext.tvPreviewCount.setText("(" + PREVIEW_COUNT + ")");
    }

    private void initCategoryEvent() {
        for (int i = 0; i < mContext.categozies.size(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mContext.categozies.get(i).setClipToOutline(true);
            }
            final int finalI = i;

            // Event when press to category
            mContext.categozies.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((finalI + 1) == mContext.categozies.size()) {
                        // If click to undefine category
                        FishScreenPresenter.CURRENT_SELECTED_ELEMENT = new Fish(-1, -1, "Other families - Những loài khác", ProcessingLibrary.Using.DrawableToBitmap(mContext, R.drawable.f8), "");
                        Intent catchIntent = new Intent(mContext, FishCatchInputActivity.class);

                        // Move to input caught fish
                        mContext.startActivity(catchIntent);
                        mContext.finish();
                    } else {
                        // When click to determine category
                        GID = finalI;
                        Intent elementIntent = new Intent(mContext, FishScreenActivity.class);
                        elementIntent.putExtra(Global.CategorizeAPI.ID, finalI + 1);
                        elementIntent.putExtra(Global.CategorizeAPI.Name, "G" + finalI);

                        // Move to select kind of fish
                        mContext.startActivity(elementIntent);
                        mContext.finish();
                    }
                }
            });

            // Event when long press to category
            mContext.categozies.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if ((finalI + 1) == mContext.categozies.size()) {
                        // If press to undefined fish category, return for not show
                        return true;
                    }

                    // Show zoomable image of this fish category
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    @SuppressLint("InflateParams") View fullPreview = layoutInflater.inflate(R.layout.dialog_preview, null);
                    builder.setView(fullPreview);
                    ImageView imageView = fullPreview.findViewById(R.id.dialog_full_preview);
                    ImageView imageView1 = fullPreview.findViewById(R.id.dialog_close_preview);


                    final AlertDialog zoomableImageDialog = builder.create();
                    if (zoomableImageDialog.getWindow() != null) {
                        zoomableImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                    zoomableImageDialog.show();
                    imageView.setImageDrawable(mContext.getResources().getDrawable(HomeScreenActivity.FishList[finalI]));
                    imageView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            zoomableImageDialog.cancel();
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
