package vn.edu.hust.set.tung.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.custom.RecyclerItemClickListener;
import vn.edu.hust.set.tung.musicplayer.custom.SongAdapter;
import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.SongManagerObserver;

import static vn.edu.hust.set.tung.musicplayer.activity.MainActivity.TAG;

/**
 * Created by tungt on 11/24/17.
 */

public class SongFragment extends Fragment implements SongManagerObserver{

    private ArrayList<Song> mListSong;
    private SongAdapter mSongAdapter;
    private RecyclerView rvListSong;
    private LinearLayoutManager mLinearLayoutManager;

    public SongFragment() {
        mListSong = new ArrayList<>();
        mSongAdapter = new SongAdapter(mListSong);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        rvListSong = view.findViewById(R.id.rvListSong);
        rvListSong.setLayoutManager(mLinearLayoutManager);
        rvListSong.setAdapter(mSongAdapter);
        rvListSong.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvListSong.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                rvListSong,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.i(TAG, "songs fragment clicked: " + position);
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
        this.mListSong = listSong;
        mSongAdapter.setListSong(mListSong);
    }

    @Override
    public void updateListAlbum(ArrayList<Album> listAlbum) {

    }

    @Override
    public void updateListArtist(ArrayList<Artist> listArtist) {

    }
}
