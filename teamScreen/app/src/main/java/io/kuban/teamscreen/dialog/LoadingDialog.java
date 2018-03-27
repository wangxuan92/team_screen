package io.kuban.teamscreen.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.kuban.teamscreen.CustomerApplication;
import io.kuban.teamscreen.R;

/**
 * Created by zhanghuawei on 2017/9/8.
 */

public class LoadingDialog {

    private static Dialog DetemainDialog;
    private static AnimationDrawable animationDrawable;

    public static void showDialog(final Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            if (DetemainDialog == null) {
                DetemainDialog = new Dialog(activity, R.style.common_dialog);
                View v = LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                DetemainDialog.addContentView(v, params);
                Window window = DetemainDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.dimAmount = 0.1f;
                window.setAttributes(lp);
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                RelativeLayout rl_dialog = (RelativeLayout) v.findViewById(R.id.rl_dialog);
                ImageView imageView = (ImageView) v.findViewById(R.id.iv_progress_dialog);
                Glide.with(CustomerApplication.getContext()).load(R.drawable.loading).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
//                animationDrawable = (AnimationDrawable) imageView.getDrawable();
//                animationDrawable.start();
//                rl_dialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });

            }
            if (DetemainDialog != null && activity != null && !activity.isFinishing() && !DetemainDialog.isShowing()) {
                DetemainDialog.show();
                DetemainDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            return false;
                        }
                        return false;
                    }
                });
            }
        }
    }


    /**
     * 【关闭】
     */
    public static void dismissDialog() {
        if (DetemainDialog != null) {
            DetemainDialog.dismiss();
            DetemainDialog = null;
        }

        if (animationDrawable != null) {
            animationDrawable.stop();
            animationDrawable = null;
        }

    }


}
