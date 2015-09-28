package com.wewang.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wewang.todoapp.R;
import com.wewang.todoapp.models.ToDoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wewang on 9/27/15.
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    public ToDoItemAdapter(Context context, List<ToDoItem> toDoItems) {
        super(context, 0, toDoItems);
    }

    private static class ViewHolder {
        private TextView value;

        public TextView getValue() {
            return value;
        }

        public void setValue(TextView value) {
            this.value = value;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoItem toDoItem = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            viewHolder.setValue((TextView) convertView.findViewById(R.id.item));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.getValue().setText(toDoItem.getValue());

        return convertView;
    }
}
