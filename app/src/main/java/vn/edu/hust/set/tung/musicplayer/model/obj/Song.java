package vn.edu.hust.set.tung.musicplayer.model.obj;

/**
 * Created by tungt on 11/23/17.
 */

public class Song {
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
}
