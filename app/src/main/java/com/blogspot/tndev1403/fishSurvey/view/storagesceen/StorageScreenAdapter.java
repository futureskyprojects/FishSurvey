package com.blogspot.tndev1403.fishSurvey.view.storagesceen;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.model.FishCatch;
import com.blogspot.tndev1403.fishSurvey.model.Fish;
import com.blogspot.tndev1403.fishSurvey.data.db.FishHandler;
import com.blogspot.tndev1403.fishSurvey.utils.ProcessingLibrary;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.view.reviewscreen.ReviewScreenActivity;

import java.io.File;
import java.util.ArrayList;

public class StorageScreenAdapter extends RecyclerView.Adapter<StorageScreenAdapter.fsRecycleViewHolder> {
    private ArrayList<FishCatch> catcheds;
    FishHandler handler;
    StorageScreenActivity mContext;

    public StorageScreenAdapter(ArrayList<FishCatch> catcheds, StorageScreenActivity mContext) {
        this.catcheds = catcheds;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public StorageScreenAdapter.fsRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.item_review, viewGroup, false);
        handler = new FishHandler(viewGroup.getContext());
        return new fsRecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final StorageScreenAdapter.fsRecycleViewHolder fsRecycleViewHolder, final int i) {
        final FishCatch catched = catcheds.get(i);
        String code = Global.CoppaLanguage.getLanguageCode(mContext);
        final Fish element = handler.get(catched.getElementId());
        String[] names;
        if (catched.getElementId() != -1)
            names = element.getName().split(" - ");
        else
            names = new String[]{"Các loài khác", "Other families"};
        if (code == "vi") {
            fsRecycleViewHolder.tvFishName.setText(names[(names.length >= 2 ? 1 : 0)]);
        } else
            fsRecycleViewHolder.tvFishName.setText(names[0]);
        fsRecycleViewHolder.tvClock.setText(catched.getCatchTime());
        final String[] FileNames = catched.getImagePath().trim().split(" ");
        Log.d("XXXYYY", "onBindViewHolder: \n[" + catched.getImagePath().trim() + "]");
        fsRecycleViewHolder.ivThumb.setImageBitmap(
                ProcessingLibrary.Using.BitmapFromFilePath(
                        (FileNames.length > 0 ? Global.CoppaFiles.APP_DIR + File.separator + FileNames[0] : null)
                )
        );
        fsRecycleViewHolder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageScreenActivity.REVIEW_CATCHED = catched;
                if (catched.getElementId() != -1)
                    StorageScreenActivity.REVIEW_ELEMENT = element;
                else
                    StorageScreenActivity.REVIEW_ELEMENT = null;
                PrepareBitmapForShowHere();
            }
        });
    }

    private void PrepareBitmapForShowHere() {
        mContext.startActivity(
                new Intent(mContext, ReviewScreenActivity.class)
        );
        mContext.finish();
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
