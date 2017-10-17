package com.fullsail.franceschinoel_ce07.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fullsail.franceschinoel_ce07.R;

import java.util.ArrayList;

// Noel Franceschi
// MDF3 1610
// PeopleAdapter.java

public class PeopleAdapter extends BaseAdapter {

    ArrayList<People> personArrayList = new ArrayList<>();

    public PeopleAdapter() {

    }

    public void updateAdapter(ArrayList<People> personArrayList) {

        this.personArrayList = personArrayList;

        this.notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return personArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return personArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return personArrayList.indexOf(personArrayList.get(position));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.list_item, parent, false);

        }

        People person = personArrayList.get(position);

        TextView firstName = (TextView) convertView.findViewById(R.id.list_first_name);
        TextView lastName = (TextView) convertView.findViewById(R.id.list_last_name);
        TextView age = (TextView) convertView.findViewById(R.id.list_number);

        firstName.setText(person.firstName);
        lastName.setText(person.lastName);
        age.setText(person.age);

        convertView.setTag(person.id);

        return convertView;

    }

}



