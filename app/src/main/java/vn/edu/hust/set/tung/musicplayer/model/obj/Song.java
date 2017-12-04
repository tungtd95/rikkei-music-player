package vn.edu.hust.set.tung.musicplayer.model.obj;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tungt on 11/23/17.
 */

public class Song implements Parcelable{
    private String id;
    private String name;
    private String uri;
    private String artist;
    private String album;
    private String duration;

    public Song(String id, String name, String uri, String artist, String album, String duration) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    protected Song(Parcel in) {
        id = in.readString();
        name = in.readString();
        uri = in.readString();
        artist = in.readString();
        album = in.readString();
        duration = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(uri);
        parcel.writeString(artist);
        parcel.writeString(album);
        parcel.writeString(duration);
    }

    @Override
    public String toString() {
        return name;
    }
}
