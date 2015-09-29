package com.wewang.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class EditItemActivity extends AppCompatActivity {
    private EditText etEditText;
    private EditText etDueDate;
    private int itemPosition;
    private long itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditText = (EditText) findViewById(R.id.etEditItem);
        etDueDate = (EditText) findViewById(R.id.etDueDate);

        String oldItemText = getIntent().getStringExtra("oldText");
        etEditText.setText(oldItemText);
        etEditText.setSelection(oldItemText.length());

        String oldDueDate = getIntent().getStringExtra("oldDueDate");
        etDueDate.setText(oldDueDate);
        etDueDate.setSelection(oldDueDate.length());

        itemPosition = getIntent().getIntExtra("position", 0);
        itemId = getIntent().getLongExtra("itemId", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onSubmit(View view) {
        EditText etName = (EditText) findViewById(R.id.etEditItem);
        EditText etDueDate = (EditText) findViewById(R.id.etDueDate);

        Intent data = new Intent();
        data.putExtra("newText", etName.getText().toString());
        data.putExtra("position", itemPosition);
        data.putExtra("itemId", itemId);
        data.putExtra("newDueDate", etDueDate.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
