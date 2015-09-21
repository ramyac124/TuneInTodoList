package com.ramyac.myprojects.tuneintodolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemDBHelper extends SQLiteOpenHelper {

    public static final String TAG = ItemDBHelper.class.getSimpleName();

    public ItemDBHelper(Context context) {
        super(context, ItemContract.DB_NAME, null, ItemContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + ItemContract.TABLE +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemContract.Columns.ITEM + " TEXT, "
                + ItemContract.Columns.COMPLETE + " BOOLEAN )";
        execSQL(db, sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlQuery = "DROP TABLE IF EXISTS " + ItemContract.TABLE;
        execSQL(db, sqlQuery);
        onCreate(db);
    }

    private void execSQL(SQLiteDatabase db, String sqlQuery) {
        db.execSQL(sqlQuery);
        Log.d(TAG, "SQL Query executed " + sqlQuery);
    }
}