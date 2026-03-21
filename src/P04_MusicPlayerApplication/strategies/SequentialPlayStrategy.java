package P04_MusicPlayerApplication.strategies;

import P04_MusicPlayerApplication.models.Playlist;
import P04_MusicPlayerApplication.models.Song;

public class SequentialPlayStrategy implements IPlayStrategy {
    private Playlist currentPlaylist;
    private int currentIndex;

    public SequentialPlayStrategy() {
        this.currentPlaylist = null;
        this.currentIndex = -1;
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        this.currentPlaylist = playlist;
        this.currentIndex = -1;
    }

    @Override
    public boolean hasNext() {
        return (currentPlaylist != null && (currentIndex + 1) < currentPlaylist.getSize());
    }

     @Override
    public Song next() {
        if (currentPlaylist == null || currentPlaylist.getSize() == 0) {
            throw new RuntimeException("No playlist loaded or playlist is empty.");
        }
        currentIndex = currentIndex + 1;
        return currentPlaylist.getSongs().get(currentIndex);
    }

    @Override
    public boolean hasPrevious() {
        return currentPlaylist != null && (currentIndex - 1 > 0);
    }

    @Override
    public Song previous() {
        if (currentPlaylist == null || currentPlaylist.getSize() == 0) {
            throw new RuntimeException("No playlist loaded or playlist is empty.");
        }
        currentIndex = currentIndex - 1;
        return currentPlaylist.getSongs().get(currentIndex);
    }
}
