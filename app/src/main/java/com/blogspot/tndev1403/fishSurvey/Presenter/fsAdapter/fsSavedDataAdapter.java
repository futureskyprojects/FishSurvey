package com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCatched;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.Model.fsElementHandler;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsSavedDataActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsShowReviewActivity;

import java.util.ArrayList;

public class fsSavedDataAdapter extends RecyclerView.Adapter<fsSavedDataAdapter.fsRecycleViewHolder> {
    private ArrayList<fsCatched> catcheds;
    fsElementHandler handler;

    public fsSavedDataAdapter(ArrayList<fsCatched> catcheds) {
        this.catcheds = catcheds;
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
    public void onBindViewHolder(@NonNull final fsSavedDataAdapter.fsRecycleViewHolder fsRecycleViewHolder, int i) {
        final fsCatched catched = catcheds.get(i);
        final fsElement element = handler.getEntry(catched.getElementID());
        final Bitmap CurrentBM = TNLib.Using.BitmapFromFilePath(catched.getImagePath());
        fsRecycleViewHolder.ivThumb.setImageBitmap(CurrentBM);
        fsRecycleViewHolder.tvFishName.setText(element.getName());
        fsRecycleViewHolder.tvClock.setText(catched.getCatchedTime());
        fsRecycleViewHolder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fsSavedDataActivity.REVIEW_CATCHED = catched;
                fsSavedDataActivity.REVIEW_ELEMENT = element;
                fsSavedDataActivity.CURRENT_BITMAP = CurrentBM;
                fsRecycleViewHolder.ivThumb.getContext().startActivity(
                        new Intent(fsRecycleViewHolder.ivThumb.getContext(), fsShowReviewActivity.class)
                );
            }
        });
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
