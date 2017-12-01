package vn.edu.hust.set.tung.musicplayer.custom;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.util.LoadBitMapAsync;

/**
 * Created by tungt on 11/24/17.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistHolder> {

    private ArrayList<Artist> listArtist;

    public ArtistAdapter(ArrayList<Artist> listArtist) {
        this.listArtist = listArtist;
    }

    @Override
    public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ArtistHolder(view);
    }

    public ArrayList<Artist> getListArtist() {
        return listArtist;
    }

    public void setListArtist(ArrayList<Artist> listArtist) {
        this.listArtist = listArtist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ArtistHolder holder, int position) {
        Artist artist = listArtist.get(position);
        int size = artist.getListSong().size();
        String song = artist.getListSong().size() == 1 ? "song" : "songs";
        holder.tvArtistName.setText(artist.getName());
        holder.tvArtistDetail.setText(size + " " + song);
        if (artist.getBitmapCover() != null) {
            holder.ivArtistCover.setImageBitmap(artist.getBitmapCover());
            return;
        }
        LoadBitMapAsync loadBitMapAsync = new LoadBitMapAsync(artist.getListSong(), holder.ivArtistCover);
        loadBitMapAsync.setArtist(artist);
        loadBitMapAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public int getItemCount() {
        return listArtist == null ? 0 : listArtist.size();
    }

    public class ArtistHolder extends RecyclerView.ViewHolder {

        TextView tvArtistName;
        TextView tvArtistDetail;
        ImageView ivArtistCover;

        public ArtistHolder(View itemView) {
            super(itemView);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            tvArtistDetail = itemView.findViewById(R.id.tvArtistDetail);
            ivArtistCover = itemView.findViewById(R.id.ivArtistCover);
        }
    }
}
