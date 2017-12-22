package vn.edu.hust.set.tung.musicplayer.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

public class LoadBitMapAsync extends AsyncTask<String, Void, String> {
    private ArrayList<Song> listSong;
    private final AtomicReference<ImageView> imageView = new AtomicReference<>();
    private Bitmap bitmap;
    private Album album;
    private Artist artist;

    public LoadBitMapAsync(ArrayList<Song> listSong, ImageView imageView) {
        this.listSong = listSong;
        this.imageView.set(imageView);
    }

    @Override
    protected String doInBackground(String... strings) {

        for (Song song : listSong) {
            Bitmap bitmap = SongHelper.songToBitmap(song);
            if (bitmap != null) {
                this.bitmap = bitmap;
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (bitmap != null) {
            if (imageView.get() != null)
                imageView.get().setImageBitmap(bitmap);
            if (album != null) {
                album.setBitmapCover(bitmap);
            } else if (artist != null) {
                artist.setBitmapCover(bitmap);
            }
        }
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}