package com.ramyac.myprojects.tuneintodolist;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TodoListFragment extends Fragment {

    ListView todoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.todo_list_fragment, container, false);
        todoList = (ListView) view.findViewById(R.id.view_todo_list);
        setHasOptionsMenu(true);
        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_todolist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_todo_item) {
            showCreateTodoFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCreateTodoFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment createTodoFragment = new CreateTodoListFragment();
        ft.replace(R.id.layout, createTodoFragment, "CreateTodoFragment");
        ft.commit();
    }

    private void updateUI() {
        ItemDBHelper helper = new ItemDBHelper(getContext());
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                ItemContract.TABLE,
                new String[]{ItemContract.Columns._ID, ItemContract.Columns.ITEM,
                        ItemContract.Columns.COMPLETE},
                ItemContract.Columns.COMPLETE + "=0", null, null, null, null);
        TodoListAdapter adapter = new TodoListAdapter(getContext(), cursor);
        todoList.setAdapter(adapter);

    }

    private void updateItemCompleteDB(String itemText, boolean isComplete) {
        ItemDBHelper helper = new ItemDBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        values.put(ItemContract.Columns.COMPLETE, isComplete);

        db.update(ItemContract.TABLE, values, ItemContract.Columns.ITEM + "=\"" + itemText + "\"", null);
    }

    private void updateItemDB(String oldItemText,String newItemText) {
        ItemDBHelper helper = new ItemDBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        values.put(ItemContract.Columns.ITEM, newItemText);

        db.update(ItemContract.TABLE, values, ItemContract.Columns.ITEM+"=\""+oldItemText+"\"", null);
    }

    private class TodoListAdapter extends ResourceCursorAdapter {

        public TodoListAdapter(Context context, Cursor cur) {
            super(context, R.layout.todo_item_view, cur, true);
        }

        @Override
        public View newView(Context context, Cursor cur, ViewGroup parent) {
            LayoutInflater li =
                    (LayoutInflater) getActivity().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            return li.inflate(R.layout.todo_item_view, parent, false);
        }

        @Override
        public void bindView(final View view, Context context, Cursor cur) {
            final TextView todoItemText = (TextView)view.findViewById(R.id.todo_item_text);
            CheckBox todoItemDone = (CheckBox)view.findViewById(R.id.todo_item_mark_done);

            todoItemText.setText(cur.getString(cur.getColumnIndex(ItemContract.Columns.ITEM)));
            todoItemDone.setChecked(
                    (cur.getInt(cur.getColumnIndex(ItemContract.Columns.COMPLETE)) == 0
                            ? false : true));
            todoItemDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    updateItemCompleteDB(todoItemText.getText().toString(), isChecked);
                    view.setVisibility(View.INVISIBLE);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView  = inflater.inflate(R.layout.edit_dialog,null);
                    final EditText editDialogText =
                            (EditText) dialogView.findViewById(R.id.edit_dialog_text);
                    AlertDialog.Builder editDialog =
                            new AlertDialog.Builder(getContext())
                                    .setView(dialogView)
                                    .setTitle(R.string.edit_dialog_title)
                                    .setPositiveButton(R.string.save_item,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String newText = editDialogText.getText().toString();
                                                    updateItemDB(todoItemText.getText().toString(),
                                                            editDialogText.getText().toString());
                                                    todoItemText.setText(newText);
                                                }
                                            })
                                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                            });
                    editDialog.create().show();
                    return true;
                }
            });
        }
    }
}
