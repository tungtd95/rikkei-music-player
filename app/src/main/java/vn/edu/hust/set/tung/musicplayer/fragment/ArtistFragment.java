package vn.edu.hust.set.tung.musicplayer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.custom.ArtistAdapter;
import vn.edu.hust.set.tung.musicplayer.custom.ItemDecorationAlbumColumns;
import vn.edu.hust.set.tung.musicplayer.custom.RecyclerItemClickListener;
import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.SongManagerObserver;
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.DisplayArtistDetailListener;
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.FragmentViewChangedListener;
import vn.edu.hust.set.tung.musicplayer.util.Finals;

import static vn.edu.hust.set.tung.musicplayer.activity.MainActivity.TAG;

/**
 * Created by tungt on 11/24/17.
 */

public class ArtistFragment extends Fragment implements SongManagerObserver {

    private static final int GRID_COUNT = 2;
    private static final int GRID_SIZE = 13;

    private ArrayList<Artist> mListArtist;
    private ArtistAdapter mArtistAdapter;
    private RecyclerView rvListArtist;
    private DisplayArtistDetailListener displayArtistDetailListener;
    private boolean isGrid = true;
    FragmentViewChangedListener fragmentViewChangedListener;
    public void setFragmentViewChangedListener(FragmentViewChangedListener listener) {
        this.fragmentViewChangedListener = listener;
    }

    public ArtistFragment() {
        mListArtist = new ArrayList<>();
        mArtistAdapter = new ArtistAdapter(mListArtist);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        rvListArtist = view.findViewById(R.id.rvListArtist);
        mArtistAdapter.setGrid(isGrid);
        if (isGrid) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GRID_COUNT);
            rvListArtist.setLayoutManager(gridLayoutManager);
            rvListArtist.addItemDecoration(new ItemDecorationAlbumColumns(GRID_SIZE, GRID_COUNT));
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rvListArtist.setLayoutManager(layoutManager);
            rvListArtist.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }
        rvListArtist.setAdapter(mArtistAdapter);
        rvListArtist.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                rvListArtist,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (displayArtistDetailListener != null) {
                            displayArtistDetailListener.displayArtistDetail(mListArtist.get(position));
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
        return view;
    }

    @Override
    public void updateListSong(ArrayList<Song> listSong) {

    }

    @Override
    public void updateListAlbum(ArrayList<Album> listAlbum) {

    }

    public void setDisplayArtistDetailListener(DisplayArtistDetailListener displayArtistDetailListener) {
        this.displayArtistDetailListener = displayArtistDetailListener;
    }

    @Override
    public void updateListArtist(ArrayList<Artist> listArtist) {
        mListArtist = listArtist;
        mArtistAdapter.setListArtist(mListArtist);
    }

    public boolean isGrid() {
        return isGrid;
    }

    public void setGrid(boolean grid) {
        isGrid = grid;
        if (fragmentViewChangedListener != null) {
            fragmentViewChangedListener.changedViewFragment(this);
        }
    }
}
