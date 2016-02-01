package service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by root on 18/12/15.
 */
public class GetUserLocationService extends Service {
    SharedPreferences sharedPreferences;
    private String user;
    private ParseGeoPoint geoPoint;
    private double lat, longg;
    private String userName;
    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            user = intent.getStringExtra("user");
            sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName", user).commit();
        }
        userName = sharedPreferences.getString("userName", null);
         timer = new Timer();
        if(timer!=null) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", user);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e == null) {
                                for (int i = 0; i < objects.size(); i++)
                                    geoPoint = objects.get(i).getParseGeoPoint("location");
                                if (geoPoint != null) {
                                    sendMessage();
                                }
                            } else {
                                // Something went wrong.
                            }
                        }
                    });

                }
            }, 0, 10000);
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void sendMessage() {
        Intent intent = new Intent("location");
        lat = geoPoint.getLatitude();
        longg = geoPoint.getLongitude();
        intent.putExtra("lat", lat);
        intent.putExtra("longg", longg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        timer.purge();
        timer.cancel();
        timer=null;
    }
}
