package com.fullsail.franceschinoel_ce03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

// Noel Franceschi
// MDF3 - 1610
// NewsAdapter.java

@SuppressWarnings("WeakerAccess")
public class NewsAdapter extends BaseAdapter {

    private ArrayList<News> nArrayList = new ArrayList<>();

    private Context mContext;

    public NewsAdapter(ArrayList<News> newsArrayList, Context mContext) {

        this.nArrayList = newsArrayList;
        this.mContext = mContext;

    }


    public void updateData(ArrayList<News> news) {

        this.nArrayList = news;
        this.notifyDataSetChanged();

    }

    @Override
    public int getCount() {

        return nArrayList.size();

    }

    @Override
    public Object getItem(int position) {

        return nArrayList.get(position);

    }

    @Override
    public long getItemId(int position) {

        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.items_notis, parent, false);

        }

        News news = nArrayList.get(position);

        TextView titleTv = (TextView) convertView.findViewById(R.id.notification_title);
        TextView descriptionTv = (TextView) convertView.findViewById(R.id.notification_description);

        titleTv.setText(news.getTitle());
        descriptionTv.setText(news.getDescription());

        convertView.setTag(news);

        return convertView;

    }
}

