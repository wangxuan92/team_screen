package io.kuban.teamscreen.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.kuban.teamscreen.R;

/**
 * Created by wangxuan on 17/12/1.
 */

public class CustomKeyBoardUtils implements View.OnClickListener {

    private StringBuffer numberS;
    private TextView keyBoard1;
    private TextView keyBoard2;
    private TextView keyBoard3;
    private TextView keyBoard4;
    private TextView keyBoard5;
    private TextView keyBoard6;
    private TextView keyBoard7;
    private TextView keyBoard8;
    private TextView keyBoard9;
    private TextView keyBoard0;
    private TextView txEmpty;
    private TextView txConfirm;
    private ImageView deleteIcon;
    private int maxLength = 0;
    private OnKeyBoardInputListener onKeyBoardInputListener;

    private static CustomKeyBoardUtils customKeyBoardUtils;

    private CustomKeyBoardUtils() {
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * 需要设置view
     */
    public void setView(View view, OnKeyBoardInputListener onKeyBoardInputListener) {
        this.onKeyBoardInputListener = onKeyBoardInputListener;
        numberS = new StringBuffer();
        keyBoard1 = view.findViewById(R.id.key_board_1);
        keyBoard2 = view.findViewById(R.id.key_board_2);
        keyBoard3 = view.findViewById(R.id.key_board_3);
        keyBoard4 = view.findViewById(R.id.key_board_4);
        keyBoard5 = view.findViewById(R.id.key_board_5);
        keyBoard6 = view.findViewById(R.id.key_board_6);
        keyBoard7 = view.findViewById(R.id.key_board_7);
        keyBoard8 = view.findViewById(R.id.key_board_8);
        keyBoard9 = view.findViewById(R.id.key_board_9);
        keyBoard0 = view.findViewById(R.id.key_board_0);
        deleteIcon = view.findViewById(R.id.delete_icon);
        txEmpty = view.findViewById(R.id.tx_empty);
        txConfirm = view.findViewById(R.id.tx_confirm);

        keyBoard1.setOnClickListener(this);
        keyBoard2.setOnClickListener(this);
        keyBoard3.setOnClickListener(this);
        keyBoard4.setOnClickListener(this);
        keyBoard5.setOnClickListener(this);
        keyBoard6.setOnClickListener(this);
        keyBoard7.setOnClickListener(this);
        keyBoard8.setOnClickListener(this);
        keyBoard9.setOnClickListener(this);
        keyBoard0.setOnClickListener(this);
        deleteIcon.setOnClickListener(this);
        txEmpty.setOnClickListener(this);
        txConfirm.setOnClickListener(this);
    }

    public static CustomKeyBoardUtils getInstance() {
        if (customKeyBoardUtils == null) {
            customKeyBoardUtils = new CustomKeyBoardUtils();
        }
        return customKeyBoardUtils;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.key_board_1:
                numberS.append("1");
                break;
            case R.id.key_board_2:
                numberS.append("2");
                break;
            case R.id.key_board_3:
                numberS.append("3");
                break;
            case R.id.key_board_4:
                numberS.append("4");
                break;
            case R.id.key_board_5:
                numberS.append("5");
                break;
            case R.id.key_board_6:
                numberS.append("6");
                break;
            case R.id.key_board_7:
                numberS.append("7");
                break;
            case R.id.key_board_8:
                numberS.append("8");
                break;
            case R.id.key_board_9:
                numberS.append("9");
                break;
            case R.id.key_board_0:
                numberS.append("0");
                break;
            case R.id.delete_icon:
                if (numberS.length() > 0) {
                    numberS.deleteCharAt((numberS.length() - 1));
                }
                break;
            case R.id.tx_empty:
                if (null == numberS) {
                    numberS = null;
                }
                numberS = new StringBuffer();
                break;
            case R.id.tx_confirm:
                if (null != onKeyBoardInputListener) {
                    onKeyBoardInputListener.OnConfirmListener(numberS.toString());
                }
                return;
        }
        if (null != onKeyBoardInputListener) {
            onKeyBoardInputListener.OnInputListener(numberS.toString());
        }
        if (maxLength > 0 && numberS.length() > maxLength) {
            numberS.deleteCharAt((numberS.length() - 1));
        }
        Log.e("==================    ", "  " + numberS.toString());
    }

    public interface OnKeyBoardInputListener {
        void OnInputListener(String string);

        void OnConfirmListener(String string);
    }
}
