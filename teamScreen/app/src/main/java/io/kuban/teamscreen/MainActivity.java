package io.kuban.teamscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.kuban.teamscreen.base.BaseCompatActivity;
import io.kuban.teamscreen.dialog.CustomDialog;
import io.kuban.teamscreen.manager.ActivityManager;
import io.kuban.teamscreen.model.AreasModel;
import io.kuban.teamscreen.model.OrganizationsModel;
import io.kuban.teamscreen.model.PadsModel;
import io.kuban.teamscreen.service.AlwaysOnService.Bootstrap;
import io.kuban.teamscreen.utils.AESCipher;
import io.kuban.teamscreen.utils.ClickUtils;
import io.kuban.teamscreen.utils.NetUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseCompatActivity {


    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.area_name)
    TextView areaName;
    @BindView(R.id.team_name)
    TextView teamName;
    @BindView(R.id.team_logo)
    ImageView teamLogo;

    private CustomDialog dialog;
    private PadsModel padsModel;
    private AreasModel areasModel;
    private int TIME = 3600 * 1000;  //每隔1小时执行一次.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Bootstrap.startAlwaysOnService(this, "Main");
        CustomerApplication.token = AESCipher.decrypt(cache.getAsString(ActivityManager.TO_KEN));
        PadsModel padsModel = cache.getObject(ActivityManager.PADS_MODEL, PadsModel.class);
        getPads();
        if (null != padsModel) {
            CustomerApplication.spaceId = String.valueOf(padsModel.space_id);
            CustomerApplication.locationId = String.valueOf(padsModel.location_id);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
//        registerReceiver(receiver, filter);
        teamName.setText(CustomerApplication.getStringResources(R.string.no_come));
        Glide.with(CustomerApplication.getContext())
                .load(R.drawable.img_icon_wuren).into(teamLogo);
        Log.e("===============   ", "  " + NetUtil.startPing("10.0.109.213"));
        handler.postDelayed(runnable, TIME); // 在初始化方法里.
    }

    @OnClick(R.id.logo)
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.logo:
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

    protected void initUi(PadsModel padsModel, AreasModel areasModel) {
        if (null != padsModel) {
            if (!TextUtils.isEmpty(padsModel.passcode)) {
                CustomerApplication.appPassword = padsModel.passcode;
                cache.put(ActivityManager.APP_PASSWORD, CustomerApplication.appPassword);
            }
            if (null != areasModel) {
                areaName.setText(areasModel.name);
            } else {
                areaName.setText(padsModel.name);
            }
            if (null != areasModel && null != areasModel.organizations && areasModel.organizations.size() > 0) {
                OrganizationsModel organizationsModel = areasModel.organizations.get(0);
                teamName.setText(organizationsModel.name);
                if (!TextUtils.isEmpty(organizationsModel.logo)) {
                    Glide.with(CustomerApplication.getContext())
                            .load(organizationsModel.logo).error(R.drawable.img_icon_wuren).into(teamLogo);
                } else {
                    Glide.with(CustomerApplication.getContext())
                            .load(R.drawable.img_icon_wuren).into(teamLogo);
                }
            } else {
                teamName.setText(CustomerApplication.getStringResources(R.string.no_come));
                Glide.with(CustomerApplication.getContext())
                        .load(R.drawable.img_icon_wuren).into(teamLogo);
            }
        }

    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {

            }
        }
    };


    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, TIME);
                getPads();
                Log.e("print", "1-------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    protected void getPads() {
        Call<PadsModel> createSessionCall = kuBanHttpClient.getKubanApi().getPads("1");
        createSessionCall.enqueue(new Callback<PadsModel>() {
            @Override
            public void onResponse(Call<PadsModel> call, Response<PadsModel> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    padsModel = response.body();
                    if (null != padsModel && null != padsModel.meeting_screen) {
                        getAreas(padsModel.area_id);
                    } else {
                        ActivityManager.startInActivity(activity);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<PadsModel> call, Throwable t) {
            }
        });
    }

    protected void getAreas(String areaId) {
        Call<AreasModel> createSessionCall = kuBanHttpClient.getKubanApi().getAreas(areaId, "organizations");
        createSessionCall.enqueue(new Callback<AreasModel>() {
            @Override
            public void onResponse(Call<AreasModel> call, Response<AreasModel> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    areasModel = response.body();
                    if (null != padsModel && null != padsModel.meeting_screen) {
                        initUi(padsModel, areasModel);
                    }
                }
            }

            @Override
            public void onFailure(Call<AreasModel> call, Throwable t) {
            }
        });
    }

    static class HideClick extends Thread {
        public static volatile int sIsAlive = 1;

        @Override
        public void run() {
            sIsAlive++;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (sIsAlive > 0) {
                sIsAlive--;
            }
            super.run();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(receiver);
    }
}