package vn.edu.hust.set.tung.musicplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

import static vn.edu.hust.set.tung.musicplayer.activity.MainActivity.TAG;

/**
 * Created by tungt on 11/23/17.
 */

public class SongHelper {

    Context context;
    ArrayList<Song> listSong = new ArrayList<>();
    ArrayList<Album> listAlbum = new ArrayList<>();
    ArrayList<Artist> listArtist = new ArrayList<>();

    public SongHelper(Context context) {
        this.context = context;
        listSong = getListSong();
        listAlbum = getListAlbum();
        listArtist = getListArtist();

        Log.i(TAG, "list song's size = " + listSong.size());
        Log.i(TAG, "list artist's size = " + listArtist.size());
        Log.i(TAG, "list album's size = " + listAlbum.size());
        Log.i(TAG, "------------------------");
        for (Artist artist : listArtist) {
            Log.i(TAG, artist.getName() + " " + artist.getListSong().size());
        }
        Log.i(TAG, "------------------------");
        for (Album album : listAlbum) {
            Log.i(TAG, album.getName() + " " + album.getListSong().size());
        }

    }

    public Cursor populateQueries() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Media._ID,     // context id/ uri id of the file
        };
        Cursor cursor = null;
        cursor = this.context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);
        // the last parameter sorts the data alphanumerically

        return cursor;
    }

    public ArrayList<Song> getListSong() {
        ArrayList<Song> list = new ArrayList<>();
        Cursor cursor = populateQueries();
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                String artist = cursor.getString(1);
                String album = cursor.getString(2);
                String duration = cursor.getString(3);
                String path = cursor.getString(4);
                String id = cursor.getString(5);
                Song s = new Song(id, name, path, artist, album, duration);
                list.add(s);
            }
        }
        return list;
    }

    public ArrayList<Album> getListAlbum() {
        ArrayList<Album> listAlbum = new ArrayList<>();try {
            for (Song song : listSong) {
                String album = song.getAlbum();
                String[] albumArr = album.split(",");
                for (String a : albumArr) {
                    album = a.trim();
                    Log.i(TAG, album);
                    if (listAlbum.size() == 0) {
                        Album ab = new Album(album);
                        ab.getListSong().add(song);
                        listAlbum.add(ab);
                        continue;
                    }
                    for (int i = 0; i < listAlbum.size(); i++) {
                        String albumOrigin = listAlbum.get(i).getName();
                        if (albumOrigin.equals(album)) {
                            listAlbum.get(i).getListSong().add(song);
                            break;
                        }
                        if (i == listAlbum.size() - 1) {
                            Album ab = new Album(album);
                            ab.getListSong().add(song);
                            listAlbum.add(ab);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAlbum;
    }

    public ArrayList<Artist> getListArtist() {
        ArrayList<Artist> listArtist = new ArrayList<>();
        try {
            for (Song song : listSong) {
                String artist = song.getArtist();
                String[] artistArr = artist.split(",");
                for (String a : artistArr) {
                    artist = a.trim();
                    Log.i(TAG, artist);
                    if (listArtist.size() == 0) {
                        Artist at = new Artist(artist);
                        at.getListSong().add(song);
                        listArtist.add(at);
                        continue;
                    }
                    for (int i = 0; i < listArtist.size(); i++) {
                        String artistOrigin = listArtist.get(i).getName();
                        if (artistOrigin.equals(artist)) {
                            listArtist.get(i).getListSong().add(song);
                            break;
                        }
                        if (i == listArtist.size() - 1) {
                            Artist at = new Artist(artist);
                            at.getListSong().add(song);
                            listArtist.add(at);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listArtist;
    }


}
