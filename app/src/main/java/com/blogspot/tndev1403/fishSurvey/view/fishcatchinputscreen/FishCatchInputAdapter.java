package com.blogspot.tndev1403.fishSurvey.view.fishcatchinputscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.view.fishscreen.FishScreenPresenter;
import com.blogspot.tndev1403.fishSurvey.R;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class FishCatchInputAdapter extends RecyclerView.Adapter<FishCatchInputAdapter.MyHolder> {
    private ArrayList<Bitmap> fishImages;

    FishCatchInputAdapter(ArrayList<Bitmap> catchedPreviews) {
        this.fishImages = catchedPreviews;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.item_small_view, viewGroup, false);
        CheckEmpty(v);
        return new MyHolder(v);
    }

    private void CheckEmpty(View v) {
        if (FishCatchInputPresenter.LIST_CATCHED_IMAGES.size() <= 0) {
            ((FishCatchInputActivity) v.getContext()).tvAnnoucement.setVisibility(View.VISIBLE);
        } else {
            ((FishCatchInputActivity) v.getContext()).tvAnnoucement.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
        final Context mContext = myHolder.img.getContext();
        myHolder.img.setImageBitmap(fishImages.get(i));
        myHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set image to show
                FishCatchInputPresenter.CURRENT_BITMAP = fishImages.get(i);
                ((FishCatchInputActivity) v.getContext()).presenter.setImageToShow();
            }
        });

        // When user long press
        myHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                // ask user delete or not
                SweetAlertDialog swd = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.delete_image))
                        .setContentText(mContext.getResources().getString(R.string.sure_to_do_delete_image_num) + (i + 1) + "?")
                        .setConfirmButton(mContext.getResources().getString(R.string.delete), new SweetAlertDialog.OnSweetClickListener() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                FishCatchInputPresenter.LIST_CATCHED_IMAGES.remove(i);
                                FishCatchInputPresenter.CURRENT_BITMAP = FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage();
                                ((FishCatchInputActivity) v.getContext()).presenter.setImageToShow();
                                ((FishCatchInputActivity) v.getContext()).presenter.adapter.notifyDataSetChanged();
                                Toasty.info(v.getContext(), mContext.getResources().getString(R.string.deleted), Toast.LENGTH_SHORT, true);
                                CheckEmpty(v);
                                sweetAlertDialog.cancel();
                            }
                        })
                        .setCancelButton(mContext.getResources().getString(R.string.close), null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    swd.create();
                }
                swd.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return fishImages.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.fish_catched_item_image);
        }
    }
}
