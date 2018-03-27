package io.kuban.teamscreen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dariopellegrini.formbuilder.FineTextView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.kuban.teamscreen.R;
import io.kuban.teamscreen.adapter.VisitorAdapter;
import io.kuban.teamscreen.base.SwipeBackActivity;
import io.kuban.teamscreen.event.ReturnHomePageEvent;
import io.kuban.teamscreen.manager.ActivityManager;
import io.kuban.teamscreen.model.SigninModel;
import io.kuban.teamscreen.model.UserModel;
import io.kuban.teamscreen.model.VisitorTypesModel;

/**
 * 访客类型选择页
 * Created by wangxuan on 17/11/9.
 */

public class VisitorTypesActivity extends SwipeBackActivity {

    @BindView(R.id.image_return)
    LinearLayout imageReturn;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.next_step_button)
    FineTextView nextStepButton;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.headings)
    TextView mHeadings;
    private SigninModel signinModel;
    private int selected = -1;
    private VisitorAdapter adapter;
    private List<VisitorTypesModel> visitorTypes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitor_types_activity);
        ButterKnife.bind(this);
        finish(imageReturn);
        visitorTypes = new ArrayList<>();
        Intent intent = getIntent();
        if (null != intent && null != intent.getExtras()) {
            signinModel = (SigninModel) intent.getExtras().getSerializable(ActivityManager.SIGNIN_MODEL);
        }
        mTitle.setText(R.string.visitors_choose_type);
        mHeadings.setText(R.string.visitors_choose_type1);
        for (VisitorTypesModel visitorTypesModel : signinModel.visitor_types) {
            if (visitorTypesModel.enabled) {
                visitorTypes.add(visitorTypesModel);
            }
        }
        if (visitorTypes.size() == 1) {
            selected = 0;
            visitorTypes.get(0).isSelected = true;
            startValidationActivity();
            finish();
        } else if (visitorTypes.size() > 1) {
            selected = 0;
            visitorTypes.get(0).isSelected = true;
        }
        adapter = new VisitorAdapter(this);
        listview.setAdapter(adapter);

        adapter.onlyAddAll(visitorTypes);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                for (VisitorTypesModel vm : visitorTypes) {
                    vm.isSelected = false;
                }
                visitorTypes.get(position).isSelected = true;
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.next_step_button, R.id.image_home})
    public void nextStep(View view) {
        switch (view.getId()) {
            case R.id.image_home:
                returnHomePage();
                break;
            case R.id.next_step_button:
                startValidationActivity();
                break;
        }

    }

    public void startValidationActivity() {
        if (selected >= 0) {
            VisitorTypesModel visitor = visitorTypes.get(selected);
            cache.put(ActivityManager.VISITOR_TYPES_MODEL, visitor);
            UserModel user = new UserModel();
            user.userMap = new HashMap<>();
            user.userMap.put(ActivityManager.VISITOR_TYPE, visitor.name);
            ActivityManager.startValidationActivity(VisitorTypesActivity.this, user);
        }
    }

    @Subscribe
    public void scanReturnHomePageEvent(ReturnHomePageEvent returnHomePageEvent) {
        if (returnHomePageEvent.isReturnHomePage()) {
            finish();
        }
    }
}
