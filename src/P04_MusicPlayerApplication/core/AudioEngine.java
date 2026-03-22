package P04_MusicPlayerApplication.core;

import P04_MusicPlayerApplication.device.IAudioOutputDevice;
import P04_MusicPlayerApplication.models.Song;

public class AudioEngine {
    private Song currentSong;
    private boolean isSongPaused;

    public AudioEngine() {
        this.currentSong = null;
        this.isSongPaused = false;
    }

    public void play(IAudioOutputDevice audioDevice, Song song) {
        if (song == null) {
            throw new RuntimeException("Cannot run null song");
        }

        if (isSongPaused && song == currentSong) {
            isSongPaused = false;
            System.out.println("Resuming Song: " + song.getTitle());
            audioDevice.playAudio(song);
            return;
        }

        currentSong = song;
        isSongPaused = false;
        System.out.println("Playing new Song: " + song.getTitle());
        audioDevice.playAudio(song);
    }

    public void pause() {
        if (currentSong == null) {
            throw new RuntimeException("No song is currently playing to pause.");
        }

        if (isSongPaused) {
            throw new RuntimeException("Song already paused");
        }

        isSongPaused = true;
        System.out.println("Pausing song: " + currentSong.getTitle());
    }

    public String getCurrentSongTitle() {
        if (currentSong != null) {
            return currentSong.getTitle();
        }

        return "";
    }
}
