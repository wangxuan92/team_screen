package io.kuban.teamscreen.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import io.kuban.teamscreen.R;

/**
 * Created by wangxuan on 2016/10/01.
 */
public class ValidationFailsDialog extends Dialog {
    private TextView noInviteCode;

    public ValidationFailsDialog(Context context) {
        super(context, R.style.Dialog);
        setCustomDialog();
    }


    private void setCustomDialog() {
        this.setCancelable(true);// 设置点击屏幕Dialog不消失
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.validation_fails_popwindow, null);
        noInviteCode = (TextView) mView.findViewById(R.id.no_invite_code);
        TextView returnText = (TextView) mView.findViewById(R.id.return_string);
        returnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        super.setContentView(mView);
    }


    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     *
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        //设置按钮监听
        noInviteCode.setOnClickListener(listener);
    }
}
