package org.citruscircuits.scout_viewer_2015_android;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.citruscircuits.realm.CalculatedTeamData;
import org.citruscircuits.realm.Match;
import org.citruscircuits.realm.Team;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmQuery;


public class ChartListAdapter implements ListAdapter {
    Context context;
    String teamNumber;
    Team team;
    ChartClickListener chartClickListener = new ChartClickListener();

    public ChartListAdapter(Context context, String teamNumber) {
        this.context = context;
        this.teamNumber = teamNumber;

        Realm realm = Realm.getInstance(this.context, Constants.REALM_FILE);
        RealmQuery<Team> teamResult = realm.where(Team.class);
        teamResult.equalTo("number", Integer.parseInt(teamNumber));

        team = teamResult.findFirst();

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
        return MainActivity.chartList.size();
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.chartList.get(position);
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

        View chartView = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            chartView = layoutInflater.inflate(R.layout.chart_row, parent, false);
        }

        TextView chartLabel = (TextView) chartView.findViewById(R.id.chartName);
        chartLabel.setText((String)getItem(position) + "");

        chartView.setTag((String)getItem(position) + "-" + (String)getItem(position) + "-" + this.teamNumber);

        chartView.setOnClickListener(chartClickListener);

        TextView averageText = (TextView) chartView.findViewById(R.id.averageText);

        if (((String) getItem(position) + "").equals("Totes stacked")) {
            averageText.setText(team.getCalculatedData().getAvgNumTotesStacked() + "");
        }

        if (((String) getItem(position) + "").equals("Recons moved into auto zone")) {
            averageText.setText(team.getCalculatedData().getAvgNumReconsMovedIntoAutoZone() + "");
        }

        if (((String) getItem(position) + "").equals("Recon levels")) {
            averageText.setText(team.getCalculatedData().getAvgNumReconLevels() + "");
        }

        if (((String) getItem(position) + "").equals("Noodles contributed")) {
            averageText.setText(team.getCalculatedData().getAvgNumNoodlesContributed() + "");
        }

        if (((String) getItem(position) + "").equals("Max tote height")) {
            averageText.setText(team.getCalculatedData().getAvgMaxFieldToteHeight() + "");
        }

        if (((String) getItem(position) + "").equals("Recons stacked")) {
            averageText.setText(team.getCalculatedData().getAvgNumReconsStacked() + "");
        }

        if (((String) getItem(position) + "").equals("Recons picked up")) {
            averageText.setText(team.getCalculatedData().getAvgNumReconsPickedUp() + "");
        }

        if (((String) getItem(position) + "").equals("Totes picked up from ground")) {
            averageText.setText(team.getCalculatedData().getAvgNumTotesPickedUpFromGround() + "");
        }

        if (((String) getItem(position) + "").equals("Litter Dropped")) {
            averageText.setText(team.getCalculatedData().getAvgNumLitterDropped() + "");
        }

        if (((String) getItem(position) + "").equals("Stacks Damaged")) {
            averageText.setText(team.getCalculatedData().getAvgNumStacksDamaged() + "");
        }

        return chartView;
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
