package vn.edu.hust.set.tung.musicplayer.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.custom.RecyclerItemClickListener;
import vn.edu.hust.set.tung.musicplayer.custom.SongAdapter;
import vn.edu.hust.set.tung.musicplayer.model.manager.NManager;
import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.NormalState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.RepeatingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.ShufferingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.ShuffleRepeat;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.State;
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.DisplayAlbumDetailListener;
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.DisplayArtistDetailListener;
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.ListSongChangedListener;
import vn.edu.hust.set.tung.musicplayer.fragment.AlbumFragment;
import vn.edu.hust.set.tung.musicplayer.fragment.ArtistFragment;
import vn.edu.hust.set.tung.musicplayer.fragment.SongFragment;
import vn.edu.hust.set.tung.musicplayer.model.manager.PlayManager;
import vn.edu.hust.set.tung.musicplayer.model.manager.SongManager;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.PlayManagerObserver;
import vn.edu.hust.set.tung.musicplayer.util.SongHelper;

import static vn.edu.hust.set.tung.musicplayer.model.manager.NManager.ACTION_NEXT;
import static vn.edu.hust.set.tung.musicplayer.model.manager.NManager.ACTION_PLAY;
import static vn.edu.hust.set.tung.musicplayer.model.manager.NManager.ACTION_PREVIOUS;

public class MainActivity extends AppCompatActivity
        implements SlidingUpPanelLayout.PanelSlideListener,
        ListSongChangedListener, PlayManagerObserver,
        DisplayAlbumDetailListener, DisplayArtistDetailListener {

    private static final int KEY_REQUEST_PERMISSION = 22;
    public static final String TAG = "main";

    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private LinearLayout llMusicController;
    private LinearLayout llMusicNavigator;
    private RecyclerView rvRecentListSong;
    private ImageView ivNextSong;
    private ImageView ivPreviousSong;
    private TextView tvSongPlaying;
    private TextView tvArtistPlaying;
    private Button btnPlayController;
    private FloatingActionButton fabPlayController;
    private ImageView ivShuffle;
    private ImageView ivRepeatOne;
    private TextView tvProgressTime;
    private ProgressBar pbController;
    private CoordinatorLayout clMainContent;
    private CoordinatorLayout clListSongDetail;
    private Button btnDismiss;

    private SongFragment songFragment;
    private ArtistFragment artistFragment;
    private AlbumFragment albumFragment;
    private FragmentTransaction ft;
    private SongAdapter mSongAdapter;

    private SongManager mSongManager;
    private static PlayManager mPlayManager;
    private NManager mNManager;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayManager.MyBinder myBinder = (PlayManager.MyBinder) iBinder;
            mPlayManager = myBinder.getPlayManagerService();
            mPlayManager.register(MainActivity.this);
            mNManager = new NManager(MainActivity.this);
            mPlayManager.register(mNManager);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, PlayManager.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mSlidingUpPanelLayout = findViewById(R.id.slidingUpPanel);
        mSlidingUpPanelLayout.addPanelSlideListener(this);
        llMusicController = findViewById(R.id.llMusicController);
        llMusicNavigator = findViewById(R.id.llMusicPlayingNavigator);
        rvRecentListSong = findViewById(R.id.rvRecentListSong);
        ivNextSong = findViewById(R.id.ivNextSong);
        ivPreviousSong = findViewById(R.id.ivPreviousSong);
        tvSongPlaying = findViewById(R.id.tvSongPlaying);
        tvArtistPlaying = findViewById(R.id.tvArtistPlaying);
        btnPlayController = findViewById(R.id.btnPlayController);
        fabPlayController = findViewById(R.id.fabPlayController);
        ivShuffle = findViewById(R.id.ivShuffle);
        ivRepeatOne = findViewById(R.id.ivRepeatOne);
        tvProgressTime = findViewById(R.id.tvProgressTime);
        pbController = findViewById(R.id.pbController);
        clMainContent = findViewById(R.id.clMainContent);
        clListSongDetail = findViewById(R.id.clListSongDetail);
        btnDismiss = findViewById(R.id.btnDismiss);

        mSongManager = new SongManager();
        songFragment = new SongFragment();
        artistFragment = new ArtistFragment();
        albumFragment = new AlbumFragment();
        mSongManager.register(songFragment);
        mSongManager.register(artistFragment);
        mSongManager.register(albumFragment);
        songFragment.setSongChangedListener(this);
        artistFragment.setDisplayArtistDetailListener(this);
        albumFragment.setDisplayAlbumDetailListener(this);

        mSongAdapter = new SongAdapter(new ArrayList<Song>());
        rvRecentListSong.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));
        rvRecentListSong.addItemDecoration(new DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
        ));
        rvRecentListSong.setAdapter(mSongAdapter);
        rvRecentListSong.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                rvRecentListSong,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mPlayManager.updateSong(position);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, songFragment);
        ft.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ft = getSupportFragmentManager().beginTransaction();
                if (tab.getPosition() == 0) {
                    ft.replace(R.id.flContainer, songFragment);
                    ft.commit();
                } else if (tab.getPosition() == 1) {
                    ft.replace(R.id.flContainer, albumFragment);
                    ft.commit();
                } else if (tab.getPosition() == 2) {
                    ft.replace(R.id.flContainer, artistFragment);
                    ft.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (permissionOK()) {
            everythingFind();
        } else {
            requestPermission();
        }

        ivNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayManager != null) {
                    mPlayManager.handleNextSong();
                }
            }
        });

        ivPreviousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayManager != null) {
                    mPlayManager.handlePreviousSong();
                }
            }
        });

        btnPlayController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayManager != null) {
                    mPlayManager.handlePlayingState();
                }
            }
        });

        fabPlayController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayManager != null) {
                    mPlayManager.handlePlayingState();
                }
            }
        });

        ivRepeatOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayManager != null) {
                    mPlayManager.handleRepeatOne();
                }
            }
        });

        ivShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayManager != null) {
                    mPlayManager.handleShuffle();
                }
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clMainContent.setVisibility(View.VISIBLE);
                clListSongDetail.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify play_music_screen parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * check read storage permission
     *
     * @return bool
     */
    public boolean permissionOK() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * request permission from user
     */
    public void requestPermission() {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                KEY_REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        if (permissionOK()) {
            everythingFind();
        }
    }

    /**
     * do the right thing after all
     */
    public void everythingFind() {
        try {
            SongHelper songHelper = new SongHelper(this);
            mSongManager.setListAlbum(songHelper.getListAlbum());
            mSongManager.setListArtist(songHelper.getListArtist());
            mSongManager.setListSong(songHelper.getListSong());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * fade music controller and music navigator depend on slide offset
     *
     * @param panel
     * @param slideOffset
     */
    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        llMusicController.setAlpha(1 - slideOffset);
        llMusicNavigator.setAlpha(slideOffset);
    }

    /**
     * display or hide music navigator and music controller depend on slide state
     *
     * @param panel
     * @param previousState
     * @param newState
     */
    @Override
    public void onPanelStateChanged(
            View panel,
            SlidingUpPanelLayout.PanelState previousState,
            SlidingUpPanelLayout.PanelState newState
    ) {
        if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            llMusicNavigator.setVisibility(View.GONE);
            llMusicController.setVisibility(View.VISIBLE);
        } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            llMusicController.setVisibility(View.GONE);
            llMusicNavigator.setVisibility(View.VISIBLE);
        } else {
            llMusicNavigator.setVisibility(View.VISIBLE);
            llMusicController.setVisibility(View.VISIBLE);
        }

    }

    /**
     * update song for music controller and play manager
     *
     * @param listSong
     * @param index
     */
    @Override
    public void updateListSong(ArrayList<Song> listSong, int index) {
        mSongAdapter.setListSong(listSong);
        mPlayManager.updateListSong(listSong, index);
    }

    @Override
    public void updateSong(Song song) {
        tvSongPlaying.setText(song.getName().trim());
        tvArtistPlaying.setText(song.getArtist().trim());
    }

    @Override
    public void updatePlayingState(boolean isPlaying) {
        if (isPlaying) {
            btnPlayController.setBackground(getResources().getDrawable(R.drawable.ic_pause_accent));
            fabPlayController.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_primary));
        } else {
            btnPlayController.setBackground(getResources().getDrawable(R.drawable.ic_play_accent));
            fabPlayController.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_primary));
        }
    }

    @Override
    public void updatePlayManagerState(State state) {
        if (state instanceof NormalState) {
            ivShuffle.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_unavailable));
            ivRepeatOne.setBackground(getResources().getDrawable(R.drawable.ic_repeat_one_unavailable));
            return;
        }
        if (state instanceof ShufferingState) {
            ivShuffle.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_available));
            ivRepeatOne.setBackground(getResources().getDrawable(R.drawable.ic_repeat_one_unavailable));
            return;
        }
        if (state instanceof RepeatingState) {
            ivShuffle.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_unavailable));
            ivRepeatOne.setBackground(getResources().getDrawable(R.drawable.ic_repeat_one_available));
            return;
        }
        if (state instanceof ShuffleRepeat) {
            ivShuffle.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_available));
            ivRepeatOne.setBackground(getResources().getDrawable(R.drawable.ic_repeat_one_available));
        }
    }

    @Override
    public void updatePlayingProgress(int progress) {
        final int second = progress % 60;
        final int minute = progress / 60;
        final int hour = progress / 3600;
        final String sec = second / 10 != 0 ? second + "" : "0" + second;
        final String min = minute / 10 != 0 ? minute + "" : "0" + minute;
        final String hou = hour / 10 != 0 ? hour + "" : "0" + hour;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (hour == 0) {
                    tvProgressTime.setText(min + ":" + sec);
                } else {
                    tvProgressTime.setText(hou + ":" + min + ":" + sec);
                }
            }
        });
    }

    @Override
    public void updateForProgressBar(final int progress, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbController.setMax(duration);
                pbController.setProgress(progress);
            }
        });
    }

    @Override
    public void displayArtistDetail(Artist artist) {
        clMainContent.setVisibility(View.GONE);
        clListSongDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayAlbumDetail(Album album) {
        clMainContent.setVisibility(View.GONE);
        clListSongDetail.setVisibility(View.VISIBLE);
    }

    public static class NotificationHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_PLAY)) {
                if (mPlayManager != null) {
                    mPlayManager.handlePlayingState();
                }
                return;
            }
            if (action.equals(ACTION_NEXT)) {
                if (mPlayManager != null) {
                    mPlayManager.handleNextSong();
                }
                return;
            }
            if (action.equals(ACTION_PREVIOUS)) {
                if (mPlayManager != null) {
                    mPlayManager.handlePreviousSong();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
