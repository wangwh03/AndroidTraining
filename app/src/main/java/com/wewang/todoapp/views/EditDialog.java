package com.wewang.todoapp.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.wewang.todoapp.R;

/**
 * Created by wewang on 9/28/15.
 */
public class EditDialog extends DialogFragment {
    private EditText etValue;
    private EditText etDueDate;
    private int position;
    private long itemId;

    public interface  DismissDialogListener {
        void onFinishEditDialog(String newValue, String newDueDate, int position, long itemId);
    }

    public EditDialog() {}

    public static EditDialog newInstance(String title, String taskName, String dueDate,
                                         int position, long itemId) {
        EditDialog fragment = new EditDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("taskName", taskName);
        args.putString("dueDate", dueDate);
        args.putInt("position", position);
        args.putLong("itemId", itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_edit, container);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        DismissDialogListener listener = (DismissDialogListener) getActivity();
        listener.onFinishEditDialog(etValue.getText().toString(), etDueDate.getText().toString(), position, itemId);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        position = getArguments().getInt("position");
        itemId = getArguments().getLong("itemId");

        etValue = (EditText) view.findViewById(R.id.etOverlayValue);
        etDueDate = (EditText) view.findViewById(R.id.etOverlayDueDate);

        String title = getArguments().getString("title", "Edit");
        getDialog().setTitle(title);

        String oldItemText = getArguments().getString("taskName");
        etValue.setText(oldItemText);
        etValue.setSelection(oldItemText.length());

        String oldDueDate = getArguments().getString("dueDate");
        etDueDate.setText(oldDueDate);
        etDueDate.setSelection(oldDueDate.length());

        etValue.requestFocus();

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
