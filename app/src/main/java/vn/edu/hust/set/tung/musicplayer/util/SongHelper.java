package vn.edu.hust.set.tung.musicplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

import static vn.edu.hust.set.tung.musicplayer.activity.MainActivity.TAG;

/**
 * Created by tungt on 11/23/17.
 */

public class SongHelper {

    Context context;

    public SongHelper(Context context) {
        this.context = context;
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

    public ArrayList<Album> getListAlbum(ArrayList<Song> listSong) {
        ArrayList<Album> listAlbum = new ArrayList<>();
        for (Song song : listSong) {
            String a = song.getAlbum();
            String album = a.trim();
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
        return listAlbum;
    }

    public ArrayList<Artist> getListArtist(ArrayList<Song> listSong) {
        ArrayList<Artist> listArtist = new ArrayList<>();
        for (Song song : listSong) {
            String artist = song.getArtist();
            String[] artistArr = artist.split(",");
            for (String a : artistArr) {
                artist = a.trim();
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
        return listArtist;
    }

    public static Bitmap songToBitmap(Song song) {
        Bitmap bitmap = null;
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(song.getUri());
        byte[] data = metadataRetriever.getEmbeddedPicture();
        if (data != null) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        return bitmap;
    }

    public static ArrayList<Song> searchSong(ArrayList<Song> listSong, String keyWord) {
        ArrayList<Song> list = new ArrayList<>();
        keyWord = convertString(keyWord);
        for (Song song : listSong) {
            if (convertString(song.getName()).contains(keyWord)) {
                list.add(song);
                if (list.size() == 10) {
                    return list;
                }
            }
        }
        return list;
    }

    public static ArrayList<Album> searchAlbum(ArrayList<Album> listAlbum, String keyWord) {
        ArrayList<Album> list = new ArrayList<>();
        keyWord = convertString(keyWord);
        for (Album album : listAlbum) {
            if (convertString(album.getName()).contains(keyWord)) {
                list.add(album);
                if (list.size() == 10) {
                    return list;
                }
            }
        }
        return list;
    }

    public static ArrayList<Artist> searchArtist(ArrayList<Artist> listArtist, String keyWord) {
        ArrayList<Artist> list = new ArrayList<>();
        keyWord = convertString(keyWord);
        for (Artist artist : listArtist) {
            if (convertString(artist.getName()).contains(keyWord)) {
                list.add(artist);
                if (list.size() == 10) {
                    return list;
                }
            }
        }
        return list;
    }

    public static ArrayList<Object> searchAll(
            ArrayList<Song> listSong,
            ArrayList<Artist> listArtist,
            ArrayList<Album> listAlbum,
            String keyWord
    ) {
        ArrayList<Song> songs = searchSong(listSong, keyWord);
        ArrayList<Artist> artists = searchArtist(listArtist, keyWord);
        ArrayList<Album> albums = searchAlbum(listAlbum, keyWord);
        ArrayList<Object> listObj = new ArrayList<>();
        for (Song song : songs) {
            listObj.add(song);
            if (listObj.size() == 10) {
                return listObj;
            }
        }
        for (Artist artist : artists) {
            listObj.add(artist);
            if (listObj.size() == 10) {
                return listObj;
            }
        }
        for (Album album : albums) {
            listObj.add(album);
            if (listObj.size() == 10) {
                return listObj;
            }
        }
        return listObj;
    }

    public static String convertString(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").toLowerCase().trim();
    }
}