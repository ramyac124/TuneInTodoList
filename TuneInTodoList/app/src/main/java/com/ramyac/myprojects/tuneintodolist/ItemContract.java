package com.ramyac.myprojects.tuneintodolist;

import android.provider.BaseColumns;

public class ItemContract {

    public static final String DB_NAME = "com.ramyac.myprojects.TuneInTodoList.db.todo_items";
    public static final int DB_VERSION = 3;
    public static final String TABLE = "items";

    public class Columns {
        public static final String ITEM = "item";
        public static final String COMPLETE = "complete";
        public static final String _ID = BaseColumns._ID;
    }
}
