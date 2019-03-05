package com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Presenter.fsCatchedInputPresenter;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsElementPresenter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.View.fsCatchedInputActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class fsCatchedPreviewAdapter extends RecyclerView.Adapter<fsCatchedPreviewAdapter.MyHolder>{
    private ArrayList<Bitmap> catchedPreviews;

    public fsCatchedPreviewAdapter(ArrayList<Bitmap> catchedPreviews) {
        this.catchedPreviews = catchedPreviews;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.item_small_view, viewGroup, false);
        CheckEmpty(v);
        return new MyHolder(v);
    }

    void CheckEmpty(View v) {
        if (((fsCatchedInputActivity) v.getContext()).presenter.LIST_CATCHED_IMAGES.size() <= 0) {
            ((fsCatchedInputActivity) v.getContext()).tvAnnoucement.setVisibility(View.VISIBLE);
        } else {
            ((fsCatchedInputActivity) v.getContext()).tvAnnoucement.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
        final Context mContext = myHolder.img.getContext();
        myHolder.img.setImageBitmap(catchedPreviews.get(i));
        myHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set image to show
                fsCatchedInputPresenter.CURRENT_BITMAP = catchedPreviews.get(i);
                ((fsCatchedInputActivity) v.getContext()).presenter.setImageToShow();
            }
        });
        myHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                SweetAlertDialog swd = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.delete_image))
                        .setContentText(mContext.getResources().getString(R.string.sure_to_do_delete_image_num) + (i + 1) + "?")
                        .setConfirmButton(mContext.getResources().getString(R.string.delete), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                ((fsCatchedInputActivity) v.getContext()).presenter.LIST_CATCHED_IMAGES.remove(i);
                                fsCatchedInputPresenter.CURRENT_BITMAP = fsElementPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage();
                                ((fsCatchedInputActivity) v.getContext()).presenter.setImageToShow();
                                ((fsCatchedInputActivity) v.getContext()).presenter.adapter.notifyDataSetChanged();
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
        return catchedPreviews.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.fish_catched_item_image);
        }
    }
}
