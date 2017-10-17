package com.fullsail.franceschinoel_ce07.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fullsail.franceschinoel_ce07.fragments.MainFragment;
import com.fullsail.franceschinoel_ce07.utils.PeopleAdapter;
import com.fullsail.franceschinoel_ce07.R;
import com.fullsail.franceschinoel_ce07.utils.StorageDB;

// Noel Franceschi
// MDF3 1610
// MainActivity.java

public class MainActivity extends AppCompatActivity implements MainFragment.MainActivityFragmentInterface {

    MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        PeopleAdapter customAdapter = new PeopleAdapter();

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);

        mainFragment.setListAdapter(customAdapter);

    }

    @Override
    protected void onStart() {

        super.onStart();

        PeopleAdapter peopleAdapter = (PeopleAdapter) mainFragment.getListAdapter();

        peopleAdapter.updateAdapter(StorageDB.readPersons(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_menu_add) {

            Intent intent = new Intent(this, FormActivity.class);

            startActivity(intent);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void launchDetailActivity(String id) {

        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra("PERSON_ID", id);

        startActivity(intent);

    }
}

