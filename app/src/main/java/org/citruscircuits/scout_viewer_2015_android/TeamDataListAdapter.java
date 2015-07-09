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
import org.citruscircuits.realm.Team;
import org.citruscircuits.realm.UploadedTeamData;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;


public class TeamDataListAdapter implements ListAdapter {
    Context context;
    String teamNumber;
    Team team;

    public TeamDataListAdapter(Context context, String teamNumber) {
        this.context = context;
        this.teamNumber = teamNumber;

        Realm realm = Realm.getInstance(this.context, Constants.REALM_FILE);
        RealmQuery<Team> teamResult = realm.where(Team.class);
        teamResult.equalTo("number", Integer.parseInt(teamNumber));

        team = teamResult.findFirst();

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
        return MainActivity.tdl.size();
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.tdl.get(position);
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
            rowView = layoutInflater.inflate(R.layout.team_data_row, parent, false);
        }

        TextView keyText = (TextView) rowView.findViewById(R.id.keyText);
        keyText.setText((String)(((ArrayList)getItem(position)).get(0)));

        TextView valueText = (TextView) rowView.findViewById(R.id.valueText);
        Method m1 = null;
        Method m2 = null;
        Object result = null;
        try {
            if ((((ArrayList)getItem(position)).toArray().length == 4)) {
                m1 = Team.class.getMethod("getCalculatedData");
                CalculatedTeamData m1r = (CalculatedTeamData) m1.invoke(team);
                m2 = CalculatedTeamData.class.getMethod((String) (((ArrayList) getItem(position)).get(1)));
                result = m2.invoke(m1r);
            } else {
                m1 = Team.class.getMethod("getUploadedData");
                UploadedTeamData m1r = (UploadedTeamData) m1.invoke(team);
                m2 = UploadedTeamData.class.getMethod((String) (((ArrayList) getItem(position)).get(1)));
                result = m2.invoke(m1r);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Constants.LOG_TAG, "NO SUCH METHOD???!??");
        }
        Log.e("asdf", result.toString());
        if ((((ArrayList)getItem(position)).get(2)) == "float") {
            valueText.setText(Float.toString((float)result));
        } else if ((((ArrayList)getItem(position)).get(2)) == "string") {
            valueText.setText((String)result);
        } else if ((((ArrayList)getItem(position)).get(2)) == "int") {
            valueText.setText(Integer.toString((Integer)result));
        } else if ((((ArrayList)getItem(position)).get(2)) == "boolean") {
            valueText.setText(Boolean.toString((boolean)result));
        }

        TextView headerText = (TextView) rowView.findViewById(R.id.header);
        headerText.setVisibility(View.GONE);
        if (!((String)((ArrayList)getItem(position)).get(3)).equals("FALSE")) {
            headerText.setVisibility(View.VISIBLE);
            headerText.setText((String)(((ArrayList)getItem(position)).get(3)));
        }

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
