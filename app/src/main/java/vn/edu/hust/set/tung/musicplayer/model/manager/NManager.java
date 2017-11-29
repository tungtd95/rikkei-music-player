package vn.edu.hust.set.tung.musicplayer.model.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.PlayManagerObservable;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.PlayManagerObserver;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.State;

/**
 * Created by tungt on 11/29/17.
 */

public class NManager implements PlayManagerObserver {

    public static final String ACTION_PLAY = "play";
    public static final String ACTION_NEXT = "next";
    public static final String ACTION_PREVIOUS = "previous";
    public static final int KEY_NOTIFICATION_ID = 1;

    NotificationManager mNotificationManager;
    NotificationCompat.Builder mNotificationBuilder;
    Context mContext;

    public NManager(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mContext = context;
    }

    public void displayNotification(boolean isPlaying) {

        Intent playIntent = new Intent(ACTION_PLAY);
        playIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingPlay = PendingIntent.getBroadcast(mContext, 0, playIntent, 0);

        Intent nextIntent = new Intent(ACTION_NEXT);
        nextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingNext = PendingIntent.getBroadcast(mContext, 0, nextIntent, 0);

        Intent previousIntent = new Intent(ACTION_PREVIOUS);
        previousIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingPrevious = PendingIntent.getBroadcast(mContext, 0, previousIntent, 0);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_controller);
        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.ivNotificationPlay, R.drawable.ic_pause);
        } else {
            remoteViews.setImageViewResource(R.id.ivNotificationPlay, R.drawable.ic_play);
        }
        remoteViews.setTextViewText(R.id.tvNotificationSong, mSong.getName());
        remoteViews.setTextViewText(R.id.tvNotificationArtist, mSong.getArtist());
        remoteViews.setOnClickPendingIntent(R.id.ivNotificationPlay, pendingPlay);
        remoteViews.setOnClickPendingIntent(R.id.ivNotificationNext, pendingNext);
        remoteViews.setOnClickPendingIntent(R.id.ivNotificationPrevious, pendingPrevious);
        mNotificationBuilder = new NotificationCompat.Builder(mContext, "")
                .setCustomBigContentView(remoteViews)
                .setCustomContentView(remoteViews)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher);
        mNotificationManager.notify(KEY_NOTIFICATION_ID, mNotificationBuilder.build());
    }

    Song mSong;

    @Override
    public void updateSong(Song song) {
        mSong = song;
        displayNotification(true);
    }

    @Override
    public void updatePlayingState(boolean isPlaying) {
        displayNotification(isPlaying);
    }

    @Override
    public void updatePlayManagerState(State state) {

    }

    @Override
    public void updatePlayingProgress(int progress) {

    }

    @Override
    public void updateForProgressBar(int progress, int duration) {

    }
}
