package com.oaksmuth.pittayaaec.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.oaksmuth.pittayaaec.activities.TopicSelect;

import java.util.List;

/**
 * Created by noraw on 9/3/2559.
 */
public class TwoTextArrayAdapter extends ArrayAdapter<Advanced> {
    private LayoutInflater mInflater;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    public TwoTextArrayAdapter(Context context, List<Advanced> items) {
        super(context, 0, items);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }
}
