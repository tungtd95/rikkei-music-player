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
import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.util.LoadBitMapAsync;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {

    private ArrayList<Album> listAlbum;
    private boolean isGrid = true;

    public AlbumAdapter(ArrayList<Album> listAlbum) {
        this.listAlbum = listAlbum;
    }

    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new AlbumHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (isGrid) {
            return R.layout.item_album;
        } else {
            return R.layout.item_album_list;
        }
    }

    public void setGrid(boolean grid) {
        isGrid = grid;
    }

    @Override
    public void onBindViewHolder(AlbumHolder holder, int position) {
        holder.ivAlbumCover.setImageDrawable(holder.ivAlbumCover.getContext().getResources().getDrawable(R.drawable.ic_album_accent));
        Album album = listAlbum.get(position);
        holder.tvAlbumName.setText(album.getName());
        holder.tvAlbumArtist.setText(album.getListSong().get(0).getArtist());
        if (album.getBitmapCover() != null) {
            holder.ivAlbumCover.setImageBitmap(album.getBitmapCover());
            return;
        }
        LoadBitMapAsync loadBitMapAsync = new LoadBitMapAsync(album.getListSong(), holder.ivAlbumCover);
        loadBitMapAsync.setAlbum(album);
        loadBitMapAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public int getItemCount() {
        return listAlbum == null ? 0 : listAlbum.size();
    }

    class AlbumHolder extends RecyclerView.ViewHolder {

        TextView tvAlbumName;
        TextView tvAlbumArtist;
        ImageView ivAlbumCover;

        AlbumHolder(View itemView) {
            super(itemView);
            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            tvAlbumArtist = itemView.findViewById(R.id.tvAlbumArtist);
            ivAlbumCover = itemView.findViewById(R.id.ivAlbumCover);
        }
    }

    public void setListAlbum(ArrayList<Album> listAlbum) {
        this.listAlbum = listAlbum;
        notifyDataSetChanged();
    }
}
