package org.citruscircuits.scout_viewer_2015_android;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ChartClickListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        Log.e(Constants.LOG_TAG, "Chart onClicked: " + view.getTag());
        Intent intent = new Intent(view.getContext(), ChartActivity.class);
        intent.putExtra("realmKey", view.getTag().toString().split("-")[0]);
        intent.putExtra("buttonValue", view.getTag().toString().split("-")[1]);
        intent.putExtra("teamNum", view.getTag().toString().split("-")[2]);


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        view.getContext().startActivity(intent);
    }
}
