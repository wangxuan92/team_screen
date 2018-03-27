package io.kuban.teamscreen.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.kuban.teamscreen.CustomerApplication;
import io.kuban.teamscreen.R;
import io.kuban.teamscreen.base.SwipeBackActivity;
import io.kuban.teamscreen.dialog.ValidationFailsDialog;
import io.kuban.teamscreen.event.ReturnHomePageEvent;
import io.kuban.teamscreen.manager.ActivityManager;
import io.kuban.teamscreen.model.UserModel;
import io.kuban.teamscreen.model.VisitsModel;
import io.kuban.teamscreen.utils.AnimatorUtils;
import io.kuban.teamscreen.utils.ErrorUtil;
import io.kuban.teamscreen.utils.JitterAnimator;
import io.kuban.teamscreen.utils.JsonUtils;
import io.kuban.teamscreen.utils.StringUtil;
import io.kuban.teamscreen.utils.TimeUtils;
import io.kuban.teamscreen.view.CustomKeyBoardUtils;
import io.kuban.teamscreen.view.LetterSpacingTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 验证访客页
 */
public class ValidationActivity extends SwipeBackActivity implements CustomKeyBoardUtils.OnKeyBoardInputListener {
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.image_return)
    LinearLayout imageReturn;
    @BindView(R.id.error_message)
    TextView mErrorMessage;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.headings)
    TextView mHeadings;

    @BindView(R.id.animation_view_click)
    LottieAnimationView mAnimationView;
    @BindView(R.id.delete_icon)
    ImageView deleteIcon;
    @BindView(R.id.image_view)
    ImageView mImageView;
    @BindView(R.id.key_board)
    LinearLayout keyBoard;
    @BindView(R.id.text_view)
    LetterSpacingTextView textView;
    private ValidationFailsDialog dialog;
    private UserModel user;
    private Drawable redLine;
    private Drawable textLine;
    private CustomKeyBoardUtils customKeyBoardUtils;
    private String substitute = "*";
    private int maxLength = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validation_activity_1);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        customKeyBoardUtils = CustomKeyBoardUtils.getInstance();
        customKeyBoardUtils.setView(keyBoard, this);
        customKeyBoardUtils.setMaxLength(maxLength);
        redLine = getResources().getDrawable(R.drawable.edit_text_red_line);
        textLine = getResources().getDrawable(R.drawable.edit_text_line);
        redLine.setBounds(0, 0, redLine.getMinimumWidth(), redLine.getMinimumHeight());
        textLine.setBounds(0, 0, textLine.getMinimumWidth(), textLine.getMinimumHeight());
        mTitle.setText(R.string.invite_code_prompt);
        mHeadings.setText(R.string.invite_code_prompt_2);
        updateButtonColor(deleteIcon, CustomerApplication.customColor, R.drawable.bg_key_board_delete_pressed, R.drawable.bg_key_board_delete_default);
        updateButtonColor(mImageView, CustomerApplication.customColor, R.drawable.img_icon_eye);
        if (null != intent && null != intent.getExtras()) {
            user = (UserModel) intent.getExtras().getSerializable(ActivityManager.USER_MODEL);
        }
        if (null == user) {
            user = new UserModel();
            user.userMap = new HashMap<>();
        }
        finish(imageReturn);
        hiddenKeyboard(mRelativeLayout);
        textView.setHighlighted(substitute, getResources().getColor(R.color.text_color_blue_de));
        textView.setText(StringUtil.formatStr("", maxLength, substitute));
    }

    @OnClick({R.id.dont_have_invite_code, R.id.image_home})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_home:
                returnHomePage();
                break;
            case R.id.dont_have_invite_code:
                ActivityManager.startInformationInputActivity(this);
                break;
        }
    }

    @Subscribe
    public void scanReturnHomePageEvent(ReturnHomePageEvent returnHomePageEvent) {
        if (returnHomePageEvent.isReturnHomePage()) {
            finish();
        }
    }

    private void putArrivedConfirm(String visits_code) {
        if (visits_code.contains(substitute)) {
//            jitter(CustomerApplication.getStringResources(R.string.invite_code_prompt));
//            Toast.makeText(this, CustomerApplication.getStringResources(R.string.invite_code_prompt), Toast.LENGTH_SHORT).show();
//            dialog();
            return;
        }
        mAnimationView.setVisibility(View.VISIBLE);
        AnimatorUtils.loading(this, mAnimationView, "loading.json", true);

        if (isNetConnect) {
            Call<List<VisitsModel>> createSessionCall = kuBanHttpClient.getKubanApi().getArrivedConfirm(CustomerApplication.locationId, visits_code);//8356
            createSessionCall.enqueue(new Callback<List<VisitsModel>>() {
                @Override
                public void onResponse(Call<List<VisitsModel>> call, Response<List<VisitsModel>> response) {
                    if (response.isSuccessful()) {
                        List<VisitsModel> visitsModelss = response.body();
                        if (null != visitsModelss && visitsModelss.size() > 0) {
                            VisitsModel visitsModels = visitsModelss.get(0);
                            Map<String, Object> map = JsonUtils.toMapObject(JsonUtils.objectToJson(visitsModels));
                            user.userMap.putAll(map);
                            user.userMap.put(ActivityManager.EXPECT_ARRIVAL_DATE, TimeUtils.getStrDate(new Date(), "yyyy-MM-dd"));
                            user.visitorsId = visitsModels.id;
                            user.isSignin = true;
                            AnimatorUtils.loading(ValidationActivity.this, mAnimationView, "successful.json", false);
                            if (null != visitsModelss && visitsModelss.size() > 0) {
                                ActivityManager.startTakingPicturesActivity(ValidationActivity.this, user);
                                finish();
                            }
                        } else {
                            jitter(CustomerApplication.getStringResources(R.string.validation_fails_popwindow_text));
//                            dialog();
                        }
                    } else {
                        ErrorUtil.handleError(ValidationActivity.this, response);
                    }
                }

                @Override
                public void onFailure(Call<List<VisitsModel>> call, Throwable t) {
                    Log.e(TAG, "Throwable  " + t);
                    ErrorUtil.handleError(ValidationActivity.this, t);
                }
            });
        } else {
            ActivityManager.startInformationInputActivity(ValidationActivity.this);
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 弹窗
    private void dialog() {
        dialog = new ValidationFailsDialog(this);
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                ActivityManager.startInformationInputActivity(ValidationActivity.this);
            }
        });
        dialog.show();
    }

    private void jitter(String errorMessage) {
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText(errorMessage);
        mAnimationView.setVisibility(View.INVISIBLE);
//        editText.setCompoundDrawables(null, null, null, redLine);
        textView.setTextColor(getResources().getColor(R.color.error_message_color));
        ObjectAnimator animator = JitterAnimator.tada(textView);
        animator.setRepeatCount(0);
        animator.start();
        ObjectAnimator nopeAnimator = JitterAnimator.nope(textView);
        nopeAnimator.setRepeatCount(0);
        nopeAnimator.start();
    }

    @Override
    public void OnInputListener(String string) {
        submit(string);
    }

    @Override
    public void OnConfirmListener(String string) {
        submit(string);
    }

    public void submit(String string) {
        textView.setText(StringUtil.formatStr(string, maxLength, substitute));
        textView.setTextColor(getResources().getColor(R.color.text_color_green));
        mErrorMessage.setVisibility(View.INVISIBLE);
        String visits_code = textView.getText().toString().replaceAll("\\s", "");
        if (visits_code.length() > 3) {
            putArrivedConfirm(visits_code);
        }
    }
}
