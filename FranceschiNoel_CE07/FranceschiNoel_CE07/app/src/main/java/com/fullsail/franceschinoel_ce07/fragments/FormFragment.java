package com.fullsail.franceschinoel_ce07.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.fullsail.franceschinoel_ce07.utils.People;
import com.fullsail.franceschinoel_ce07.R;
import com.fullsail.franceschinoel_ce07.activities.FormActivity;

// Noel Franceschi
// MDF3 1610
// FormFragment.java

public class FormFragment extends Fragment {

    //public static final String TAG = "Form";

    EditText firstName, lastName, age;

    FormFragmentInterface formFragmentInterface;

    public FormFragment() {

    }

    public interface FormFragmentInterface {

        void SavePeopleFromValues(People person);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FormFragmentInterface) {

            formFragmentInterface = (FormFragmentInterface) context;

        } else throw new RuntimeException("FormInterface not implemented!");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_form,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FormActivity formActivity = (FormActivity) getActivity();

        firstName = (EditText) formActivity.findViewById(R.id.form_fragment_first_name);
        lastName = (EditText) formActivity.findViewById(R.id.form_fragment_last_name);
        age = (EditText) formActivity.findViewById(R.id.form_fragment_age);

    }

    public void savePerson(){

        if (firstName.getText().toString().equals("") || lastName.getText().toString().equals("")
                || age.getText().toString().equals("")) {

            Toast.makeText(getActivity(), "Please fill all the details", Toast.LENGTH_SHORT).show();

        }else {

            String first = String.valueOf(firstName.getText());
            String last = String.valueOf(lastName.getText());
            String age = String.valueOf(this.age.getText());

            People person = new People(first, last, age,  null);

            formFragmentInterface.SavePeopleFromValues(person);

        }

    }
}



