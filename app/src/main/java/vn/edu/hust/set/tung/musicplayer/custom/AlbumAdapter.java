package vn.edu.hust.set.tung.musicplayer.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.model.obj.Album;

/**
 * Created by tungt on 11/24/17.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {

    private ArrayList<Album> listAlbum;

    public AlbumAdapter(ArrayList<Album> listAlbum) {
        this.listAlbum = listAlbum;
    }

    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new AlbumHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumHolder holder, int position) {
        Album album = listAlbum.get(position);
        holder.tvAlbumName.setText(album.getName());
        holder.tvAlbumArtist.setText(album.getListSong().get(0).getArtist());
    }

    @Override
    public int getItemCount() {
        return listAlbum == null ? 0 : listAlbum.size();
    }

    public class AlbumHolder extends RecyclerView.ViewHolder {

        TextView tvAlbumName;
        TextView tvAlbumArtist;
        ImageView ivAlbumCover;

        public AlbumHolder(View itemView) {
            super(itemView);
            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            tvAlbumArtist = itemView.findViewById(R.id.tvAlbumArtist);
            ivAlbumCover = itemView.findViewById(R.id.ivAlbumCover);
        }
    }

    public ArrayList<Album> getListAlbum() {
        return listAlbum;
    }

    public void setListAlbum(ArrayList<Album> listAlbum) {
        this.listAlbum = listAlbum;
        notifyDataSetChanged();
    }
}
