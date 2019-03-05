package com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCatched;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.Model.fsElementHandler;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsCatchedInputPresenter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsSavedDataActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsShowReviewActivity;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class fsSavedDataAdapter extends RecyclerView.Adapter<fsSavedDataAdapter.fsRecycleViewHolder> {
    private ArrayList<fsCatched> catcheds;
    fsElementHandler handler;
    fsSavedDataActivity mContext;

    public fsSavedDataAdapter(ArrayList<fsCatched> catcheds, fsSavedDataActivity mContext) {
        this.catcheds = catcheds;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public fsSavedDataAdapter.fsRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.item_review, viewGroup, false);
        handler = new fsElementHandler(viewGroup.getContext());
        return new fsRecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final fsSavedDataAdapter.fsRecycleViewHolder fsRecycleViewHolder, final int i) {
        final fsCatched catched = catcheds.get(i);
        final fsElement element = handler.getEntry(catched.getElementID());
        fsRecycleViewHolder.tvFishName.setText(element.getName());
        fsRecycleViewHolder.tvClock.setText(catched.getCatchedTime());
        final String[] FileNames = catched.getImagePath().trim().split(" ");
        Log.d("XXXYYY", "onBindViewHolder: \n[" + catched.getImagePath().trim() + "]");
        fsRecycleViewHolder.ivThumb.setImageBitmap(
                TNLib.Using.BitmapFromFilePath(
                        (FileNames.length>0? ApplicationConfig.FOLDER.APP_DIR + File.separator + FileNames[0]:null)
                )
        );
        fsRecycleViewHolder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fsSavedDataActivity.REVIEW_CATCHED = catched;
                fsSavedDataActivity.REVIEW_ELEMENT = element;
                PrepareBitmapForShowHere(FileNames);
            }
        });
    }

    private void PrepareBitmapForShowHere(final String[] FileNames) {
        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("ĐANG XỬ LÝ");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (fsCatchedInputPresenter.LIST_CATCHED_IMAGES != null)
                    fsCatchedInputPresenter.LIST_CATCHED_IMAGES.clear();
                else
                    fsCatchedInputPresenter.LIST_CATCHED_IMAGES = new ArrayList<>();
                for (String x : FileNames) {
                    fsCatchedInputPresenter.LIST_CATCHED_IMAGES.add(
                            TNLib.Using.BitmapFromFilePath(ApplicationConfig.FOLDER.APP_DIR + File.separator + x)
                    );
                }
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sweetAlertDialog.cancel();
                        mContext.startActivity(
                                new Intent(mContext, fsShowReviewActivity.class)
                        );
                    }
                });
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return catcheds.size();
    }

    public class fsRecycleViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlItem;
        ImageView ivThumb;
        TextView tvFishName;
        TextView tvClock;

        public fsRecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            rlItem = (RelativeLayout) itemView.findViewById(R.id.review_item);
            ivThumb = (ImageView) itemView.findViewById(R.id.review_thumb);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivThumb.setClipToOutline(true);
            }
            tvFishName = (TextView) itemView.findViewById(R.id.review_fish_name);
            tvClock = (TextView) itemView.findViewById(R.id.review_catch_time);
        }
    }
}