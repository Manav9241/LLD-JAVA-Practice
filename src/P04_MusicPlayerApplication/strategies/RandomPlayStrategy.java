package P04_MusicPlayerApplication.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import P04_MusicPlayerApplication.models.Playlist;
import P04_MusicPlayerApplication.models.Song;

public class RandomPlayStrategy implements IPlayStrategy {
    private Playlist currentPlaylist;
    private List<Song> remainingSongs;
    private Stack<Song> history;
    private Random random;

    public RandomPlayStrategy() {
        this.currentPlaylist = null;
        this.random = new Random();
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        currentPlaylist = playlist;

        if (currentPlaylist == null || currentPlaylist.getSize() == 0) {
            return;
        }

        remainingSongs = new ArrayList<>(currentPlaylist.getSongs());
        history = new Stack<>();
    }

    @Override
    public boolean hasNext() {
        return (currentPlaylist != null && !remainingSongs.isEmpty());
    }

    @Override
    public Song next() {
        if (currentPlaylist == null || currentPlaylist.getSize() == 0) {
            throw new RuntimeException("No playlist loaded or playlist is empty.");
        }
        if (remainingSongs.isEmpty()) {
            throw new RuntimeException("No songs left to play");
        }

        int idx = random.nextInt(remainingSongs.size());
        Song selectedSong = remainingSongs.get(idx);

        int lastIndex = remainingSongs.size() - 1;
        remainingSongs.set(idx, remainingSongs.get(lastIndex));
        remainingSongs.remove(lastIndex);

        history.push(selectedSong);
        return selectedSong;
    }

    @Override
    public boolean hasPrevious() {
        return (history.size() > 0);
    }

    @Override
    public Song previous() {
        if (history.isEmpty()) {
            throw new RuntimeException("No previous song available");
        }

        Song song = history.pop();
        return song;
    }
}
