package org.citruscircuits.scout_viewer_2015_android;

import com.dropbox.sync.android.DbxPath;

import org.citruscircuits.realm.Team;

/**
 * Created by citruscircuits on 1/31/15.
 */
public class Utils {
    public static DbxPath getCurrentPath(int teamNumber) {
        DbxPath path = new DbxPath(Constants.ROBOT_PHOTOS_PATH + teamNumber + "/");

        return path;
    }
}
