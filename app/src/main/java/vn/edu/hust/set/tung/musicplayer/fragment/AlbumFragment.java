package vn.edu.hust.set.tung.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.DisplayAlbumDetailListener;
import vn.edu.hust.set.tung.musicplayer.model.stratergypattern.FragmentViewChangedListener;

public class AlbumFragment extends Fragment implements SongManagerObserver {

    private static final int GRID_COUNT = 2;
    private static final int GRID_SIZE = 13;

    private ArrayList<Album> mListAlbum;
    private AlbumAdapter mAlbumAdapter;
    private DisplayAlbumDetailListener displayAlbumDetailListener;
    private boolean isGrid = true;
    FragmentViewChangedListener fragmentViewChangedListener;
    public void setFragmentViewChangedListener(FragmentViewChangedListener listener) {
        this.fragmentViewChangedListener = listener;
    }

    public AlbumFragment() {
        mListAlbum = new ArrayList<>();
        mAlbumAdapter = new AlbumAdapter(mListAlbum);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        RecyclerView rvListAlbum = view.findViewById(R.id.rvListAlbum);
        mAlbumAdapter.setGrid(isGrid);
        if (isGrid) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GRID_COUNT);
            rvListAlbum.addItemDecoration(new ItemDecorationAlbumColumns(GRID_SIZE, GRID_COUNT));
            rvListAlbum.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rvListAlbum.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            rvListAlbum.setLayoutManager(layoutManager);
        }
        rvListAlbum.setAdapter(mAlbumAdapter);
        rvListAlbum.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                rvListAlbum,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (displayAlbumDetailListener != null) {
                            displayAlbumDetailListener.displayAlbumDetail(mListAlbum.get(position));
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
        mListAlbum = listAlbum;
        mAlbumAdapter.setListAlbum(mListAlbum);
    }

    @Override
    public void updateListArtist(ArrayList<Artist> listArtist) {

    }

    public void setDisplayAlbumDetailListener(DisplayAlbumDetailListener displayAlbumDetailListener) {
        this.displayAlbumDetailListener = displayAlbumDetailListener;
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
