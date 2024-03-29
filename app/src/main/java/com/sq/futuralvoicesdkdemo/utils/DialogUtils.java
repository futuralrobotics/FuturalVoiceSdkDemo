package com.sq.futuralvoicesdkdemo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.sq.futuralvoicesdkdemo.R;

public class DialogUtils {
    public interface Callback {
        default void ok(){};
        default void cancel(){};
    }

    private static Dialog dialog;

    public static Dialog showOneBtnDlg(Activity context, @StringRes int titleResId,
                                       @StringRes int contentResId, Callback callback){
        return showOneBtnDlg(context, context.getString(titleResId), context.getString(contentResId), Gravity.CENTER, android.R.string.ok, callback);
    }

    public static Dialog showOneBtnDlg(Activity context, @StringRes int titleResId,
                                       @StringRes int contentResId, int gravity, @StringRes int btnTextResId, Callback callback){
        return showOneBtnDlg(context, context.getString(titleResId), context.getString(contentResId), gravity, btnTextResId, callback);
    }

    public static Dialog showOneBtnDlg(Activity context, String title,
                                       String content, @StringRes int btnTextResId, Callback callback){
        return showOneBtnDlg(context, title, content, Gravity.CENTER, btnTextResId, callback);
    }

    public static Dialog showOneBtnDlg(Activity context, String title,
                                       String content, int gravity, @StringRes int btnTextResId, Callback callback) {
        return showCommonDlg(context, title, content, gravity, btnTextResId, callback);
    }

    public static Dialog showCommonDlg(Activity context, String title, String content, int gravity, @StringRes int btnTextResId, Callback callback) {
        return showIKnownDlg(context, title, content, gravity, context.getString(btnTextResId), callback);
    }

    public static Dialog showIKnownDlg(Activity context, String titleStr,
                                       String contentStr, int gravity, String btnText, Callback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.common_dialog, null);
        TextView title = v.findViewById(R.id.dialog_title);
        title.setText(titleStr);
        TextView content = v.findViewById(R.id.dialog_content);
        content.setGravity(gravity);
        content.setText(contentStr);
        v.findViewById(R.id.dialog_btn_sure).setVisibility(View.GONE);
        TextView button = v.findViewById(R.id.dialog_btn_cancel);
        button.setText(btnText);
        builder.setCancelable(false);
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
        dialog =builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setContentView(v);
        button.setOnClickListener((btn)->{
            dialog.dismiss();
            if (callback != null){
                callback.ok();
            }
        });
        return dialog;
    }
}
