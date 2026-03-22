package P04_MusicPlayerApplication.managers;

import java.util.HashMap;
import java.util.Map;

import P04_MusicPlayerApplication.models.Playlist;
import P04_MusicPlayerApplication.models.Song;

public class PlaylistManager {
    private static PlaylistManager instance;
    private Map<String, Playlist> playlists;

    private PlaylistManager() {
        this.playlists = new HashMap<>();
    }

    public static synchronized PlaylistManager getInstance() {
        if (instance == null) {
            instance = new PlaylistManager();
        }
        return instance;
    }

    public void createNewPlaylist(String playlistName) {
        if (playlists.containsKey(playlistName)) {
            throw new RuntimeException("Playlist with the name " + playlistName + "already exists.");
        }
        playlists.put(playlistName, new Playlist(playlistName));
    }

    public void addSongToPlaylist(String playlistName, Song song) {
        if (!playlists.containsKey(playlistName)) {
            throw new RuntimeException("No playlist with the name: " + playlistName + " found in the list.");
        }

        playlists.get(playlistName).addSongToPlaylist(song);
    }

    public Playlist getPlaylist(String name) {
        if (!playlists.containsKey(name)) {
            throw new RuntimeException("Playlist \"" + name + "\" not found.");
        }
        return playlists.get(name);
    }
}
