package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCategorize;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter.fsCategorizeAdapter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsCategorizeActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsElementActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class fsCategorizePresenter {
    fsCategorizeActivity mContext;
    /* Data store declare */

    public fsCategorizePresenter(fsCategorizeActivity categorizeActivity) {
        this.mContext = categorizeActivity;
        initGirdView();
        initEvent();
    }

    private void initEvent() {
        initButtonOther();
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
                    imageView.setImageDrawable(mContext.getResources().getDrawable(fsCategorizeActivity.FishList[finalI]));
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
