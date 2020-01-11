package com.blogspot.tndev1403.fishSurvey.view.fishscreen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.model.Fish;
import com.blogspot.tndev1403.fishSurvey.data.db.FishHandler;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.view.fishcatchinputscreen.FishCatchInputActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FishScreenPresenter {
    public static Fish CURRENT_SELECTED_ELEMENT;
    private FishScreenActivity mContext;
    private ArrayList<Fish> elements;
    private int CATEGORIZE_ID = -1;

    private FishHandler elementHandler;

    FishScreenPresenter(FishScreenActivity mContext) {
        this.mContext = mContext;
        initArguments();
        new InitGridView().execute();
    }

    private void initArguments() {
        Intent currentIntent = mContext.getIntent();
        elementHandler = new FishHandler(mContext);
        CATEGORIZE_ID = currentIntent.getIntExtra(Global.CategorizeAPI.ID, -1);
    }


    @SuppressLint("StaticFieldLeak")
    class InitGridView extends AsyncTask<Void, Void, Void> {
        SweetAlertDialog mLoading;

        @Override
        protected void onPreExecute() {
            final InitGridView itSelf = this;
            elements = new ArrayList<>();
            mLoading = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(mContext.getResources().getString(R.string.loading))
                    .setCancelButton(mContext.getResources().getString(R.string.cancle), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            if (itSelf.cancel(true))
                                sweetAlertDialog.cancel();
                        }
                    });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mLoading.create();
            }
            mLoading.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            elements = elementHandler.getByCategory(CATEGORIZE_ID);
            FishScreenAdapter adapter = new FishScreenAdapter(mContext, elements);
            mContext.gridView.setAdapter(adapter);
            gridViewItemClickedEvent();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mLoading.isShowing())
                mLoading.cancel();
            return null;
        }
    }

    private void gridViewItemClickedEvent() {
        mContext.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CURRENT_SELECTED_ELEMENT = elements.get(position);
                Intent catchedIntent = new Intent(mContext, FishCatchInputActivity.class);
                mContext.startActivity(catchedIntent);
                mContext.finish();
            }
        });


        mContext.gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                @SuppressLint("InflateParams") View full_preview = layoutInflater.inflate(R.layout.dialog_preview, null);
                builder.setView(full_preview);
                ImageView imageView = full_preview.findViewById(R.id.dialog_full_preview);
                ImageView imageView1 = full_preview.findViewById(R.id.dialog_close_preview);


                final AlertDialog zoomableImageDialog = builder.create();
                if (zoomableImageDialog.getWindow() != null)
                    zoomableImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                zoomableImageDialog.show();

                Fish element = elements.get(position);
                if (element.getFeatureImage() != null)
                    imageView.setImageBitmap(element.getFeatureImage());
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_404));

                imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zoomableImageDialog.cancel();
                    }
                });
                return true;
            }
        });
    }
}
