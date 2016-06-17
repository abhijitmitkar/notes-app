package com.abhijitmitkar.realmdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Abhijit on 15-06-2016.
 */
public class AppUtils {

    public static void setUpToolbar(AppCompatActivity activity, Toolbar toolbar, boolean showBackArrow) {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            if(showBackArrow) actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void showSnackbar(View view, String text, String buttonText, View.OnClickListener onClickListener) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction(buttonText, onClickListener).show();
    }

    public static ProgressDialog createProgressDialog(Context context, String title, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        if (title != null) progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}
