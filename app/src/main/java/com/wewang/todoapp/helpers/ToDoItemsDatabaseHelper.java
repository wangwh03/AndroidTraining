package com.wewang.todoapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wewang.todoapp.commons.Constants;
import com.wewang.todoapp.models.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDoItemsDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ToDoApp";
    private static ToDoItemsDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "toDoItemsDatabase";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_ITEMS = "items";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_VALUE = "value";
    private static final String KEY_ITEM_DUE_DATE = "due_date";

    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DUE_DATE_FORMAT);

    private ToDoItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ToDoItemsDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ToDoItemsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_VALUE + " TEXT," +
                KEY_ITEM_DUE_DATE + " DATE" +
                ")";

        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    public List<ToDoItem> getAllItems() {
        List<ToDoItem> toDoItems = new ArrayList<>();

        // SELECT * FROM ITEMS
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_ITEMS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.d("fetched data time", cursor.getString(cursor.getColumnIndex(KEY_ITEM_DUE_DATE)));
                    Date dueDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_ITEM_DUE_DATE)));
                    ToDoItem newItem =
                            new ToDoItem(cursor.getString(cursor.getColumnIndex(KEY_ITEM_VALUE)),
                                    dueDate);
                    toDoItems.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get to do items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return toDoItems;
    }

    public void addItem(ToDoItem toDoItem) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_VALUE, toDoItem.getValue());
            values.put(KEY_ITEM_DUE_DATE, dateFormat.format(toDoItem.getDueDate()));

            db.insertOrThrow(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
            Log.d(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void updateItemValue(ToDoItem toDoItem, long itemId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_VALUE, toDoItem.getValue());
        values.put(KEY_ITEM_DUE_DATE, dateFormat.format(toDoItem.getDueDate()));

        db.beginTransaction();
        try {
            Log.d(TAG, "updating item with id: " + itemId);
            db.update(TABLE_ITEMS, values, KEY_ITEM_ID + " = ?",
                    new String[]{String.valueOf(itemId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update to do item with id " + itemId);
        } finally {
            db.endTransaction();
        }
    }

    public void deleteItem(long itemId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            Log.d(TAG, "deleting item with id: " + itemId);
            db.delete(TABLE_ITEMS, KEY_ITEM_ID + "=" + itemId, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete to do item with id " + itemId);
            Log.d(TAG, e.getMessage());
        }
        db.endTransaction();
    }
}
