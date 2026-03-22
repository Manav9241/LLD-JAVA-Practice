package P04_MusicPlayerApplication.strategies;

import P04_MusicPlayerApplication.models.Playlist;
import P04_MusicPlayerApplication.models.Song;

public interface IPlayStrategy {
    void setPlaylist(Playlist playlist);
    boolean hasNext();
    Song next();
    boolean hasPrevious();
    Song previous();
    default void addToNext(Song song) {}
}
