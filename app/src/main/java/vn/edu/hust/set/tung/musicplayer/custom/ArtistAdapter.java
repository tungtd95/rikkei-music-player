package vn.edu.hust.set.tung.musicplayer.custom;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.util.LoadBitMapAsync;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistHolder> {

    private ArrayList<Artist> listArtist;
    private boolean isGrid = true;

    public ArtistAdapter(ArrayList<Artist> listArtist) {
        this.listArtist = listArtist;
    }

    @Override
    public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ArtistHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (isGrid) {
            return R.layout.item_artist;
        } else {
            return R.layout.item_artist_list;
        }
    }

    public void setGrid(boolean grid) {
        isGrid = grid;
    }

    public void setListArtist(ArrayList<Artist> listArtist) {
        this.listArtist = listArtist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ArtistHolder holder, int position) {
        holder.ivArtistCover.setImageDrawable(holder.ivArtistCover.getContext().getResources().getDrawable(R.drawable.ic_artist_accent));
        Artist artist = listArtist.get(position);
        int size = artist.getListSong().size();
        String song = artist.getListSong().size() == 1 ? "song" : "songs";
        holder.tvArtistName.setText(artist.getName());
        holder.tvArtistDetail.setText(String.format(Locale.getDefault(), "%d %s", size, song));
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

    class ArtistHolder extends RecyclerView.ViewHolder {

        TextView tvArtistName;
        TextView tvArtistDetail;
        ImageView ivArtistCover;

        ArtistHolder(View itemView) {
            super(itemView);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            tvArtistDetail = itemView.findViewById(R.id.tvArtistDetail);
            ivArtistCover = itemView.findViewById(R.id.ivArtistCover);
        }
    }
}
