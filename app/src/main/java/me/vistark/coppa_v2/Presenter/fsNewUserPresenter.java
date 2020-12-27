package me.vistark.coppa_v2.Presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import me.vistark.coppa_v2.Model.Entity.fsUser;
import me.vistark.coppa_v2.R;
import me.vistark.coppa_v2.View.fsHome;
import me.vistark.coppa_v2.View.fsNewUserActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class fsNewUserPresenter {
    fsNewUserActivity newUserActivity;
    fsUser previousUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public fsNewUserPresenter(fsNewUserActivity newUserActivity) {
        this.newUserActivity = newUserActivity;
        initSharedPreferences();
        initUserAndFill();
        initEvent();
    }

    private void initSharedPreferences() {
    }

    private void initUserAndFill() {
        previousUser = new fsUser(newUserActivity);
        if (previousUser.get())
        {
            newUserActivity.user_name.setText(previousUser.getUserName());
            newUserActivity.user_phone.setText(previousUser.getPhoneNumber());
            newUserActivity.user_boat_code.setText(previousUser.getBoatCode());
        }
    }

    void initEvent() {
        initFinishButton();
    }

    private void initFinishButton() {
        newUserActivity.finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* init input info still have empty field */
                if (newUserActivity.user_name.getText().toString().isEmpty()||
                        newUserActivity.user_phone.getText().toString().isEmpty() ||
                        newUserActivity.user_boat_code.getText().toString().isEmpty())
                {
                    SweetAlertDialog dialog = new SweetAlertDialog(newUserActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(newUserActivity.getResources().getString(R.string.not_enough))
                            .setContentText(newUserActivity.getResources().getString(R.string.plz_insert_full_info))
                            .setConfirmButton(newUserActivity.getResources().getString(R.string.close),null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dialog.create();
                    }
                    dialog.show();
                }
                else {
                    if (newUserActivity.user_name.getText().toString().matches(".*\\d+.*"))
                    {
                        SweetAlertDialog dialog = new SweetAlertDialog(newUserActivity, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(newUserActivity.getResources().getString(R.string.review).toUpperCase() + "!")
                                .setContentText(newUserActivity.getResources().getString(R.string.name_can_not_included_number))
                                .setConfirmButton(newUserActivity.getResources().getString(R.string.close),null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            dialog.create();
                        }
                        dialog.show();
                        return;
                    }
                    /* else ask for sure of user */
                    SweetAlertDialog confirm = new SweetAlertDialog(newUserActivity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(newUserActivity.getResources().getString(R.string.finish).toUpperCase() + "?")
                            .setContentText(newUserActivity.getResources().getString(R.string.sure_save_all_infor_above))
                            .setCancelButton(newUserActivity.getResources().getString(R.string.review), null)
                            .setConfirmButton(newUserActivity.getResources().getString(R.string.save), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    fsUser user = new fsUser(newUserActivity, newUserActivity.user_name.getText().toString(),
                                            newUserActivity.user_phone.getText().toString(),
                                            newUserActivity.user_boat_code.getText().toString());
                                    if (user.commit())
                                    {
                                        Toasty.success(newUserActivity, newUserActivity.getResources().getString(R.string.declare_info_successfully), Toast.LENGTH_SHORT, true).show();
                                        newUserActivity.startActivity(new Intent(newUserActivity, fsHome.class));
                                        newUserActivity.finish();
                                    } else {
                                        Toasty.error(newUserActivity, newUserActivity.getResources().getString(R.string.can_not_update_info_plz_try), Toast.LENGTH_SHORT, true).show();
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
