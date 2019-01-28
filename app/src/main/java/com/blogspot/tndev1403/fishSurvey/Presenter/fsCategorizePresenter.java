package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.view.View;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCategorize;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter.fsCategorizeAdapter;
import com.blogspot.tndev1403.fishSurvey.View.fsCategorizeActivity;

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
        categorizes = new ArrayList<>();
        categorizes = fsCategorize.getFromAPI();
        fsCategorizeAdapter adapter = new fsCategorizeAdapter(mContext, categorizes);
        mContext.gvCategorizes.setAdapter(adapter);
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
