package com.fullsail.franceschinoel_ce07.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.fullsail.franceschinoel_ce07.fragments.FormFragment;
import com.fullsail.franceschinoel_ce07.utils.People;
import com.fullsail.franceschinoel_ce07.R;
import com.fullsail.franceschinoel_ce07.utils.StorageDB;
import com.fullsail.franceschinoel_ce07.widget.WidgetHelper;

// Noel Franceschi
// MDF3 1610
// FormActivity.java

public class FormActivity extends AppCompatActivity implements FormFragment.FormFragmentInterface {

    //private static final String TAG = "FormActivity" ;

    FormFragment formFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form);

        formFragment = (FormFragment) getSupportFragmentManager().findFragmentById(R.id.form_fragment);

    }

    @Override
    public void SavePeopleFromValues(People person) {

        int i = StorageDB.readPersons(this).size();

        person.id = String.valueOf(i);

        StorageDB.savePerson(this, person);

        WidgetHelper.updateWidget(this);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_form, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.form_menu_save) {

            formFragment.savePerson();

            return true;

        }

        return super.onOptionsItemSelected(item);

    }
}

