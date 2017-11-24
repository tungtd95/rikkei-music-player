package vn.edu.hust.set.tung.musicplayer.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

/**
 * Created by tungt on 11/24/17.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private ArrayList<Song> listSong;

    public SongAdapter(ArrayList<Song> listSong) {
        this.listSong = listSong;
    }

    public ArrayList<Song> getListSong() {
        return listSong;
    }

    public void setListSong(ArrayList<Song> listSong) {
        this.listSong = listSong;
        notifyDataSetChanged();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        holder.tvArtist.setText(listSong.get(position).getArtist());
        holder.tvSong.setText(listSong.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listSong == null ? 0 : listSong.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        TextView tvSong;
        TextView tvArtist;

        public SongViewHolder(View itemView) {
            super(itemView);
            tvSong = itemView.findViewById(R.id.tvSong);
            tvArtist = itemView.findViewById(R.id.tvArtist);
        }
    }
}
