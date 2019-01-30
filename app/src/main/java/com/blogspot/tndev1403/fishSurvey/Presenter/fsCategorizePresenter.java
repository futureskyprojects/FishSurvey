package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCategorize;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter.fsCategorizeAdapter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.View.fsCategorizeActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsElementActivity;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class fsCategorizePresenter {
    fsCategorizeActivity mContext;
    /* Data store declare */
    ArrayList<fsCategorize> categorizes;

    public fsCategorizePresenter(fsCategorizeActivity categorizeActivity) {
        this.mContext = categorizeActivity;
        initGirdView();
        initEvent();
    }

    private void initEvent() {
        initButtonOther();
    }

    private void initGirdView() {
        categorizes = fsCategorize.getFromAPI();
        fsCategorizeAdapter adapter = new fsCategorizeAdapter(mContext, categorizes);
        mContext.gvCategorizes.setAdapter(adapter);
        initGridViewItemClickEvent();
    }

    private void initGridViewItemClickEvent() {
        mContext.gvCategorizes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fsCategorize categorize = categorizes.get(position);
                Intent elementIntent = new Intent(mContext, fsElementActivity.class);
                elementIntent.putExtra(ApplicationConfig.CategorizeAPI.ID, categorize.getID());
                elementIntent.putExtra(ApplicationConfig.CategorizeAPI.Name, categorize.getName());
                mContext.startActivity(elementIntent);
            }
        });
        mContext.gvCategorizes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                View full_preview = layoutInflater.inflate(R.layout.dialog_preview, null);
                builder.setView(full_preview);
                ImageView imageView = (ImageView) full_preview.findViewById(R.id.dialog_full_preview);
                ImageView imageView1 = (ImageView) full_preview.findViewById(R.id.dialog_close_preview);


                final AlertDialog Ok = builder.create();
                Ok.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Ok.show();

                fsCategorize categorize = categorizes.get(position);
                if (categorize.getFeatureImage()!=null)
                    imageView.setImageBitmap(categorize.getFeatureImage());
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_404));

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

    private void initButtonOther() {
        mContext.btnOtherCategorizes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.info(mContext, "Hiện tại chưa hỗ trợ!", Toast.LENGTH_SHORT, true).show();
            }
        });
    }

}
