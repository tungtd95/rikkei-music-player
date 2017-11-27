package vn.edu.hust.set.tung.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.custom.AlbumAdapter;
import vn.edu.hust.set.tung.musicplayer.custom.ItemDecorationAlbumColumns;
import vn.edu.hust.set.tung.musicplayer.custom.RecyclerItemClickListener;
import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.SongManagerObserver;

import static vn.edu.hust.set.tung.musicplayer.activity.MainActivity.TAG;

/**
 * Created by tungt on 11/24/17.
 */

public class AlbumFragment extends Fragment implements SongManagerObserver{

    private static final int GRID_COUNT = 2;
    private static final int GRID_SIZE = 15;

    private ArrayList<Album> mListAlbum;
    private AlbumAdapter mAlbumAdapter;
    private RecyclerView rvListAlbum;

    public AlbumFragment() {
        mListAlbum = new ArrayList<>();
        mAlbumAdapter = new AlbumAdapter(mListAlbum);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        rvListAlbum = view.findViewById(R.id.rvListAlbum);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GRID_COUNT);
        rvListAlbum.addItemDecoration(new ItemDecorationAlbumColumns(GRID_SIZE, GRID_COUNT));
        rvListAlbum.setLayoutManager(gridLayoutManager);
        rvListAlbum.setAdapter(mAlbumAdapter);
        rvListAlbum.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                rvListAlbum,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "album item clicked: " + position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        return view;
    }

    @Override
    public void updateSong(ArrayList<Song> listSong, int indexSong) {

    }

    @Override
    public void updateListSong(ArrayList<Song> listSong) {

    }

    @Override
    public void updateListAlbum(ArrayList<Album> listAlbum) {
        mListAlbum = listAlbum;
        mAlbumAdapter.setListAlbum(mListAlbum);
    }

    @Override
    public void updateListArtist(ArrayList<Artist> listArtist) {

    }
}
