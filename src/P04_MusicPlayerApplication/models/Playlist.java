package P04_MusicPlayerApplication.models;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private final String name;
    private List<Song> songList;

    public Playlist(String name) {
        this.name = name;
        this.songList = new ArrayList<>();
    }

    public String getPlaylistName() {
        return this.name;
    }

    public List<Song> getSongs() {
        return this.songList;
    }

    public int getSize() {
        return songList.size();
    }

    public void addSongToPlaylist(Song song) {
        if (song == null) {
            throw new RuntimeException("Cannot add null song to a playlist.");
        }

        if(songList.contains(song)) {
            throw new RuntimeException(this.name + " already has the song: " + song.getTitle() + ", so cannot add again");
        }

        songList.add(song);
    }
}
