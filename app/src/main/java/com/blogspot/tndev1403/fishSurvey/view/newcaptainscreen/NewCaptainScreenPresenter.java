package com.blogspot.tndev1403.fishSurvey.view.newcaptainscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.model.Captain;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.view.homescreen.HomeScreenActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

class NewCaptainScreenPresenter {
    private NewCaptainScreenActivity mContext;
    private Captain previousUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    NewCaptainScreenPresenter(NewCaptainScreenActivity newUserActivity) {
        this.mContext = newUserActivity;
        initSharedPreferences();
        initUserAndFill();
        initEvent();
    }

    private void initSharedPreferences() {
    }

    private void initUserAndFill() {
        previousUser = new Captain(mContext);
        if (previousUser.get())
        {
            mContext.user_name.setText(previousUser.getUserName());
            mContext.user_phone.setText(previousUser.getPhoneNumber());
            mContext.user_boat_code.setText(previousUser.getBoatCode());
        }
    }

    private void initEvent() {
        initFinishButton();
    }

    private void initFinishButton() {
        mContext.finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* init input info still have empty field */
                if (mContext.user_name.getText().toString().isEmpty()||
                        mContext.user_phone.getText().toString().isEmpty() ||
                        mContext.user_boat_code.getText().toString().isEmpty())
                {
                    SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(mContext.getResources().getString(R.string.not_enough))
                            .setContentText(mContext.getResources().getString(R.string.plz_insert_full_info))
                            .setConfirmButton(mContext.getResources().getString(R.string.close),null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dialog.create();
                    }
                    dialog.show();
                }
                else {
                    if (mContext.user_name.getText().toString().matches(".*\\d+.*"))
                    {
                        SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(mContext.getResources().getString(R.string.review).toUpperCase() + "!")
                                .setContentText(mContext.getResources().getString(R.string.name_can_not_included_number))
                                .setConfirmButton(mContext.getResources().getString(R.string.close),null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            dialog.create();
                        }
                        dialog.show();
                        return;
                    }
                    /* else ask for sure of captain */
                    SweetAlertDialog confirm = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(mContext.getResources().getString(R.string.finish).toUpperCase() + "?")
                            .setContentText(mContext.getResources().getString(R.string.sure_save_all_infor_above))
                            .setCancelButton(mContext.getResources().getString(R.string.review), null)
                            .setConfirmButton(mContext.getResources().getString(R.string.save), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    Captain user = new Captain(mContext, mContext.user_name.getText().toString(),
                                            mContext.user_phone.getText().toString(),
                                            mContext.user_boat_code.getText().toString());
                                    if (user.updateCaptainInfo())
                                    {
                                        Toasty.success(mContext, mContext.getResources().getString(R.string.declare_info_successfully), Toast.LENGTH_SHORT, true).show();
                                        mContext.startActivity(new Intent(mContext, HomeScreenActivity.class));
                                        mContext.finish();
                                    } else {
                                        Toasty.error(mContext, mContext.getResources().getString(R.string.can_not_update_info_plz_try), Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        confirm.create();
                    }
                    confirm.show();
                }
            }
        });
    }
}
