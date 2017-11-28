package vn.edu.hust.set.tung.musicplayer.model.obj;

import java.util.ArrayList;

/**
 * Created by tungt on 11/23/17.
 */

public class Album {
    private String name;
    private ArrayList<Song> listSong;

    public Album(String name) {
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
}