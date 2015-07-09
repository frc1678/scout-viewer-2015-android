package org.citruscircuits.scout_viewer_2015_android;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.citruscircuits.realm.Team;


public class ThirdPickListAdapter implements ListAdapter {
    Context context;
    TeamClickListener teamClickListener = new TeamClickListener();

    public ThirdPickListAdapter(Context context) {
        this.context = context;
        Log.e(Constants.LOG_TAG, "Constructor called");
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return MainActivity.thirdPickList.size();
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.thirdPickList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = layoutInflater.inflate(R.layout.second_picklist_row, parent, false);
        }

        TextView teamNumberLabel = (TextView) rowView.findViewById(R.id.teamNumberText);
        teamNumberLabel.setText(((Team)getItem(position)).getNumber() + "");

        TextView teamNameLabel = (TextView) rowView.findViewById(R.id.teamNameText);
        teamNameLabel.setText(((Team)getItem(position)).getName());


        TextView seedLabel = (TextView) rowView.findViewById(R.id.abilityText);
        seedLabel.setText(Math.round(((Team) getItem(position)).getCalculatedData().getThirdPickAbility()) + "");
        rowView.setTag(((Team)getItem(position)).getNumber() + "");

        rowView.setOnClickListener(teamClickListener);

        return rowView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
