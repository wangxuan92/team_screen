package io.kuban.teamscreen.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.kuban.teamscreen.CustomerApplication;
import io.kuban.teamscreen.R;
import io.kuban.teamscreen.base.BaseCompatActivity;
import io.kuban.teamscreen.event.BindingResultsEvent;
import io.kuban.teamscreen.manager.ActivityManager;
import io.kuban.teamscreen.model.PadsModel;
import io.kuban.teamscreen.model.ToKenModel;
import io.kuban.teamscreen.utils.AESCipher;
import io.kuban.teamscreen.utils.EquipmentInformationUtil;
import io.kuban.teamscreen.utils.ErrorUtil;
import io.kuban.teamscreen.utils.StringUtil;
import io.kuban.teamscreen.view.LetterSpacingTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 绑定界面
 * Created by wangxuan on 17/11/15.
 */

public class BindingActivity extends BaseCompatActivity {
    @BindView(R.id.validate_code)
    LetterSpacingTextView mValidateCode;
    @BindView(R.id.refresh)
    Button refresh;

    private int validateCode;
    private CountDownTimer timer;
    private PadsModel padsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binding_activity);
        ButterKnife.bind(this);
        CustomerApplication.token = AESCipher.decrypt(cache.getAsString(ActivityManager.TO_KEN));
        PadsModel padsModel = cache.getObject(ActivityManager.PADS_MODEL, PadsModel.class);

        CustomerApplication.appPassword = cache.getAsString(ActivityManager.APP_PASSWORD);
        if (TextUtils.isEmpty(CustomerApplication.appPassword)) {
            CustomerApplication.appPassword = "123456";
            cache.put(ActivityManager.APP_PASSWORD, CustomerApplication.appPassword);
        }

        if (null != padsModel && null != padsModel.meeting_screen) {
            CustomerApplication.spaceId = String.valueOf(padsModel.space_id);
            CustomerApplication.locationId = String.valueOf(padsModel.location_id);
            ActivityManager.startMainActivity(BindingActivity.this);
            finish();
        } else {
            generateCode();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.refresh)
    public void onClick(View view) {
        generateCode();
    }

    public void generateCode() {
        refresh.setEnabled(false);
        validateCode = (int) ((Math.random() * 9 + 1) * 100000);
        mValidateCode.setText(String.valueOf(validateCode));
        cache.remove(ActivityManager.TO_KEN);
        postRegister();
        long millisInFuture = 120000;
        if (null == timer) {
            timer = new CountDownTimer(millisInFuture, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    refresh.setText(StringUtil.getString(R.string.again_generate_code_hint, (millisUntilFinished / 1000)));
                    getPads();
                }

                @Override
                public void onFinish() {
                    initButton();
                }
            };
            timer.start();
        } else {
            timer.cancel();
            timer.start();
        }
    }

    protected void initButton() {
        timer.cancel();
        refresh.setEnabled(true);
        refresh.setText(CustomerApplication.getStringResources(R.string.again_generate_code));
    }

    protected void postRegister() {
        Map<String, String> queries = new HashMap<>();
        queries.put("code", String.valueOf(validateCode));
        queries.put("device_id", EquipmentInformationUtil.getDeviceId(BindingActivity.this));
        queries.put("model", EquipmentInformationUtil.getDeviceInformation(EquipmentInformationUtil.MANUFACTURER) + " " + EquipmentInformationUtil.getDeviceInformation(EquipmentInformationUtil.MODEL));
        queries.put("app_version", EquipmentInformationUtil.getVersionName(BindingActivity.this));
        queries.put("os_version", EquipmentInformationUtil.getDeviceInformation(EquipmentInformationUtil.RELEASE));
        queries.put("os", "android");
        queries.put("apptype", "office");
        queries.put("subtype", "office_display");
        queries.put("screen_size", EquipmentInformationUtil.getScreenSize(this));
        Call<ToKenModel> createSessionCall = kuBanHttpClient.getKubanApi().postRegister(queries);
        createSessionCall.enqueue(new Callback<ToKenModel>() {
            @Override
            public void onResponse(Call<ToKenModel> call, Response<ToKenModel> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    ToKenModel toKenModel = response.body();
                    String tpken = AESCipher.encrypt(toKenModel.token);
                    cache.put(ActivityManager.TO_KEN, tpken);
                    CustomerApplication.token = AESCipher.decrypt(cache.getAsString(ActivityManager.TO_KEN));
                } else {
//                    ErrorUtil.handleError(BindingActivity.this, response);
                }
            }

            @Override
            public void onFailure(Call<ToKenModel> call, Throwable t) {
                ErrorUtil.handleError(BindingActivity.this, t);
            }
        });
    }

    protected void getPads() {
        Call<PadsModel> createSessionCall = kuBanHttpClient.getKubanApi().getPads("1");
        createSessionCall.enqueue(new Callback<PadsModel>() {
            @Override
            public void onResponse(Call<PadsModel> call, Response<PadsModel> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    padsModel = response.body();
                    if (null != padsModel && null != padsModel.meeting_screen) {
                        initButton();
                        CustomerApplication.spaceId = String.valueOf(padsModel.space_id);
                        if (!TextUtils.isEmpty(padsModel.passcode)) {
                            CustomerApplication.appPassword = padsModel.passcode;
                            cache.put(ActivityManager.APP_PASSWORD, CustomerApplication.appPassword);
                        }
                        ActivityManager.startBindingSuccessfulActivity(BindingActivity.this, padsModel);
                    }
                }
            }

            @Override
            public void onFailure(Call<PadsModel> call, Throwable t) {
            }
        });
    }

    @Subscribe
    public void scanNetworkRequestStatusEvent(BindingResultsEvent bindingResultsEvent) {
        if (bindingResultsEvent.isResults()) {
            cache.put(ActivityManager.PADS_MODEL, padsModel);
            ActivityManager.startMainActivity(BindingActivity.this);
            finish();
        } else {
//            generateCode();
        }
    }

}