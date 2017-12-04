package vn.edu.hust.set.tung.musicplayer.custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import vn.edu.hust.set.tung.musicplayer.activity.MainActivity;

/**
 * Created by tungt on 12/04/17.
 */

public class MyBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
