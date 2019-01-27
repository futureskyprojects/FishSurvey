package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;
import com.blogspot.tndev1403.fishSurvey.View.fsCategorizeActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsNewUserActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class fsNewUserPresenter {
    fsNewUserActivity newUserActivity;

    public fsNewUserPresenter(fsNewUserActivity newUserActivity) {
        this.newUserActivity = newUserActivity;
        initEvent();
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
                            .setTitleText("CÒN THIẾU")
                            .setContentText("Vui lòng nhập đầy đủ thông tin!")
                            .setConfirmButton("Đóng",null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dialog.create();
                    }
                    dialog.show();
                }
                else {
                    /* else ask for sure of user */
                    SweetAlertDialog confirm = new SweetAlertDialog(newUserActivity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("HOÀN TẤT?")
                            .setContentText("Chắc chắn lưu lại những thông tin trên?")
                            .setCancelButton("Xem lại", null)
                            .setConfirmButton("Lưu", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    fsUser user = new fsUser(newUserActivity, newUserActivity.user_name.getText().toString(),
                                            newUserActivity.user_phone.getText().toString(),
                                            newUserActivity.user_boat_code.getText().toString());
                                    if (user.commit())
                                    {
                                        Toasty.success(newUserActivity, "Khai báo thông tin thành công!", Toast.LENGTH_SHORT, true).show();
                                        newUserActivity.startActivity(new Intent(newUserActivity, fsCategorizeActivity.class));
                                        newUserActivity.finish();
                                    } else {
                                        Toasty.error(newUserActivity, "Không thể cập nhật thông tin. Vui lòng thử lại", Toast.LENGTH_SHORT, true).show();
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