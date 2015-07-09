package org.citruscircuits.scout_viewer_2015_android;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.citruscircuits.realm.Match;


public class MatchListAdapter implements ListAdapter {
    Context context;
    MatchClickListener matchClickListener = new MatchClickListener();

    public MatchListAdapter(Context context) {
        this.context = context;

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
        return MainActivity.matchList.size();
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.matchList.get(position);
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
            rowView = layoutInflater.inflate(R.layout.match_row, parent, false);
        }

        TextView matchNumberLabel = (TextView) rowView.findViewById(R.id.matchNum);
        matchNumberLabel.setText(((Match)getItem(position)).getMatch() + "");

        TextView redScoreLabel = (TextView) rowView.findViewById(R.id.redTeamScore);

        if (((Match)getItem(position)).getOfficialRedScore() != -1) {
            redScoreLabel.setText(((Match)getItem(position)).getOfficialRedScore() +"");
            redScoreLabel.setAlpha(1.0f);
        } else {
            redScoreLabel.setText(((Match) getItem(position)).getCalculatedData().getPredictedRedScore() + "");
            redScoreLabel.setAlpha(0.3f);
        }

        TextView blueScoreLabel = (TextView) rowView.findViewById(R.id.blueTeamScore);

        if (((Match)getItem(position)).getOfficialBlueScore() != -1) {
            blueScoreLabel.setText(((Match) getItem(position)).getOfficialBlueScore() + "");
            blueScoreLabel.setAlpha(1.0f);
        } else {
            blueScoreLabel.setText(((Match) getItem(position)).getCalculatedData().getPredictedBlueScore() + "");
            blueScoreLabel.setAlpha(0.3f);
        }

        TextView redTeam1 = (TextView) rowView.findViewById(R.id.redTeam1);
        redTeam1.setText(((Match) getItem(position)).getRedTeams().get(0).getNumber() + "");

        TextView redTeam2 = (TextView) rowView.findViewById(R.id.redTeam2);
        redTeam2.setText(((Match) getItem(position)).getRedTeams().get(1).getNumber() + "");

        TextView redTeam3 = (TextView) rowView.findViewById(R.id.redTeam3);
        redTeam3.setText(((Match) getItem(position)).getRedTeams().get(2).getNumber() + "");

        TextView blueTeam1 = (TextView) rowView.findViewById(R.id.blueTeam1);
        blueTeam1.setText(((Match) getItem(position)).getBlueTeams().get(0).getNumber() + "");

        TextView blueTeam2 = (TextView) rowView.findViewById(R.id.blueTeam2);
        blueTeam2.setText(((Match) getItem(position)).getBlueTeams().get(1).getNumber() + "");

        TextView blueTeam3 = (TextView) rowView.findViewById(R.id.blueTeam3);
        blueTeam3.setText(((Match) getItem(position)).getBlueTeams().get(2).getNumber() + "");

        rowView.setTag(((Match)getItem(position)).getMatch() + "");

        rowView.setOnClickListener(matchClickListener);

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
