package org.citruscircuits.scout_viewer_2015_android;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class RealmDownloadTask extends AsyncTask {

    MainActivity mainActivity;
    private static int activeThreadCount = 0;
    DbxFile realmFile;
    DbxFileSystem dbxFs;

    public static int getActiveThreadCount() {
        return activeThreadCount;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        activeThreadCount++;

        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        dbxFs = (DbxFileSystem)params[0];
        mainActivity = (MainActivity)params[1];
        return 0;
    }

    @Override
    protected void onPostExecute(Object o) {
        activeThreadCount--;
        super.onPostExecute(o);

        DbxFile dbfile = null;
        try {
            dbfile = dbxFs.open(new DbxPath("/Database File/" + Constants.REALM_FILE));
            FileInputStream stream = dbfile.getReadStream();

            File file = new File(Constants.REALM_FILE);
            file.delete();

            FileOutputStream out = mainActivity.openFileOutput(Constants.REALM_FILE, Context.MODE_PRIVATE);


            int b = 0;

            while ((b = stream.read()) != -1) {
                out.write(b);
            }

            out.close();
            stream.close();
            Log.e(Constants.LOG_TAG, "Got database file \"" + Constants.REALM_FILE + "\"");

        } catch (DbxException e) {
            e.printStackTrace();
            Log.e(Constants.LOG_TAG, "Hit DbxException: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Constants.LOG_TAG, "Hit IOException: " + e.getMessage());
        }
        if (dbfile != null) {
            dbfile.close();
        } else {
            Log.e(Constants.LOG_TAG, "OH GOD EVERYTHING IS FUCKED UP!");
        }
    }
}