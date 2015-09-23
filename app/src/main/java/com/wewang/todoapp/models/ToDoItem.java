package com.wewang.todoapp.models;

/**
 * Created by wewang on 9/22/15.
 */
public class ToDoItem {
    private String value;

    public ToDoItem(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
