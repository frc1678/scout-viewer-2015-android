package org.citruscircuits.scout_viewer_2015_android;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.citruscircuits.realm.Match;
import org.citruscircuits.realm.Team;
import org.citruscircuits.realm.TeamInMatchData;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class MatchDetailActivity extends ActionBarActivity {

    public static ArrayList<TeamInMatchData> matchList = new ArrayList<>();
    public Match match;

    TeamClickListener teamClickListener = new TeamClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_detail);

        Intent intent = getIntent();

        Realm realm = Realm.getInstance(this, Constants.REALM_FILE);

        RealmQuery<Match> matchQuery = realm.where(Match.class);
        matchQuery.equalTo("match", intent.getStringExtra("matchNum"));
        match = matchQuery.findFirst();

        ((TextView)findViewById(R.id.matchNumberText)).setText(match.getMatch());

        ((TextView)findViewById(R.id.redTeam1)).setText(Integer.toString(match.getRedTeams().get(0).getNumber()));
        ((TextView)findViewById(R.id.redTeam2)).setText(Integer.toString(match.getRedTeams().get(1).getNumber()));
        ((TextView)findViewById(R.id.redTeam3)).setText(Integer.toString(match.getRedTeams().get(2).getNumber()));

        ((TextView)findViewById(R.id.blueTeam1)).setText(Integer.toString(match.getBlueTeams().get(0).getNumber()));
        ((TextView)findViewById(R.id.blueTeam2)).setText(Integer.toString(match.getBlueTeams().get(1).getNumber()));
        ((TextView)findViewById(R.id.blueTeam3)).setText(Integer.toString(match.getBlueTeams().get(2).getNumber()));



        ((TextView)findViewById(R.id.redTeam1)).setTag(match.getRedTeams().get(0).getNumber());
        ((TextView)findViewById(R.id.redTeam1)).setOnClickListener(teamClickListener);

        ((TextView)findViewById(R.id.redTeam2)).setTag(match.getRedTeams().get(1).getNumber());
        ((TextView)findViewById(R.id.redTeam2)).setOnClickListener(teamClickListener);

        ((TextView)findViewById(R.id.redTeam3)).setTag(match.getRedTeams().get(2).getNumber());
        ((TextView)findViewById(R.id.redTeam3)).setOnClickListener(teamClickListener);

        ((TextView)findViewById(R.id.blueTeam1)).setTag(match.getBlueTeams().get(0).getNumber());
        ((TextView)findViewById(R.id.blueTeam1)).setOnClickListener(teamClickListener);

        ((TextView)findViewById(R.id.blueTeam2)).setTag(match.getBlueTeams().get(1).getNumber());
        ((TextView)findViewById(R.id.blueTeam2)).setOnClickListener(teamClickListener);

        ((TextView)findViewById(R.id.blueTeam3)).setTag(match.getBlueTeams().get(2).getNumber());
        ((TextView)findViewById(R.id.blueTeam3)).setOnClickListener(teamClickListener);

        ((TextView) findViewById(R.id.redTeamScore)).setText(Integer.toString(match.getOfficialRedScore()));
        ((TextView) findViewById(R.id.blueTeamScore)).setText(Integer.toString(match.getOfficialBlueScore()));

        ((TextView) findViewById(R.id.redTeamPredictedScore)).setText("");
        ((TextView) findViewById(R.id.blueTeamPredictedScore)).setText("");


        this.setTitle("Match " + match.getMatch());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            Log.e(Constants.LOG_TAG, "home pressed");
            startActivity(new Intent(MatchDetailActivity.this, MainActivity.class));
        }
        return true;
    }

}
