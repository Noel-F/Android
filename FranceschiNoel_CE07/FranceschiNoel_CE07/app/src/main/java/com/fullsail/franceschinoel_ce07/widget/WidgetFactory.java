package com.fullsail.franceschinoel_ce07.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fullsail.franceschinoel_ce07.R;
import com.fullsail.franceschinoel_ce07.utils.People;
import com.fullsail.franceschinoel_ce07.utils.StorageDB;

import java.util.ArrayList;

// Noel Franceschi
// MDF3 1610
// WidgetFactory.java

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<People> personArrayList = new ArrayList<>();

    private Context context;

    public WidgetFactory(Context context) {

        this.context = context;

        personArrayList = StorageDB.readPersons(context);

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        personArrayList = StorageDB.readPersons(context);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

        return personArrayList.size();

    }

    @Override
    public RemoteViews getViewAt(int position) {

        People person = personArrayList.get(position);

        RemoteViews itemView = new RemoteViews(context.getPackageName(), R.layout.list_item);

        itemView.setTextViewText(R.id.list_first_name, person.firstName);
        itemView.setTextViewText(R.id.list_last_name, person.lastName);
        itemView.setTextViewText(R.id.list_number, person.age);

        Intent intent = new Intent();
        intent.putExtra("PERSON_ID", person.id);

        itemView.setOnClickFillInIntent(R.id.list_item_layout, intent);

        return itemView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

