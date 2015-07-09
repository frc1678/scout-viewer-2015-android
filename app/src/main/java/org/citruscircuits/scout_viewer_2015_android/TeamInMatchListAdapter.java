package org.citruscircuits.scout_viewer_2015_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.citruscircuits.realm.Match;
import org.citruscircuits.realm.Team;
import org.citruscircuits.realm.TeamInMatchData;


public class TeamInMatchListAdapter implements ListAdapter {
    Context context;
    MatchClickListener matchClickListener = new MatchClickListener();

    public TeamInMatchListAdapter(Context context) {
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
        return TeamDetailActivity.teamInMatchList.size();
    }

    @Override
    public Object getItem(int position) {
        return TeamDetailActivity.teamInMatchList.get(position);
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
            rowView = layoutInflater.inflate(R.layout.team_in_match_row, parent, false);
        }

        final TextView teamInMatchLabel = (TextView) rowView.findViewById(R.id.teamInMatchNumText);
        teamInMatchLabel.setText(((TeamInMatchData)getItem(position)).getMatch().getMatch() + "");

        TextView redScoreLabel = (TextView) rowView.findViewById(R.id.redTeamScore);

        if (((TeamInMatchData)getItem(position)).getMatch().getOfficialRedScore() != -1) {
            redScoreLabel.setText(((TeamInMatchData)getItem(position)).getMatch().getOfficialRedScore() + "");
            redScoreLabel.setAlpha(1.0f);
        } else {
            redScoreLabel.setText(((TeamInMatchData)getItem(position)).getMatch().getCalculatedData().getPredictedRedScore() + "");
            redScoreLabel.setAlpha(0.3f);
        }

        TextView blueScoreLabel = (TextView) rowView.findViewById(R.id.blueTeamScore);

        if (((TeamInMatchData)getItem(position)).getMatch().getOfficialBlueScore() != -1) {
            blueScoreLabel.setText(((TeamInMatchData)getItem(position)).getMatch().getOfficialBlueScore() + "");
            blueScoreLabel.setAlpha(1.0f);
        } else {
            blueScoreLabel.setText(((TeamInMatchData)getItem(position)).getMatch().getCalculatedData().getPredictedBlueScore() + "");
            blueScoreLabel.setAlpha(0.3f);
        }

        rowView.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Log.e(Constants.LOG_TAG, "Team onClicked: " + view.getTag());
                Intent intent = new Intent(view.getContext(), MatchDetailActivity.class);
                intent.putExtra("matchNum", view.getTag().toString());

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                view.getContext().startActivity(intent);
            }
        });

        rowView.setTag(((TeamInMatchData)getItem(position)).getMatch().getMatch() + "");

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
