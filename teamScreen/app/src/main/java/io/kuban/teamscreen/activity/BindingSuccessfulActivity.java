package io.kuban.teamscreen.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.yanzhenjie.permission.AndPermission;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.kuban.teamscreen.R;
import io.kuban.teamscreen.base.BaseCompatActivity;
import io.kuban.teamscreen.event.BindingResultsEvent;
import io.kuban.teamscreen.manager.ActivityManager;
import io.kuban.teamscreen.model.PadsModel;
import io.kuban.teamscreen.utils.AnimatorUtils;

/**
 * 绑定成功
 * Created by wangxuan on 17/11/21.
 */

public class BindingSuccessfulActivity extends BaseCompatActivity {
    @BindView(R.id.company_name)
    TextView mCompanyName;
    @BindView(R.id.location_name)
    TextView mLocationName;
    @BindView(R.id.animation_view_click)
    LottieAnimationView mAnimationView;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.headings)
    TextView mHeadings;

    private PadsModel padsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binding_successful_activity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        requestAppPermissions();
        mTitle.setText(R.string.binding_successful);
        mHeadings.setText(R.string.binding_successful_hint);
        if (null != intent && null != intent.getExtras()) {
            padsModel = (PadsModel) intent.getExtras().getSerializable(ActivityManager.PADS_MODEL);
        }
        if (null != padsModel) {
            if (null != padsModel.space) {
                mCompanyName.setText(padsModel.space.name);
            }
            if (null != padsModel.location) {
                mLocationName.setText(padsModel.location.name);
            }
        }
        AnimatorUtils.loading(this, mAnimationView, "data.json", false);
//        mAnimationView.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.e("===================    ", " 动画进度" + (int) (animation.getAnimatedFraction() * 100) + "%");
//            }
//        });
    }

    private void requestAppPermissions() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .send();
    }

    @OnClick({R.id.open_visitors_system, R.id.again_match})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_visitors_system:
                EventBus.getDefault().post(new BindingResultsEvent(true));
                break;
            case R.id.again_match:
                EventBus.getDefault().post(new BindingResultsEvent(false));
                break;
        }
        finish();
    }
}
