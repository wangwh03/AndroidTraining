package com.wewang.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wewang.todoapp.R;
import com.wewang.todoapp.commons.Constants;
import com.wewang.todoapp.models.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wewang on 9/27/15.
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DUE_DATE_FORMAT);

    public ToDoItemAdapter(Context context, List<ToDoItem> toDoItems) {
        super(context, 0, toDoItems);
    }

    private static class ViewHolder {
        private TextView value;
        private TextView dueDate;

        public TextView getValue() {
            return value;
        }

        public TextView getDueDate() {
            return dueDate;
        }

        public void setValue(TextView value) {
            this.value = value;
        }

        public void setDueDate(TextView dueDate) {
            this.dueDate = dueDate;
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
            viewHolder.setValue((TextView) convertView.findViewById(R.id.itemValue));
            viewHolder.setDueDate((TextView) convertView.findViewById(R.id.itemDueDate));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.getValue().setText(toDoItem.getValue());
        viewHolder.getDueDate().setText(dateFormat.format(toDoItem.getDueDate()));

        return convertView;
    }
}
