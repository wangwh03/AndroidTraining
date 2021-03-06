package com.wewang.todoapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.wewang.todoapp.adapters.ToDoItemAdapter;
import com.wewang.todoapp.commons.Constants;
import com.wewang.todoapp.helpers.ToDoItemsDatabaseHelper;
import com.wewang.todoapp.models.ToDoItem;
import com.wewang.todoapp.views.EditDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditDialog.DismissDialogListener {
    private List<ToDoItem> todoItems;
    private ToDoItemAdapter aToDoAdapter;
    private ListView lvItems;
    private EditText etEditText;
    private EditText etDueDateText;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DUE_DATE_FORMAT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        etDueDateText = (EditText) findViewById(R.id.etDueDateText);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                deleteItem(id+1);
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoItem selectedItem = (ToDoItem) lvItems.getItemAtPosition(position);
                launchEditView(position, selectedItem, id+1);
            }
        });
    }

    public void populateArrayItems() {
        todoItems = readItems();
        aToDoAdapter = new ToDoItemAdapter(this, todoItems);
    }

    public void launchEditView(int position, ToDoItem selectedItem, long id) {
        FragmentManager fm = getSupportFragmentManager();
        EditDialog editNameDialog = EditDialog.newInstance("Edit Item", selectedItem.getValue(),
                dateFormat.format(selectedItem.getDueDate()), position, id);
        editNameDialog.show(fm, "fragment_edit");
    }

    private List<ToDoItem> readItems() {
        ToDoItemsDatabaseHelper databaseHelper = ToDoItemsDatabaseHelper.getInstance(this);
        return databaseHelper.getAllItems();
    }

    private void addItem(ToDoItem item) {
        ToDoItemsDatabaseHelper databaseHelper = ToDoItemsDatabaseHelper.getInstance(this);
        databaseHelper.addItem(item);
    }

    private void updateItem(ToDoItem item, long itemId) {
        ToDoItemsDatabaseHelper databaseHelper = ToDoItemsDatabaseHelper.getInstance(this);
        databaseHelper.updateItemValue(item, itemId);
    }

    private void deleteItem(long itemId) {
        ToDoItemsDatabaseHelper databaseHelper = ToDoItemsDatabaseHelper.getInstance(this);
        databaseHelper.deleteItem(itemId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) throws ParseException {
        ToDoItem toDoItem = new ToDoItem(etEditText.getText().toString(),
                dateFormat.parse(etDueDateText.getText().toString()));
        aToDoAdapter.add(toDoItem);
        etEditText.setText("");
        etDueDateText.setText("");
        addItem(toDoItem);
    }

    @Override
    public void onFinishEditDialog(String newValue, String newDueDate, int position, long itemId) {
        try {
            ToDoItem updatedItem = new ToDoItem(newValue, dateFormat.parse(newDueDate));
            todoItems.set(position, updatedItem);
            aToDoAdapter.notifyDataSetChanged();
            Log.d("main", todoItems.toString());
            Log.d("position", position+"");
            Log.d("finish edit", aToDoAdapter.getItem(position).toString());
            updateItem(updatedItem, itemId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
