package io.kuban.teamscreen.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dariopellegrini.formbuilder.FineTextView;

import io.kuban.teamscreen.R;

/**
 * Created by wangxuan on 16/11/19.
 */
public class CustomDialog extends Dialog {
    private EditText mEtPassword;
    private FineTextView negativeButton;
    private Context context;
    private int SHOW_ANOTHER_ACTIVITY = 0x1234;
    private boolean isShowing = true;
    private String mstPassword = "";


    public CustomDialog(Context context) {
        super(context, R.style.Dialog);
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        isShowing = true;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_normal_layout, null);
        mEtPassword = (EditText) mView.findViewById(R.id.et_password);
        this.setCancelable(false);// 设置点击屏幕Dialog不消失
        negativeButton = (FineTextView) mView.findViewById(R.id.negativeButton);
        resetTime();

        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉虚拟按键全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //让虚拟键盘一直不显示
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("============     ", "afterTextChanged");
                mstPassword = s.toString();
                resetTime();
            }
        });
        super.setContentView(mView);
    }


    @Override
    public void setContentView(int layoutResID) {
    }


    @Override
    public void setContentView(View view) {
    }

    public String getPassword() {
        return mstPassword;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        resetTime();
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 六秒没有操作关闭弹窗
     */
    private void resetTime() {
        // TODO Auto-generated method stub
        mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//從消息隊列中移除
        Message msg = mHandler.obtainMessage(SHOW_ANOTHER_ACTIVITY);
        mHandler.sendMessageDelayed(msg, 6000);//無操作5分钟后進入屏保
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == SHOW_ANOTHER_ACTIVITY) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive(mEtPassword)) {
                    imm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
                }
                Log.e("CustomDialog", "无操作后关闭");
                if (isShowing) {
                    dismiss();
                }
            }
        }
    };


    public void remove() {
        Log.e("CustomDialog", " 從消息隊列中移除");
        isShowing = false;
        mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//從消息隊列中移除
        dismiss();
    }


    /**
     * 登录键监听器
     *
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener) {
        negativeButton.setOnClickListener(listener);
    }
}