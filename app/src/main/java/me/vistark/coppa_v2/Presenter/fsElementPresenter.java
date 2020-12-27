package me.vistark.coppa_v2.Presenter;

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

import me.vistark.coppa_v2.Model.Config.ApplicationConfig;
import me.vistark.coppa_v2.Model.Entity.fsElement;
import me.vistark.coppa_v2.Model.fsElementHandler;
import me.vistark.coppa_v2.Presenter.fsAdapter.fsElementAdapter;
import me.vistark.coppa_v2.R;
import me.vistark.coppa_v2.View.fsCatchedInputActivity;
import me.vistark.coppa_v2.View.fsElementActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class fsElementPresenter {
    public static fsElement CURRENT_SELECTED_ELEMENT;
    fsElementActivity mContext;
    ArrayList<fsElement> elements;
    fsElementAdapter adapter;
    public int CATEGORIZE_ID = -1;
    public String CATEGORIZE_NAME = "";

    fsElementHandler elementHandler;

    public fsElementPresenter(fsElementActivity mContext) {
        this.mContext = mContext;
        initArguments();
        new InitGridView().execute();
    }

    private void initArguments() {
        Intent currentIntent = mContext.getIntent();
        elementHandler = new fsElementHandler(mContext);
        CATEGORIZE_ID = currentIntent.getIntExtra(ApplicationConfig.CategorizeAPI.ID, -1);
        CATEGORIZE_NAME = currentIntent.getStringExtra(ApplicationConfig.CategorizeAPI.Name);
    }


    class InitGridView extends AsyncTask<Void, Void, Void> {
        SweetAlertDialog mLoading;
        @Override
        protected void onPreExecute() {
            final InitGridView itselft = this;
            elements = new ArrayList<>();
            mLoading = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(mContext.getResources().getString(R.string.loading))
                    .setCancelButton(mContext.getResources().getString(R.string.cancle), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            if (itselft.cancel(true))
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
            elements = elementHandler.getEntriesByCategorizeID(CATEGORIZE_ID);
            adapter = new fsElementAdapter(mContext, elements);
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
                fsElement element = elements.get(position);
                CURRENT_SELECTED_ELEMENT = element;
                Intent catchedIntent = new Intent(mContext, fsCatchedInputActivity.class);
                mContext.startActivity(catchedIntent);
                mContext.finish();
            }
        });
        mContext.gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

                fsElement element = elements.get(position);
                if (element.getFeatureImage() != null)
                    imageView.setImageBitmap(element.getFeatureImage());
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
}
