package com.fullsail.franceschinoel_ce07.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullsail.franceschinoel_ce07.utils.People;
import com.fullsail.franceschinoel_ce07.R;
import com.fullsail.franceschinoel_ce07.activities.DetailActivity;

// Noel Franceschi
// MDF3 1610
// DetailFragment.java

public class DetailFragment extends Fragment {

    DetailActivityFragmentInterface detailActivityFragmentInterface;

    TextView firstName, lastName, age;

    public DetailFragment() {

    }

    public interface DetailActivityFragmentInterface {

        People passData();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DetailActivityFragmentInterface) {

            detailActivityFragmentInterface = (DetailActivityFragmentInterface) context;

        } else throw new RuntimeException("DetailActivityFragmentInterface not implemented");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        DetailActivity detailActivity = (DetailActivity) getActivity();

        firstName = (TextView) detailActivity.findViewById(R.id.detail_fragment_first_name);

        lastName = (TextView) detailActivity.findViewById(R.id.detail_fragment_last_name);

        age = (TextView) detailActivity.findViewById(R.id.detail_fragment_age);

        super.onActivityCreated(savedInstanceState);

    }

    public void setPerson() {

        People person = detailActivityFragmentInterface.passData();

        if (person != null) {

            firstName.setText(person.firstName);

            lastName.setText(person.lastName);

            age.setText(person.age);
        }
    }
}

