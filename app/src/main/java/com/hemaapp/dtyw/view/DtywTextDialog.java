package com.hemaapp.dtyw.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.dtyw.myapplication.R;

import xtom.frame.XtomObject;

/**
 * Created by lenovo on 2016/9/6.
 */
public class DtywTextDialog extends XtomObject {

    private Dialog mDialog;
    private TextView mTextView;

    private Runnable cancelRunnable = new Runnable() {

        @Override
        public void run() {
            cancel();
        }
    };

    public DtywTextDialog(Context context) {
        mDialog = new Dialog(context, R.style.toast);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_text, null);
        mTextView = (TextView) view.findViewById(R.id.textview);
        mDialog.setCancelable(true);
        mDialog.setContentView(view);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                mTextView.removeCallbacks(cancelRunnable);
            }
        });
        mDialog.show();
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setText(int textID) {
        mTextView.setText(textID);
    }

    public void show() {
        mDialog.show();
        mTextView.postDelayed(cancelRunnable, 2000);
    }

    public void cancel() {
        mDialog.cancel();
    }


}
