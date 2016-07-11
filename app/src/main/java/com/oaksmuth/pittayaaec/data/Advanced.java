package com.oaksmuth.pittayaaec.data;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.oaksmuth.pittayaaec.R;

/**
 * Created by noraw on 9/3/2559.
 */
public class Advanced{
    public static final boolean TOPIC = true;
    public static final boolean HEADER = false;
    public String sentence;
    public boolean type;

    public Advanced(boolean type,String sentence){
        this.type = type;
        this.sentence = sentence;
    }

    @Override
    public String toString() {
        return sentence;
    }

    public int getViewType() {
        if(type){
            return TwoTextArrayAdapter.RowType.LIST_ITEM.ordinal();
        }
        else {
            return TwoTextArrayAdapter.RowType.HEADER_ITEM.ordinal();
        }
    }

    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        TextView text;
        if(type) {
            if (convertView == null) {
                view = inflater.inflate(R.layout.topic, null);
            } else {
                view = convertView;
            }
            text = (TextView) view.findViewById(R.id.topicTextView);
        }else {
            if (convertView == null) {
                view = inflater.inflate(R.layout.header, null);
            } else {
                view = convertView;
            }
            text = (TextView) view.findViewById(R.id.separator);
        }
        text.setText(sentence);
        return view;
    }
}
