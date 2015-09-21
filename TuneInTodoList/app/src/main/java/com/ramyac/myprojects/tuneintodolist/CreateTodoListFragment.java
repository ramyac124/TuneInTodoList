package com.ramyac.myprojects.tuneintodolist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateTodoListFragment extends Fragment {

    private EditText itemText;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_todo_list, container, false);
        itemText = (EditText) view.findViewById(R.id.add_todo_item_text);
        saveButton = (Button) view.findViewById(R.id.save_item);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItemDB(itemText.getText().toString());
                showToastAndClearText();
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_createtodo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.show_all_items) {
            showTodoListFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showTodoListFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment todoListFragment = new TodoListFragment();
        ft.replace(R.id.layout, todoListFragment, "TodoListFragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void saveItemDB(String item) {
        ItemDBHelper helper = new ItemDBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        values.put(ItemContract.Columns.ITEM, item);
        values.put(ItemContract.Columns.COMPLETE, Boolean.FALSE);

        db.insertWithOnConflict(
                ItemContract.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void showToastAndClearText() {
        Toast.makeText(
                getContext(), getResources().getString(R.string.toast_text), Toast.LENGTH_SHORT)
                .show();
        itemText.setText("");
    }

}
