package org.citruscircuits.scout_viewer_2015_android;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;

import org.citruscircuits.realm.Team;
import org.citruscircuits.realm.TeamInMatchData;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class TeamDetailActivity extends ActionBarActivity {

    public static ArrayList<TeamInMatchData> teamInMatchList = new ArrayList<>();
    public Team team;
    public DbxFileSystem dbxFs;
    public DbxAccountManager mDbxAcctMgr;
    private String appKey = "APP KEY";
    private String appSecret = "APP SECRET";


    ChartClickListener chartClickListener = new ChartClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_team_detail);

        Intent intent = getIntent();

        Realm realm = Realm.getInstance(this, Constants.REALM_FILE);

        RealmQuery<Team> query = realm.where(Team.class);
        query.equalTo("number", Integer.parseInt(intent.getStringExtra("teamNum")));
        team = query.findFirst();

        ((TextView)findViewById(R.id.teamNameText)).setText(team.getName());
        ((TextView)findViewById(R.id.teamNumberText)).setText(Integer.toString(team.getNumber()));

        this.setTitle("Team " + team.getNumber());

        ListView listView = (ListView)this.findViewById(R.id.matchListView);
        listView.setAdapter(new TeamInMatchListAdapter(this.getApplicationContext()));

        findViewById(R.id.chartButton).setTag(Integer.toString(team.getNumber()));
        findViewById(R.id.pitDataButton).setTag(Integer.toString(team.getNumber()));

        teamInMatch();

        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
        try {
            dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
            List<DbxFileInfo> dbxFileInfos = dbxFs.listFolder(Utils.getCurrentPath(team.getNumber()));
            for (DbxFileInfo dbxFileInfo : dbxFileInfos) {
                new PhotoDownloadTask().execute(dbxFs, dbxFileInfo, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chartButtonClick(View view) {
        Intent intent = new Intent(view.getContext(), ChartListActivity.class);
        intent.putExtra("teamNum", view.getTag().toString());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        view.getContext().startActivity(intent);
    }

    public void pitDataButtonClick(View view) {
        Intent intent = new Intent(view.getContext(), TeamDataActivity.class);
        intent.putExtra("teamNum", view.getTag().toString());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        view.getContext().startActivity(intent);
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
            startActivity(new Intent(TeamDetailActivity.this, MainActivity.class));
        }
        return true;
    }

    public void teamInMatch(){
        Realm realm = Realm.getInstance(this, Constants.REALM_FILE);

        RealmQuery<TeamInMatchData> teamMatchQuery = realm.where(TeamInMatchData.class);

        teamMatchQuery.equalTo("team.number", team.getNumber());

        RealmResults<TeamInMatchData> result = teamMatchQuery.findAll();

        teamInMatchList.clear();

        for (TeamInMatchData teamMatches : result) {
            teamInMatchList.add(teamMatches);
        }
    }

}
