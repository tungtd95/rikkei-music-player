package vn.edu.hust.set.tung.musicplayer.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.triggertrap.seekarc.SeekArc;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.custom.ObjectAdapter;
import vn.edu.hust.set.tung.musicplayer.custom.RecyclerItemClickListener;
import vn.edu.hust.set.tung.musicplayer.custom.SongAdapter;
import vn.edu.hust.set.tung.musicplayer.fragment.AlbumFragment;
import vn.edu.hust.set.tung.musicplayer.fragment.ArtistFragment;
import vn.edu.hust.set.tung.musicplayer.fragment.SongFragment;
import vn.edu.hust.set.tung.musicplayer.model.manager.NManager;
import vn.edu.hust.set.tung.musicplayer.model.manager.PlayManager;
import vn.edu.hust.set.tung.musicplayer.model.manager.SongManager;
import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.PlayManagerObserver;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.NormalState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.RepeatingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.ShufferingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.ShuffleRepeat;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.State;
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.DisplayAlbumDetailListener;
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.DisplayArtistDetailListener;
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.FragmentViewChangedListener;
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.ListSongChangedListener;
import vn.edu.hust.set.tung.musicplayer.util.Finals;
import vn.edu.hust.set.tung.musicplayer.util.SongHelper;

import static vn.edu.hust.set.tung.musicplayer.model.manager.NManager.ACTION_NEXT;
import static vn.edu.hust.set.tung.musicplayer.model.manager.NManager.ACTION_PLAY;
import static vn.edu.hust.set.tung.musicplayer.model.manager.NManager.ACTION_PREVIOUS;

public class MainActivity extends AppCompatActivity
        implements SlidingUpPanelLayout.PanelSlideListener,
        ListSongChangedListener, PlayManagerObserver,
        DisplayAlbumDetailListener, DisplayArtistDetailListener,
        FragmentViewChangedListener {

    private static final int KEY_REQUEST_PERMISSION = 22;
//    public static final String TAG = "main";
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private LinearLayout llMusicController;
    private LinearLayout llMusicNavigator;
    private TextView tvSongPlaying;
    private TextView tvArtistPlaying;
    private ImageView ivPlayController;
    private FloatingActionButton fabPlayController;
    private ImageView ivShuffle;
    private ImageView ivRepeatOne;
    private TextView tvProgressTime;
    private ProgressBar pbController;
    private CoordinatorLayout clMainContent;
    private CoordinatorLayout clListSongDetail;
    private ImageView ivCoverDetail;
    private CoordinatorLayout clSortPlayingList;
    private MenuItem miChangeView;
    private TabLayout tabLayout;
    private SeekArc seekBarPlaying;
    private SearchView mSearchMain;
    private SearchView svSearchPlayingList;
    private TextView tvSongNamePlaying;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar tbListDetail;
    private SearchView svDetailList;

    private SongFragment songFragment;
    private ArtistFragment artistFragment;
    private AlbumFragment albumFragment;
    private FragmentTransaction ft;
    private SongAdapter mSongAdapter;
    private SongManager mSongManager;
    private static PlayManager mPlayManager;
    private ObjectAdapter mObjectAdapterSearching;
    private SongAdapter mSongSearchPlayListAdapter;
    private SongAdapter mSongListDetailAdapter;
    private SharedPreferences preferences;
    private ArrayList<Song> listSongOrigin;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayManager.MyBinder myBinder = (PlayManager.MyBinder) iBinder;
            mPlayManager = myBinder.getPlayManagerService();
            mPlayManager.register(MainActivity.this);
            NManager mNManager = new NManager(MainActivity.this);
            mPlayManager.register(mNManager);
            ArrayList<Song> listSong = getLastListSong();
            int indexLast = getLastSongIndex();
            if (listSong != null && listSong.size() > 0) {
                preferences = getSharedPreferences(Finals.KEY_SHARED_FILE, MODE_PRIVATE);
                mNManager.setSong(listSong.get(indexLast));
                mPlayManager.setUpLastState(listSong, indexLast, preferences.getInt(Finals.KEY_PLAY_MANAGER_PROGRESS, 0));
            }
            int lastPlayManagerState = getLastPlayManagerState();
            if (lastPlayManagerState == Finals.PLAY_MANAGER_REPEAT) {
                mPlayManager.handleRepeatOne();
            } else if (lastPlayManagerState == Finals.PLAY_MANAGER_SHUFFLE) {
                mPlayManager.handleShuffle();
            } else if (lastPlayManagerState == Finals.PLAY_MANAGER_SHUFFLE_REPEAT) {
                mPlayManager.handleRepeatOne();
                mPlayManager.handleShuffle();
            }
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

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabs);
        mSlidingUpPanelLayout = findViewById(R.id.slidingUpPanel);
        mSlidingUpPanelLayout.addPanelSlideListener(this);
        llMusicController = findViewById(R.id.llMusicController);
        llMusicNavigator = findViewById(R.id.llMusicPlayingNavigator);
        RecyclerView rvRecentListSong = findViewById(R.id.rvRecentListSong);
        ImageView ivNextSong = findViewById(R.id.ivNextSong);
        ImageView ivPreviousSong = findViewById(R.id.ivPreviousSong);
        tvSongPlaying = findViewById(R.id.tvSongPlaying);
        tvArtistPlaying = findViewById(R.id.tvArtistPlaying);
        ivPlayController = findViewById(R.id.ivPlayController);
        fabPlayController = findViewById(R.id.fabPlayController);
        ivShuffle = findViewById(R.id.ivShuffle);
        ivRepeatOne = findViewById(R.id.ivRepeatOne);
        tvProgressTime = findViewById(R.id.tvProgressTime);
        pbController = findViewById(R.id.pbController);
        clMainContent = findViewById(R.id.clMainContent);
        clListSongDetail = findViewById(R.id.clListSongDetail);
        ivCoverDetail = findViewById(R.id.ivCoverDetail);
        RecyclerView rvListSorting = findViewById(R.id.rvListSorting);
        clSortPlayingList = findViewById(R.id.clSortPlayingList);
        LinearLayout llMusicPlaying = findViewById(R.id.llMusicPlaying);
        seekBarPlaying = findViewById(R.id.seekBarPlaying);
        RecyclerView rvListSearching = findViewById(R.id.rvListSearching);
        svSearchPlayingList = findViewById(R.id.svSearchPlayingList);
        ((EditText) svSearchPlayingList.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setTextColor(getResources().getColor(R.color.colorWhite));
        RecyclerView rvSearchPlayingListMusic = findViewById(R.id.rvSearchPlayingListMusic);
        tvSongNamePlaying = findViewById(R.id.tvSongNamePlaying);
        RelativeLayout rlPlayingBack = findViewById(R.id.rlPlayingBack);
        RelativeLayout rlReorder = findViewById(R.id.rlReorder);
        RelativeLayout rlSortingBack = findViewById(R.id.rlSortingBack);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        tbListDetail = findViewById(R.id.tbListDetail);
        RecyclerView rvListDetail = findViewById(R.id.rvListDetail);

        tbListDetail.inflateMenu(R.menu.menu_detail);
        tbListDetail.setNavigationIcon(R.drawable.ic_arrow_back);
        tbListDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMain();
            }
        });

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
        artistFragment.setFragmentViewChangedListener(this);
        albumFragment.setFragmentViewChangedListener(this);
        preferences = getSharedPreferences(Finals.KEY_SHARED_FILE, MODE_PRIVATE);
        albumFragment.setGrid(preferences.getBoolean(Finals.KEY_FRAGMENT_ALBUM_STATUS, true));
        artistFragment.setGrid(preferences.getBoolean(Finals.KEY_FRAGMENT_ARTIST_STATUS, true));
        mSongListDetailAdapter = new SongAdapter(new ArrayList<Song>());
        listSongOrigin = new ArrayList<>();

        rvListDetail.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));
        rvListDetail.addItemDecoration(new DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL));
        rvListDetail.setAdapter(mSongListDetailAdapter);
        rvListDetail.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                rvListDetail,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Song s = mSongListDetailAdapter.getListSong().get(position);
                        for (int i = 0; i < listSongOrigin.size(); i++) {
                            if (s.equals(listSongOrigin.get(i))) {
                                updateListSong(listSongOrigin, i);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));

        mSongAdapter = new SongAdapter(new ArrayList<Song>());
        mObjectAdapterSearching = new ObjectAdapter(new ArrayList<>());
        mSongSearchPlayListAdapter = new SongAdapter(new ArrayList<Song>());

        rvSearchPlayingListMusic.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));
        rvSearchPlayingListMusic.setAdapter(mSongSearchPlayListAdapter);
        rvSearchPlayingListMusic.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                rvSearchPlayingListMusic,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Song s = mSongSearchPlayListAdapter.getListSong().get(position);
                        for (int i = 0; i < mSongAdapter.getListSong().size(); i++) {
                            if (s.equals(mSongAdapter.getListSong().get(i))) {
                                mPlayManager.updateSong(i);
                                svSearchPlayingList.setIconified(true);
                                svSearchPlayingList.onActionViewCollapsed();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
        svSearchPlayingList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    mSongSearchPlayListAdapter.setListSong(new ArrayList<Song>());
                    return false;
                }
                mSongSearchPlayListAdapter.setListSong(SongHelper.searchSong(mSongAdapter.getListSong(), newText));
                return false;
            }
        });

        rvListSearching.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));
        rvListSearching.setAdapter(mObjectAdapterSearching);
        rvListSearching.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                rvListSearching,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Object object = mObjectAdapterSearching.getListObj().get(position);
                        if (object instanceof Song) {
                            Song song = (Song) object;
                            ArrayList<Song> songs = new ArrayList<>();
                            songs.add(song);
                            updateListSong(songs, 0);
                        } else if (object instanceof Artist) {
                            Artist artist = (Artist) object;
                            displayArtistDetail(artist);
                        } else if (object instanceof Album) {
                            Album album = (Album) object;
                            displayAlbumDetail(album);
                        }
                        toolbar.collapseActionView();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));

        ArrayList<Song> listSong = getLastListSong();
        final int indexLast = getLastSongIndex();
        if (listSong != null && listSong.size() > 0) {
            mSongAdapter.setListSong(listSong);
            mSongAdapter.setIndexCurrentSong(indexLast);
            mSongAdapter.notifyDataSetChanged();

            Song song = listSong.get(indexLast);
            tvSongPlaying.setText(song.getName().trim());
            tvArtistPlaying.setText(song.getArtist().trim());
            tvSongNamePlaying.setText(song.getName());
        }

        llMusicPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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

        rlReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySortingList();
            }
        });
        rvListSorting.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));
        rvListSorting.addItemDecoration(new DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
        ));
        rvListSorting.setAdapter(mSongAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            int indexCurrentBefore;
            Song songCurrentBefore;

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                indexCurrentBefore = mSongAdapter.getIndexCurrentSong();
                songCurrentBefore = mSongAdapter.getListSong().get(indexCurrentBefore);
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(mSongAdapter.getListSong(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                mSongAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                for (int i = 0; i < mSongAdapter.getListSong().size(); i++) {
                    if (songCurrentBefore.equals(mSongAdapter.getListSong().get(i))) {
                        mPlayManager.setIndexCurrentSong(i);
                        mPlayManager.setListSong(mSongAdapter.getListSong());
                        mSongAdapter.setIndexCurrentSong(i);
                        break;
                    }
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        ItemTouchHelper ith = new ItemTouchHelper(callback);
        ith.attachToRecyclerView(rvListSorting);

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
                    if (miChangeView != null) {
                        miChangeView.setIcon(R.drawable.ic_view_list);
                    }
                } else if (tab.getPosition() == 1) {
                    ft.replace(R.id.flContainer, albumFragment);
                    ft.commit();
                    setAlbumActionIcon();
                } else if (tab.getPosition() == 2) {
                    ft.replace(R.id.flContainer, artistFragment);
                    ft.commit();
                    setArtistActionIcon();
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

        rlPlayingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMain();
            }
        });

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

        ivPlayController.setOnClickListener(new View.OnClickListener() {
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

        rlSortingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMain();
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        seekBarPlaying.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int i, boolean b) {
                if (b) {
                    mPlayManager.handleSeek(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        BroadcastHandler handler = new BroadcastHandler();
        registerReceiver(handler, intentFilter);
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (manager != null) {
            manager.registerMediaButtonEventReceiver(new ComponentName(this, BroadcastHandler.class));
        }


        svDetailList = (SearchView) tbListDetail.getMenu().findItem(R.id.action_search_detail).getActionView();
        svDetailList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSongListDetailAdapter.setListSong(SongHelper.searchSong(listSongOrigin, newText));
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        miChangeView = menu.findItem(R.id.action_change_view);
        MenuItem miSearch = menu.findItem(R.id.action_search);
        mSearchMain = (SearchView) miSearch.getActionView();
        mSearchMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    mObjectAdapterSearching.setListObj(new ArrayList<>());
                    return false;
                }

                ArrayList<Object> listObj = SongHelper.searchAll(
                        mSongManager.getListSong(),
                        mSongManager.getListArtist(),
                        mSongManager.getListAlbum(), newText);
                if (!mSearchMain.isIconified())
                    mObjectAdapterSearching.setListObj(listObj);
                return false;
            }
        });
        mSearchMain.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mObjectAdapterSearching.setListObj(new ArrayList<>());
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify component_play_music_screen parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_view) {
            if (tabLayout.getSelectedTabPosition() == 1) {
                albumFragment.setGrid(!albumFragment.isGrid());
            } else if (tabLayout.getSelectedTabPosition() == 2) {
                artistFragment.setGrid(!artistFragment.isGrid());
            }
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
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * request permission from user
     */
    public void requestPermission() {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
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
            mSongManager.setListSong(songHelper.getListSong());
            mSongManager.setListAlbum(songHelper.getListAlbum(mSongManager.getListSong()));
            mSongManager.setListArtist(songHelper.getListArtist(mSongManager.getListSong()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * fade music controller and music navigator depend on slide offset
     */
    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        llMusicController.setAlpha(1 - slideOffset);
        llMusicNavigator.setAlpha(slideOffset);
    }

    /**
     * display or hide music navigator and music controller depend on slide state
     */
    @Override
    public void onPanelStateChanged(
            View panel,
            SlidingUpPanelLayout.PanelState previousState,
            SlidingUpPanelLayout.PanelState newState
    ) {
        if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            llMusicNavigator.setVisibility(View.INVISIBLE);
            llMusicController.setVisibility(View.VISIBLE);
        } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            llMusicController.setVisibility(View.INVISIBLE);
            llMusicNavigator.setVisibility(View.VISIBLE);
        } else {
            llMusicNavigator.setVisibility(View.VISIBLE);
            llMusicController.setVisibility(View.VISIBLE);
        }

    }

    /**
     * update song for music controller and play manager
     */
    @Override
    public void updateListSong(ArrayList<Song> listSong, int index) {
        mSongAdapter.setListSong(listSong);
        mPlayManager.updateListSong(listSong, index);
    }

    /**
     * play manager notify about song changed
     */
    @Override
    public void updateSong(Song song, int index) {
        tvSongPlaying.setText(song.getName().trim());
        tvArtistPlaying.setText(song.getArtist().trim());
        tvSongNamePlaying.setText(song.getName());
        mSongAdapter.setIndexCurrentSong(index);
        mSongAdapter.notifyDataSetChanged();
        try {
            saveToSharedPref();
        } catch (Exception ignored) {
        }
    }

    /**
     * observable notify about playing or not playing state
     *
     * @param isPlaying playing state
     */
    @Override
    public void updatePlayingState(boolean isPlaying) {
        if (isPlaying) {
            ivPlayController.setBackground(getResources().getDrawable(R.drawable.ic_pause));
            fabPlayController.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_primary));
        } else {
            ivPlayController.setBackground(getResources().getDrawable(R.drawable.ic_play));
            fabPlayController.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_primary));
        }
    }

    /**
     * observable notify about shuffle and repeat function changed
     *
     * @param state shuffle of repeat state
     */
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

    /**
     * observable notify about song progress changed
     *
     * @param progress current song progress
     */
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
                    tvProgressTime.setText(String.format("%s:%s", min, sec));
                } else {
                    tvProgressTime.setText(String.format("%s:%s:%s", hou, min, sec));
                }
            }
        });
    }

    /**
     * observable notify about song progress changed
     *
     * @param progress current song progress
     * @param duration current song duration
     */
    @Override
    public void updateForProgressBar(final int progress, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbController.setMax(duration);
                pbController.setProgress(progress);
                seekBarPlaying.setProgress(progress * 100 / duration);
                saveProgress(progress);
            }
        });
    }

    /**
     * observable notify about display list song of an artist
     *
     * @param artist selected artist
     */
    @Override
    public void displayArtistDetail(Artist artist) {
        displayListSongDetail(artist.getName());
        if (artist.getBitmapCover() != null) {
            ivCoverDetail.setImageBitmap(artist.getBitmapCover());
        } else {
            ivCoverDetail.setImageDrawable(getResources().getDrawable(R.drawable.ic_artist_accent));
        }
        mSongListDetailAdapter.setListSong(artist.getListSong());
        listSongOrigin = artist.getListSong();
    }

    /**
     * observable notify about display list song of an album
     *
     * @param album selected album
     */
    @Override
    public void displayAlbumDetail(Album album) {
        displayListSongDetail(album.getName());
        if (album.getBitmapCover() != null) {
            ivCoverDetail.setImageBitmap(album.getBitmapCover());
        } else {
            ivCoverDetail.setImageDrawable(getResources().getDrawable(R.drawable.ic_album_accent));
        }
        mSongListDetailAdapter.setListSong(album.getListSong());
        listSongOrigin = album.getListSong();
    }

    /**
     * display song manager screen
     */
    public void displayMain() {
        clMainContent.setVisibility(View.VISIBLE);
        clListSongDetail.setVisibility(View.INVISIBLE);
        clSortPlayingList.setVisibility(View.INVISIBLE);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mSongAdapter.setSorting(false);
    }

    /**
     * display list song and description of album or artist
     *
     * @param description album name or artist name
     */
    public void displayListSongDetail(String description) {
        collapsingToolbar.setTitle(description);
        clMainContent.setVisibility(View.INVISIBLE);
        clListSongDetail.setVisibility(View.VISIBLE);
        clSortPlayingList.setVisibility(View.INVISIBLE);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mSongAdapter.setSorting(false);
        svDetailList.setQuery("", false);
        svDetailList.setIconified(true);
        tbListDetail.collapseActionView();
    }

    /**
     * display sorting playing list screen
     */
    public void displaySortingList() {
        clMainContent.setVisibility(View.INVISIBLE);
        clListSongDetail.setVisibility(View.INVISIBLE);
        clSortPlayingList.setVisibility(View.VISIBLE);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mSongAdapter.setSorting(true);
    }

    /**
     * correspond to switch between grid view and list view of fragment
     *
     * @param fragment album fragment or artist fragment
     */
    @Override
    public void changedViewFragment(Fragment fragment) {
        if (tabLayout.getSelectedTabPosition() == 0) {
            return;
        }
        if (fragment instanceof AlbumFragment) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.detach(albumFragment);
            ft.attach(albumFragment);
            ft.commit();
            setAlbumActionIcon();
        } else if (fragment instanceof ArtistFragment) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.detach(artistFragment);
            ft.attach(artistFragment);
            ft.commit();
            setArtistActionIcon();
        }
    }

    public void setAlbumActionIcon() {
        if (albumFragment.isGrid()) {
            miChangeView.setIcon(R.drawable.ic_view_list);
        } else {
            miChangeView.setIcon(R.drawable.ic_view_grid);
        }
    }

    public void setArtistActionIcon() {
        if (artistFragment.isGrid()) {
            miChangeView.setIcon(R.drawable.ic_view_list);
        } else {
            miChangeView.setIcon(R.drawable.ic_view_grid);
        }
    }

    /**
     * do not allow android system destroy activity when press back button
     */
    @Override
    public void onBackPressed() {
        if (clSortPlayingList.getVisibility() == View.VISIBLE
                || clListSongDetail.getVisibility() == View.VISIBLE
                || mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            displayMain();
            return;
        }
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveToSharedPref();
        super.onSaveInstanceState(outState);
    }

    /**
     * save playing progress to shared preference
     *
     * @param progress current song progress
     */
    public void saveProgress(int progress) {
        preferences.edit().putInt(Finals.KEY_PLAY_MANAGER_PROGRESS, progress).apply();
    }

    /**
     * save playing list song and song to shared preference
     */
    public void saveToSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(Finals.KEY_SHARED_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonArr = new Gson().toJson(mPlayManager.getListSong());
        editor.putString(Finals.KEY_LAST_LIST_SONG, jsonArr);
        editor.putInt(Finals.KEY_LAST_INDEX, mPlayManager.getIndexCurrentSong());
        editor.putBoolean(Finals.KEY_FRAGMENT_ALBUM_STATUS, albumFragment.isGrid());
        editor.putBoolean(Finals.KEY_FRAGMENT_ARTIST_STATUS, artistFragment.isGrid());
        int playState = 0;
        if (mPlayManager.getState() instanceof ShufferingState) {
            playState = Finals.PLAY_MANAGER_SHUFFLE;
        } else if (mPlayManager.getState() instanceof RepeatingState) {
            playState = Finals.PLAY_MANAGER_REPEAT;
        } else if (mPlayManager.getState() instanceof ShuffleRepeat) {
            playState = Finals.PLAY_MANAGER_SHUFFLE_REPEAT;
        }
        editor.putInt(Finals.KEY_PLAY_MANAGER_STATE, playState);
        editor.apply();
    }

    /**
     * get last playing state
     *
     * @return (shuffle, repeat...)
     */
    public int getLastPlayManagerState() {
        SharedPreferences preferences = getSharedPreferences(Finals.KEY_SHARED_FILE, MODE_PRIVATE);
        return preferences.getInt(Finals.KEY_PLAY_MANAGER_STATE, 0);
    }

    /**
     * get last playing list song
     *
     * @return list song
     */
    public ArrayList<Song> getLastListSong() {
        SharedPreferences sharedPreferences = getSharedPreferences(Finals.KEY_SHARED_FILE, MODE_PRIVATE);
        String jsonArr = sharedPreferences.getString(Finals.KEY_LAST_LIST_SONG, null);
        if (jsonArr == null) {
            return null;
        }
        ArrayList<Song> listSong = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONArray(jsonArr);
            for (int i = 0; i < jsonArray.length(); i++) {
                Song s = gson.fromJson(jsonArray.getString(i), Song.class);
                listSong.add(s);
            }
            return listSong;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * get last index of playing song
     *
     * @return last song index before turn of the app
     */
    public int getLastSongIndex() {
        SharedPreferences sharedPreferences = getSharedPreferences(Finals.KEY_SHARED_FILE, MODE_PRIVATE);
        return sharedPreferences.getInt(Finals.KEY_LAST_INDEX, 0);
    }

    /**
     * handle notification event and headset event
     */
    public static class BroadcastHandler extends BroadcastReceiver {

        private static boolean lastStateByPhone = false;
        private static boolean lastStateByHeadset = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            if (action.equals(Intent.ACTION_MEDIA_BUTTON)) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }
                KeyEvent keyEvent = (KeyEvent) bundle.get(Intent.EXTRA_KEY_EVENT);
                if (keyEvent == null) {
                    return;
                }
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    mPlayManager.handlePlayingState();
                }
                return;
            }
            if (action.equals("android.intent.action.PHONE_STATE")) {
                String state = intent.getStringExtra("state");
                if (mPlayManager != null) {
                    if (state.equals("RINGING") || state.equals("OFFHOOK")) {
                        lastStateByPhone = mPlayManager.isPlaying();
                        mPlayManager.handlePause();
                    } else if (state.equals("IDLE") && lastStateByPhone) {
                        mPlayManager.handlePlay();
                    }
                }
                return;
            }
            if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
                int status = intent.getIntExtra("state", -1);
                if (status == 0 && mPlayManager != null) {
                    lastStateByHeadset = mPlayManager.isPlaying();
                    mPlayManager.handlePause();
                } else if (status == 1 && mPlayManager != null && lastStateByHeadset) {
                    mPlayManager.handlePlay();
                }
                return;
            }
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
            }
        }
    }

}
