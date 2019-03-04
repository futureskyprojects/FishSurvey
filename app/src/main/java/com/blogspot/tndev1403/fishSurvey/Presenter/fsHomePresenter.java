package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;
import com.blogspot.tndev1403.fishSurvey.Model.fsCatchedHandler;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.View.fsElementActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsHome;
import com.blogspot.tndev1403.fishSurvey.View.fsNewUserActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsSavedDataActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class fsHomePresenter {
    public static int PREVIEW_COUNT = 0;
    fsHome mContext;
    fsUser currentUser;
    /* Data store declare */

    public fsHomePresenter(fsHome fshome) {
        this.mContext = fshome;
        currentUser = new fsUser(mContext);
        initUserInfo();
        initGirdView();
        initEvent();
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
        fsCatchedHandler catchedHandler = new fsCatchedHandler(mContext);
        PREVIEW_COUNT = catchedHandler.getAllEntry().size();
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
                    Intent elementIntent = new Intent(mContext, fsElementActivity.class);
                    elementIntent.putExtra(ApplicationConfig.CategorizeAPI.ID, temp + 1);
                    elementIntent.putExtra(ApplicationConfig.CategorizeAPI.Name, "G" + temp);
                    mContext.startActivity(elementIntent);
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
