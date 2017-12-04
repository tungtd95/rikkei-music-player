package vn.edu.hust.set.tung.musicplayer.model.obj;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by tungt on 11/23/17.
 */

public class Artist {
    private String name;
    private ArrayList<Song> listSong;
    Bitmap bitmapCover;

    public Artist(String name) {
        this.name = name;
        listSong = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Song> getListSong() {
        return listSong;
    }

    public void setListSong(ArrayList<Song> listSong) {
        this.listSong = listSong;
    }

    public void setBitmapCover(Bitmap bitmapCover) {
        this.bitmapCover = bitmapCover;
    }

    public Bitmap getBitmapCover() {
        return bitmapCover;
    }

    @Override
    public String toString() {
        return name;
    }
}
