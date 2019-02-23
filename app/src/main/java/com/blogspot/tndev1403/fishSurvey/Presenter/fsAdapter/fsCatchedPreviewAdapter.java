package com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCatchedPreview;
import com.blogspot.tndev1403.fishSurvey.R;

import java.util.ArrayList;

public class fsCatchedPreviewAdapter extends RecyclerView.Adapter<fsCatchedPreviewAdapter.MyHolder>{
    private ArrayList<fsCatchedPreview> catchedPreviews;

    public fsCatchedPreviewAdapter(ArrayList<fsCatchedPreview> catchedPreviews) {
        this.catchedPreviews = catchedPreviews;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.item_small_view, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return catchedPreviews.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
