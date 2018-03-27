package io.kuban.teamscreen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.kuban.teamscreen.CustomerApplication;
import io.kuban.teamscreen.R;
import io.kuban.teamscreen.model.VisitorTypesModel;
import io.kuban.teamscreen.utils.AccentColorUtils;


/**
 * Created by wangxuan on 17/11/9.
 */

public class VisitorAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<VisitorTypesModel> mDataList;

    private final Context mContext;

    public VisitorAdapter(Context context) {
        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VisitorTypesModel visitorTypesModel = mDataList.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.visitor_adapter_itme, null);
        TextView visitorName = (TextView) view.findViewById(R.id.visitor_name);
        ImageView visitor_types = (ImageView) view.findViewById(R.id.visitor_types);
        visitor_types.setImageDrawable(selectorDrawable(mContext, CustomerApplication.customColor, R.drawable.img_icon_yes_copy_pressed, R.drawable.img_icon_yes_copy));
        visitor_types.setSelected(visitorTypesModel.isSelected);
        visitorName.setText(visitorTypesModel.name);
        return view;
    }


    public void onlyAddAll(List<VisitorTypesModel> datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<VisitorTypesModel> datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }

    public Drawable selectorDrawable(Context context, String colorRes, int selectedId, int defaultId) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, AccentColorUtils.drawableColor(context, Color.parseColor(colorRes), selectedId));
        stateListDrawable.addState(new int[]{}, context.getResources().getDrawable(defaultId));
        return stateListDrawable;
    }
}
