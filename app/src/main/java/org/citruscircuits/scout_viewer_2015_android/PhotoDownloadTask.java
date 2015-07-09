package org.citruscircuits.scout_viewer_2015_android;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxRuntimeException;

import org.citruscircuits.scout_viewer_2015_android.TeamDetailActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by citruscircuits on 2/14/15.
 */
public class PhotoDownloadTask extends AsyncTask {
    DbxFileInfo fileInfo;
    TeamDetailActivity teamDetailActivity;

    private static int activeThreadCount = 0;

    public static int getActiveThreadCount() {
        return activeThreadCount;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        activeThreadCount++;

        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        DbxFileSystem dbxFs = (DbxFileSystem)params[0];
        fileInfo = (DbxFileInfo)params[1];
        teamDetailActivity = (TeamDetailActivity)params[2];

        try {
            long cacheSize = dbxFs.getMaxFileCacheSize();
            dbxFs.setMaxFileCacheSize(0);
            dbxFs.setMaxFileCacheSize(cacheSize);
        } catch (DbxException.NotFound nfe) {
            //HANDLE ERRORZ
        } catch (DbxException e) {
            //HANDLE ERRORZ
        }

        ImageView newImage = null;
        try {
            DbxFile currentPhoto = dbxFs.openThumbnail(fileInfo.path, DbxFileSystem.ThumbSize.L, DbxFileSystem.ThumbFormat.JPG);
            currentPhoto.update();
            InputStream imageStream = currentPhoto.getReadStream();

            newImage = new ImageView(teamDetailActivity.getApplicationContext());

            newImage.setImageBitmap(BitmapFactory.decodeStream(imageStream));

            imageStream.close();
            currentPhoto.close();

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(5, 5, 5, 5);

            newImage.setLayoutParams(layoutParams);
            newImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } catch (DbxException e) {
            //HANDLE ERRORZ
        } catch (DbxRuntimeException dbxre) {
            //HANDLE ERRORZ
        } catch (IOException ioe) {
            //HANDLE ERRORZ
        }

        return newImage;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o != null) {
            ImageView newImage = (ImageView)o;
            LinearLayout linearLayout = (LinearLayout) teamDetailActivity.findViewById(R.id.robotPhotos);

            //teamDetailActivity.photos.put(newImage.getId(), fileInfo.path.toString());

            if (fileInfo.path.toString().contains("selectedImage")) {
                linearLayout.addView(newImage, 0);
            } else {
                linearLayout.addView(newImage);
            }
            activeThreadCount--;
        }
    }
}
