package com.example.flexiblerefreshview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anymo on 2018/7/2.
 */

public class RefreshAdapter extends BaseAdapter {
    private List<String> lists;
    private Context context;
    private boolean isEnabled = true;

    public RefreshAdapter(List<String> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    public void setStrs(List<String> lists){
        this.lists = lists;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvText.setText(lists.get(i));
        return convertView;
    }

    static class ViewHolder {
        TextView tvText;
    }

    @Override
    public boolean isEnabled(int position) {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
