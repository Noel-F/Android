package com.fullsail.franceschinoel_ce07.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.fullsail.franceschinoel_ce07.fragments.DetailFragment;
import com.fullsail.franceschinoel_ce07.utils.People;
import com.fullsail.franceschinoel_ce07.R;
import com.fullsail.franceschinoel_ce07.utils.StorageDB;
import com.fullsail.franceschinoel_ce07.widget.WidgetHelper;

// Noel Franceschi
// MDF3 1610
// DetailActivity.java

public class DetailActivity extends AppCompatActivity implements DetailFragment.DetailActivityFragmentInterface {

    String personId;

    DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        personId = getIntent().getStringExtra("PERSON_ID");

        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
    }

    @Override
    public People passData() {
        return StorageDB.getPersonFromId(this, personId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        detailFragment.setPerson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.detail_menu_delete) {

            StorageDB.deletePerson(this, personId);

            WidgetHelper.updateWidget(this);

            finish();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}

