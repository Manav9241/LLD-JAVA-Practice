package P04_MusicPlayerApplication.device;

import P04_MusicPlayerApplication.models.Song;

public interface IAudioOutputDevice {
    void playAudio(Song song);
}
