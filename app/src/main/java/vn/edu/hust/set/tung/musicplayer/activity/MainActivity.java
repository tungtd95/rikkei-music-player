package vn.edu.hust.set.tung.musicplayer.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.custom.SongAdapter;
import vn.edu.hust.set.tung.musicplayer.custom.ListSongChangedListener;
import vn.edu.hust.set.tung.musicplayer.fragment.AlbumFragment;
import vn.edu.hust.set.tung.musicplayer.fragment.ArtistFragment;
import vn.edu.hust.set.tung.musicplayer.fragment.SongFragment;
import vn.edu.hust.set.tung.musicplayer.model.manager.PlayManager;
import vn.edu.hust.set.tung.musicplayer.model.manager.SongManager;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.PlayManagerObserver;
import vn.edu.hust.set.tung.musicplayer.util.SongHelper;

public class MainActivity extends AppCompatActivity
        implements SlidingUpPanelLayout.PanelSlideListener, ListSongChangedListener, PlayManagerObserver {

    private static final int KEY_REQUEST_PERMISSION = 22;
    public static final String TAG = "main";

    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private LinearLayout llMusicController;
    private LinearLayout llMusicNavigator;
    private RecyclerView rvRecentListSong;

    private SongFragment songFragment;
    private ArtistFragment artistFragment;
    private AlbumFragment albumFragment;
    private FragmentTransaction ft;
    private SongAdapter mSongAdapter;

    private SongManager mSongManager;
    private PlayManager mPlayManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mSlidingUpPanelLayout = findViewById(R.id.slidingUpPanel);
        mSlidingUpPanelLayout.addPanelSlideListener(this);
        llMusicController = findViewById(R.id.llMusicController);
        llMusicNavigator = findViewById(R.id.llMusicPlayingNavigator);
        rvRecentListSong = findViewById(R.id.rvRecentListSong);

        mSongManager = new SongManager();
        songFragment = new SongFragment();
        artistFragment = new ArtistFragment();
        albumFragment = new AlbumFragment();
        mSongManager.register(songFragment);
        mSongManager.register(artistFragment);
        mSongManager.register(albumFragment);
        songFragment.setSongChangedListener(this);

        mPlayManager = new PlayManager();
        mPlayManager.register(this);
        mSongAdapter = new SongAdapter(mPlayManager.getListSong());
        rvRecentListSong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvRecentListSong.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvRecentListSong.setAdapter(mSongAdapter);

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
        // as you specify play_music_design parent activity in AndroidManifest.xml.
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
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
    public void updateSong(ArrayList<Song> listSong, int index) {
        mSongAdapter.setListSong(listSong);
        mPlayManager.updateListSong(listSong, index);
    }

    @Override
    public void updateSong(Song song) {
        Log.i(TAG, "Song changed: " + song.getName());
    }
}
