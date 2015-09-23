package com.wewang.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.wewang.todoapp.helpers.ToDoItemsDatabaseHelper;
import com.wewang.todoapp.models.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ToDoItem> todoItems;
    private ArrayAdapter<ToDoItem> aToDoAdapter;
    private ListView lvItems;
    private EditText etEditText;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
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
        todoItems = new ArrayList<>();
        readItems();
        aToDoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    public void launchEditView(int position, ToDoItem selectedItem, long id) {
        Intent editIntent = new Intent(this, EditItemActivity.class);
        editIntent.putExtra("position", position);
        editIntent.putExtra("oldText", selectedItem.getValue());
        editIntent.putExtra("itemId", id);
        startActivityForResult(editIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String newText = data.getExtras().getString("newText");
            int position = data.getExtras().getInt("position", 0);
            long itemId = data.getExtras().getLong("itemId", 0);

            ToDoItem updatedItem = new ToDoItem(newText);

            todoItems.set(position, updatedItem);
            aToDoAdapter.notifyDataSetChanged();
            updateItem(updatedItem, itemId);
        }
    }

    private void readItems() {
        ToDoItemsDatabaseHelper databaseHelper = ToDoItemsDatabaseHelper.getInstance(this);
        todoItems = databaseHelper.getAllItems();
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

    public void onAddItem(View view) {
        ToDoItem toDoItem = new ToDoItem(etEditText.getText().toString());
        aToDoAdapter.add(toDoItem);
        etEditText.setText("");
        addItem(toDoItem);
    }
}
