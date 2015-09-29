package com.wewang.todoapp.models;

import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by wewang on 9/22/15.
 */
public class ToDoItem {
    private String value;
    private Date dueDate;

    public ToDoItem(String value, Date dueDate) {
        this.value = value;
        this.dueDate = dueDate;
    }

    public String getValue() {
        return value;
    }

    public Date getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return value + ": " + dueDate;
    }
}
