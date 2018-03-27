package com.dariopellegrini.formbuilder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by zhanghuawei on 2017/2/9.
 */

public class SelectedDialog extends Dialog {
    private Context mContext;
    private int beforePosition;
    private SelectedAdapter selectedAdapter;
    private List<String> list;
    private String customColor;
    private MyOnItemClickListener onItemClickListene;

    public SelectedDialog(Context context, String customColor, List<String> list, int position, MyOnItemClickListener onItemClickListener) {
        super(context, R.style.Dialog);
        this.mContext = context;
        this.list = list;
        this.customColor = customColor;
        this.beforePosition = position;
        this.onItemClickListene = onItemClickListener;
        setCustomDialog();
    }


    private void setCustomDialog() {
        this.setCancelable(true);// 设置点击屏幕Dialog不消失
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.selected_dialog, null);
        ListView listView = (ListView) mView.findViewById(R.id.list_view);
        selectedAdapter = new SelectedAdapter(mContext);
        listView.setAdapter(selectedAdapter);
        super.setContentView(mView);
    }


    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }


    class SelectedAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public SelectedAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.selected_item, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.name);
            ImageView visitor_types = (ImageView) convertView.findViewById(R.id.visitor_types);
            name.setText(list.get(position));
            if (!TextUtils.isEmpty(customColor)) {
                visitor_types.setImageDrawable(selectorDrawable(mContext, customColor, R.drawable.img_icon_yes_copy_pressed, R.drawable.img_icon_yes_copy));
            }
            if (position == beforePosition) {
                visitor_types.setSelected(true);
            } else {
                visitor_types.setSelected(false);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListene) {
                        onItemClickListene.onItemClickListener(position);
                        beforePosition = position;
                        notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                dismiss();
                            }
                        }, 100);

                    }
                }
            });
            return convertView;
        }
    }

    public Drawable selectorDrawable(Context context, String colorRes, int selectedId, int defaultId) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, drawableColor(context, Color.parseColor(colorRes), selectedId));
        stateListDrawable.addState(new int[]{}, context.getResources().getDrawable(defaultId));
        return stateListDrawable;
    }

    public static Drawable drawableColor(Context context, int colorResId, int drawableId) {
        DrawableColorChange1 normalDrawable = new DrawableColorChange1(context);
        normalDrawable.setDrawable(drawableId);
        normalDrawable.setColor(colorResId);
        return normalDrawable.getColorChangedDrawable();
    }

    public interface MyOnItemClickListener {
        public void onItemClickListener(int position);
    }
}

