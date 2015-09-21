package com.ramyac.myprojects.tuneintodolist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        showCreateTodoFragment();
    }

    private void showCreateTodoFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment createTodoFragment = new CreateTodoListFragment();
        ft.replace(R.id.layout, createTodoFragment, "CreateTodoFragment");
        ft.commit();
    }
}
