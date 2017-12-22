package vn.edu.hust.set.tung.musicplayer.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private ArrayList<Song> listSong;
    private boolean isSorting = false;
    private int indexCurrentSong = -1;

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
        if (isSorting) {
            holder.ivSortingIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.ivSortingIndicator.setVisibility(View.GONE);
        }
        if (holder.getAdapterPosition() == indexCurrentSong) {
            holder.tvSong.setTextColor(holder.tvSong.getResources().getColor(R.color.colorAccent));
        } else {
            holder.tvSong.setTextColor(holder.tvSong.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public int getIndexCurrentSong() {
        return indexCurrentSong;
    }

    public void setIndexCurrentSong(int indexCurrentSong) {
        this.indexCurrentSong = indexCurrentSong;
    }

    @Override
    public int getItemCount() {
        return listSong == null ? 0 : listSong.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        TextView tvSong;
        TextView tvArtist;
        ImageView ivSortingIndicator;

        SongViewHolder(View itemView) {
            super(itemView);
            tvSong = itemView.findViewById(R.id.tvSong);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            ivSortingIndicator = itemView.findViewById(R.id.ivSortIndicator);
        }
    }

    public void setSorting(boolean sorting) {
        isSorting = sorting;
        notifyDataSetChanged();
    }
}
