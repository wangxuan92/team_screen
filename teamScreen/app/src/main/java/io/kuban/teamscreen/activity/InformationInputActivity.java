package io.kuban.teamscreen.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dariopellegrini.formbuilder.FineEditText;
import com.dariopellegrini.formbuilder.FormBuilder;
import com.dariopellegrini.formbuilder.FormButton;
import com.dariopellegrini.formbuilder.FormElement;
import com.dariopellegrini.formbuilder.FormObject;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
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
import io.kuban.teamscreen.event.ReturnHomePageEvent;
import io.kuban.teamscreen.manager.ActivityManager;
import io.kuban.teamscreen.model.FieldsModel;
import io.kuban.teamscreen.model.UserModel;
import io.kuban.teamscreen.model.VisitorTypesModel;
import io.kuban.teamscreen.utils.ScreenUtil;
import io.kuban.teamscreen.utils.TimeUtils;

/**
 * 信息录入页
 */
public class InformationInputActivity extends SwipeBackActivity {
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.image_return)
    LinearLayout imageReturn;
    @BindView(R.id.linearLayout)
    LinearLayout mLinearLayout;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.headings)
    TextView mHeadings;
    private FormBuilder formBuilder;
    private List<FormObject> formObjects;
    private VisitorTypesModel visitorTypesModel;
    private UserModel userModel;
    private boolean isJump = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_input_activity);
        ButterKnife.bind(this);
        finish(imageReturn);
        mTitle.setText(R.string.temporary_visitor);
        mHeadings.setText(R.string.temporary_visitor_1);
        formBuilder = new FormBuilder(this, mLinearLayout);
        formObjects = new ArrayList<>();
        userModel = new UserModel();
        userModel.userMap = new HashMap<>();
        visitorTypesModel = cache.getObject(ActivityManager.VISITOR_TYPES_MODEL, VisitorTypesModel.class);
        hiddenKeyboard(mRelativeLayout);
        init();
    }

    private void init1() {
        if (null != visitorTypesModel) {
            visitorTypesModel.fields.clear();
        } else {
            visitorTypesModel = new VisitorTypesModel();
            visitorTypesModel.fields = new ArrayList<>();
        }
        for (int i = 0; i < 5; i++) {
            FieldsModel fm = new FieldsModel();
            fm.custom = false;
            fm.deletable = false;
            fm.modifable = false;
            fm.required = false;
            fm.options = new ArrayList<>();
            if (i == 0) {
                fm.field_name = "name";
                fm.field_type = "text";
                fm.required = false;
                fm.name = "您的姓名";
            }
            if (i == 1) {
                fm.field_name = "email";
                fm.field_type = "email";
                fm.name = "您的email";
                fm.required = true;
            }

            if (i == 2) {
                fm.field_name = "phone_num";
                fm.field_type = "phone";
                fm.required = true;
                fm.name = "您的phone";
            }

            if (i == 3) {
                fm.field_name = "integer";
                fm.field_type = "integer";
                fm.required = false;
                fm.name = "您的integer";
            }

            if (i == 4) {
                fm.field_name = "options";
                fm.field_type = "options";
                fm.name = "您的options";
                fm.required = true;
                fm.options.add("123");
                fm.options.add("234");
                fm.options.add("345");
            }
            visitorTypesModel.fields.add(fm);
        }
    }

    public static <T> String mapToJson(Map<String, T> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }

    private void init() {
//        init1();
        LinearLayout.LayoutParams fieldLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fieldLayoutParams.setMargins(8, 8, 8, ScreenUtil.px2dip(getResources().getDimension(R.dimen.dp_22)));
        if (null != visitorTypesModel && null != visitorTypesModel.fields) {
            for (FieldsModel fieldsModel : visitorTypesModel.fields) {
                if (null != fieldsModel) {
                    FormElement.Type type = FormElement.Type.TEXT;
                    if (fieldsModel.field_type.equals("text")) {
                        type = FormElement.Type.TEXT;
                    } else if (fieldsModel.field_type.equals("email")) {
                        type = FormElement.Type.EMAIL;
                    } else if (fieldsModel.field_type.equals("phone")) {
                        type = FormElement.Type.PHONE;
                    } else if (fieldsModel.field_type.equals("integer")) {
                        type = FormElement.Type.PHONE;
                    } else if (fieldsModel.field_type.equals("options")) {
                        type = FormElement.Type.SELECTION;
                    }
                    if (null != fieldsModel.options && fieldsModel.options.size() > 0) {
                        formObjects.add(new FormElement().setTag(fieldsModel.field_name).setHint(fieldsModel.name).setType(type).setParams(fieldLayoutParams).setTextSize(getResources().getDimension(R.dimen.dp_18)).setRequired(fieldsModel.required).setBackgroundDrawable(getResources().getDrawable(R.drawable.line_selector)).setCustomColor(CustomerApplication.customColor).setOptions(fieldsModel.options));
                    } else {
                        formObjects.add(new FormElement().setTag(fieldsModel.field_name).setHint(fieldsModel.name).setType(type).setParams(fieldLayoutParams).setTextSize(getResources().getDimension(R.dimen.dp_18)).setRequired(fieldsModel.required).setBackgroundDrawable(getResources().getDrawable(R.drawable.line_selector)).setCustomColor(CustomerApplication.customColor));
                    }
                }
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.px2dip(getResources().getDimension(R.dimen.dp_386)), ScreenUtil.px2dip(getResources().getDimension(R.dimen.dp_92)));
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(0, ScreenUtil.px2dip(getResources().getDimension(R.dimen.dp_98)), 0, ScreenUtil.px2dip(getResources().getDimension(R.dimen.dp_52)));
            formObjects.add(new FormButton()
                            .setTitle(CustomerApplication.getStringResources(R.string.next_step))
                            .setBackgroundResource(R.drawable.bg_btn_selector)
                            .setTextColor(Color.WHITE)
                            .setParams(layoutParams)
                            .setRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    isJump = true;
                                    for (String str : formBuilder.formMap.keySet()) {
                                        FormElement fore = formBuilder.formMap.get(str);
                                        String tag = fore.getTagOrToString();
                                        String value = fore.getValue();
                                        userModel.userMap.put(tag, value);
                                        if (fore.getRequired() && TextUtils.isEmpty(value)) {
                                            isJump = false;
                                            Toast.makeText(InformationInputActivity.this, CustomerApplication.getStringResources(R.string.mandatory) + fore.getHint(), Toast.LENGTH_SHORT).show();
                                            View view = formBuilder.editTextMap.get(tag);
                                            if (view instanceof FineEditText) {
                                                ((FineEditText) view).setFocusable(true);
                                                ((FineEditText) view).setFocusableInTouchMode(true);
                                                ((FineEditText) view).requestFocus();
//                                            TextInputLayout text_input = view.findViewById(com.dariopellegrini.formbuilder.R.id.text_input);
//                                            if (null != text_input) {
//                                                text_input.setError(CustomerApplication.getStringResources(R.string.mandatory) + fore.getHint());
                                            }
                                            return;
                                        }

                                    }
                                    userModel.userMap.put(ActivityManager.EXPECT_ARRIVAL_DATE, TimeUtils.getStrDate(new Date(), "yyyy-MM-dd"));
                                    userModel.userMap.put(ActivityManager.EXPECT_ARRIVAL_TIME, 1200);
                                    userModel.userMap.put(ActivityManager.VISITOR_TYPE, visitorTypesModel.name);
                                    ActivityManager.startTakingPicturesActivity(InformationInputActivity.this, userModel);
                                }
                            })
            );
            formBuilder.build(formObjects);
        }
    }

    @Subscribe
    public void scanReturnHomePageEvent(ReturnHomePageEvent returnHomePageEvent) {
        if (returnHomePageEvent.isReturnHomePage()) {
            finish();
        }
    }

    @OnClick(R.id.image_home)
    public void onClick(View view) {
        returnHomePage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
