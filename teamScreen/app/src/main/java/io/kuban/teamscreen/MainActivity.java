package io.kuban.teamscreen;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.kuban.teamscreen.base.BaseCompatActivity;
import io.kuban.teamscreen.dialog.CustomDialog;
import io.kuban.teamscreen.manager.ActivityManager;
import io.kuban.teamscreen.model.PadsModel;
import io.kuban.teamscreen.model.SettingsModel;
import io.kuban.teamscreen.model.UserModel;
import io.kuban.teamscreen.service.AlwaysOnService.Bootstrap;
import io.kuban.teamscreen.utils.AESCipher;
import io.kuban.teamscreen.utils.ClickUtils;
import io.kuban.teamscreen.utils.ErrorUtil;
import io.kuban.teamscreen.utils.NetUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseCompatActivity {

    private static final String CONFIGURATION_MODEL = "SettingsModel";
    public static SettingsModel configurationModel;
    private CustomDialog dialog;

    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.image_view)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Bootstrap.startAlwaysOnService(this, "Main");
        CustomerApplication.token = AESCipher.decrypt(cache.getAsString(ActivityManager.TO_KEN));
        PadsModel padsModel = cache.getObject(ActivityManager.PADS_MODEL, PadsModel.class);

        if (null != padsModel) {
            CustomerApplication.spaceId = String.valueOf(padsModel.space_id);
            CustomerApplication.locationId = String.valueOf(padsModel.location_id);
        }
        updateButtonColor(mImageView, CustomerApplication.customColor, R.drawable.img_icon_back_pressed, R.drawable.img_icon_back);

        Log.e("===============   ", "  " + NetUtil.startPing("10.0.109.213"));
//
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetConnect) {
            getConfiguration();
        } else {
            configurationModel = cache.getObject(CONFIGURATION_MODEL, SettingsModel.class);
            loadingLogo();
        }
        CustomerApplication.appPassword = cache.getAsString(ActivityManager.APP_PASSWORD);
        Log.e(TAG, "退出密码  " + CustomerApplication.appPassword);
    }

    @OnClick({R.id.sign_in, R.id.sign_back, R.id.main_relative})
    void jump(View view) {
        switch (view.getId()) {
            case R.id.sign_in:

                if (null != MainActivity.configurationModel && null != MainActivity.configurationModel.config && null != MainActivity.configurationModel.config.signin) {
                    if (MainActivity.configurationModel.config.signin.visitor_types.size() > 0) {
                        ActivityManager.startVisitorTypesActivity(this, MainActivity.configurationModel.config.signin);
                    } else {
                        UserModel user = new UserModel();
                        user.userMap = new HashMap<>();
                        user.userMap.put("visitor_type", "");
                        ActivityManager.startValidationActivity(this, user);
                    }
                } else {
                    UserModel user = new UserModel();
                    user.userMap = new HashMap<>();
                    user.userMap.put("visitor_type", "");
                    ActivityManager.startValidationActivity(this, user);
                }
                break;
            case R.id.sign_back:
//                ActivityManager.startInformationInputActivity(this);
                break;
            case R.id.main_relative:
                new HideClick().start();
                if (HideClick.sIsAlive >= 5) {
                    dialog = new CustomDialog(MainActivity.this);
                    dialog.setOnNegativeListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.remove();
                            Log.e(TAG, "退出密码  " + CustomerApplication.appPassword + "   ==" + dialog.getPassword() + "==");
                            if (CustomerApplication.appPassword.equals(dialog.getPassword())) {
                                Bootstrap.stopAlwaysOnService(MainActivity.this);
                                AtyContainer.getInstance().finishAllActivity();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(0);//正常退出App
                                    }
                                }, 500);
                            } else {
                                Toast.makeText(MainActivity.this, CustomerApplication.getStringResources(R.string.password_mistake), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    if (ClickUtils.isFastClick()) {
                        dialog.show();
                    }
                }
                break;

        }
    }

    protected void getConfiguration() {
        Call<SettingsModel> createSessionCall = kuBanHttpClient.getKubanApi().getSettings();
        createSessionCall.enqueue(new Callback<SettingsModel>() {
            @Override
            public void onResponse(Call<SettingsModel> call, Response<SettingsModel> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    configurationModel = response.body();
                    cache.put(CONFIGURATION_MODEL, configurationModel);
                    loadingLogo();
                    refreshColor();
                } else {
                    ErrorUtil.handleError(MainActivity.this, response);
                }
            }

            @Override
            public void onFailure(Call<SettingsModel> call, Throwable t) {
                ErrorUtil.handleError(MainActivity.this, t);
            }
        });
    }

    public void loadingLogo() {
        if (null != configurationModel && null != configurationModel.config) {
            if (null != configurationModel.config.welcome && !TextUtils.isEmpty(configurationModel.config.welcome.image_url)) {
                Glide.with(CustomerApplication.getContext())
                        .load(configurationModel.config.welcome.image_url).placeholder(R.drawable.img_pic_logo).
                        into(logo);
            } else if (null != configurationModel.config.general) {
                Glide.with(CustomerApplication.getContext())
                        .load(configurationModel.config.general.logo).placeholder(R.drawable.img_pic_logo).
                        into(logo);
            }
        }
    }

    public void refreshColor() {
        String accentColor = "";
        if (null != configurationModel && null != configurationModel.config) {
            if (null != configurationModel.config.welcome && !TextUtils.isEmpty(configurationModel.config.welcome.accent_color)) {
                accentColor = configurationModel.config.welcome.accent_color;
            } else if (null != configurationModel.config.general && !TextUtils.isEmpty(configurationModel.config.general.accent_color)) {
                accentColor = configurationModel.config.general.accent_color;
            }
        }
        Log.e("================    ", accentColor + "  " + CustomerApplication.customColor);
        if (!TextUtils.isEmpty(accentColor) && !CustomerApplication.customColor.equals(accentColor)) {
            CustomerApplication.customColor = accentColor;
            cache.put(ActivityManager.CUSTOM_COLOR, CustomerApplication.customColor);
            CustomerApplication.primaryColor(CustomerApplication.customColor);
            recreate();
        }
    }

    static class HideClick extends Thread {
        public static volatile int sIsAlive = 1;

        @Override
        public void run() {
            sIsAlive++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (sIsAlive > 0) {
                sIsAlive--;
            }
            super.run();

        }
    }
}
