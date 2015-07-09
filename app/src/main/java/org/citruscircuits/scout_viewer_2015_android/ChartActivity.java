package org.citruscircuits.scout_viewer_2015_android;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

import org.citruscircuits.realm.Match;
import org.citruscircuits.realm.TeamInMatchData;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class ChartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        this.setTitle("Team " + getIntent().getStringExtra("teamNum"));

        BarChart chart = (BarChart) findViewById(R.id.chart);

        chart.setDrawYValues(true);
        chart.setDrawValueAboveBar(true);
        chart.setDescription("");
        chart.set3DEnabled(false);
        chart.setPinchZoom(false);
        chart.setHighlightEnabled(false);

        chart.setDrawGridBackground(false);
        chart.setDrawHorizontalGrid(true);
        chart.setDrawVerticalGrid(false);

        chart.setDrawBorder(false);

        chart.animateY(1337);

        XLabels xl = chart.getXLabels();
        xl.setPosition(XLabels.XLabelPosition.BOTTOM);
        xl.setCenterXLabelText(true);

        YLabels yl = chart.getYLabels();
        yl.setLabelCount(8);
        yl.setPosition(YLabels.YLabelPosition.BOTH_SIDED);

        ArrayList<BarEntry> valsComp1 = new ArrayList<>();



        Realm realm = Realm.getInstance(this, Constants.REALM_FILE);
        RealmQuery<TeamInMatchData> teamMatchQuery1 = realm.where(TeamInMatchData.class);
        teamMatchQuery1.equalTo("team.number", Integer.parseInt(getIntent().getStringExtra("teamNum")));
        RealmResults<TeamInMatchData> result1 = teamMatchQuery1.findAll();

        int index = 0;
        String key = getIntent().getStringExtra("realmKey");
        Log.e(Constants.LOG_TAG, key);

        for (TeamInMatchData teamMatches : result1) {
            if (key.equals("numContainersMovedIntoAutoZone")) {  valsComp1.add(new BarEntry(teamMatches.getUploadedData().getNumContainersMovedIntoAutoZone(), index)); }
            if (key.equals("numTotesStacked")) {                 valsComp1.add(new BarEntry(teamMatches.getUploadedData().getNumTotesStacked(), index)); }
            if (key.equals("numReconLevels")) {                  valsComp1.add(new BarEntry(teamMatches.getUploadedData().getNumReconLevels(), index)); }
            if (key.equals("numNoodlesContributed")) {           valsComp1.add(new BarEntry(teamMatches.getUploadedData().getNumNoodlesContributed(), index)); }
            if (key.equals("numReconsStacked")) {                valsComp1.add(new BarEntry(teamMatches.getUploadedData().getNumReconsStacked(), index)); }
            if (key.equals("numReconsPickedUp")) {               valsComp1.add(new BarEntry(teamMatches.getCalculatedData().getNumReconsPickedUp(), index)); }
            if (key.equals("numTotesPickedUpFromGround")) {      valsComp1.add(new BarEntry(teamMatches.getUploadedData().getNumTotesPickedUpFromGround(), index)); }
            if (key.equals("numLitterDropped")) {                valsComp1.add(new BarEntry(teamMatches.getUploadedData().getNumLitterDropped(), index)); }
            if (key.equals("numStacksDamaged")) {                valsComp1.add(new BarEntry(teamMatches.getUploadedData().getNumStacksDamaged(), index)); }
            if (key.equals("maxFieldToteHeight")) {              valsComp1.add(new BarEntry(teamMatches.getUploadedData().getMaxFieldToteHeight(), index)); }
            Log.e(Constants.LOG_TAG, teamMatches + "");
            index++;
        }



        BarDataSet setComp1 = new BarDataSet(valsComp1, getIntent().getStringExtra("buttonValue"));



        setComp1.setColor(Color.rgb(91, 227, 0));

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(setComp1);

        ArrayList<String> xVals = new ArrayList<>();




        RealmQuery<TeamInMatchData> teamMatchQuery2 = realm.where(TeamInMatchData.class);
        teamMatchQuery2.equalTo("team.number", Integer.parseInt(getIntent().getStringExtra("teamNum")));
        RealmResults<TeamInMatchData> result2 = teamMatchQuery2.findAll();

        for (TeamInMatchData teamMatches : result2) {
            xVals.add(teamMatches.getMatch().getMatch());
            Log.e(Constants.LOG_TAG, teamMatches + "");
        }


        BarData data = new BarData(xVals, dataSets);

        chart.setData(data);

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
            startActivity(new Intent(ChartActivity.this, MainActivity.class));
        }
        return true;
    }
}
