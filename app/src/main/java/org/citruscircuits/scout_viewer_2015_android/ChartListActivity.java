package org.citruscircuits.scout_viewer_2015_android;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;


public class ChartListActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_list);

        this.setTitle("Team " + getIntent().getStringExtra("teamNum"));

        ListView listView = (ListView)findViewById(R.id.chartList);
        listView.setAdapter(new ChartListAdapter(this.getApplicationContext(), getIntent().getStringExtra("teamNum")));
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
            startActivity(new Intent(ChartListActivity.this, MainActivity.class));
        }
        return true;
    }

}
