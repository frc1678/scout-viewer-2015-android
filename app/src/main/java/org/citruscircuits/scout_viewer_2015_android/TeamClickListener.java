package org.citruscircuits.scout_viewer_2015_android;

import android.content.Intent;
import android.util.Log;
import android.view.View;

public class TeamClickListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        Log.e(Constants.LOG_TAG, "Team onClicked: " + view.getTag());
        Intent intent = new Intent(view.getContext(), TeamDetailActivity.class);
        intent.putExtra("teamNum", view.getTag().toString());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        view.getContext().startActivity(intent);
    }
}
