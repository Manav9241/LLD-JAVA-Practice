package P04_MusicPlayerApplication.facade;

import P04_MusicPlayerApplication.core.AudioEngine;
import P04_MusicPlayerApplication.device.IAudioOutputDevice;
import P04_MusicPlayerApplication.enums.DeviceType;
import P04_MusicPlayerApplication.enums.PlayStrategyType;
import P04_MusicPlayerApplication.managers.DeviceManager;
import P04_MusicPlayerApplication.managers.PlaylistManager;
import P04_MusicPlayerApplication.managers.StrategyManager;
import P04_MusicPlayerApplication.models.Playlist;
import P04_MusicPlayerApplication.models.Song;
import P04_MusicPlayerApplication.strategies.IPlayStrategy;

public class MusicPlayerFacade {
    private static MusicPlayerFacade instance = null;
    private AudioEngine audioEngine;
    private Playlist loadedPlaylist;
    private IPlayStrategy playStrategy;

    private MusicPlayerFacade() {
        this.audioEngine = new AudioEngine();
        this.loadedPlaylist = null;
        this.playStrategy = null;
    }

    public static synchronized MusicPlayerFacade getInstance() {
        if (instance == null) {
            instance = new MusicPlayerFacade();
        }
        return instance;
    }

    public void connectOutputDevice(DeviceType deviceType) {
        DeviceManager.getInstance().connect(deviceType);
    }

    public void playSong(Song song) {
        if (!DeviceManager.getInstance().hasOutputDevice()) {
            throw new RuntimeException("Cannot play song, no output device connected");
        }

        IAudioOutputDevice outputDevice = DeviceManager.getInstance().getOutputDevice();

        audioEngine.play(outputDevice, song);
    }

    public void pauseSong(Song song) {
        if (!audioEngine.getCurrentSongTitle().equalsIgnoreCase(song.getTitle())) {
            throw new RuntimeException("Cannot pause \"" + song.getTitle() + "\"; not currently playing.");
        }
        audioEngine.pause();
    }

    public void setPlayStrategy(PlayStrategyType strategyType) {
        this.playStrategy = StrategyManager.getInstance().getStrategy(strategyType);
    }

    public void loadPlaylist(String name) {
        if (playStrategy == null) {
            throw new RuntimeException("Play strategy not set before loading.");
        }
        this.loadedPlaylist = PlaylistManager.getInstance().getPlaylist(name);
        this.playStrategy.setPlaylist(loadedPlaylist);
    }

    public void playAllTracks() {
        if (loadedPlaylist == null) {
            throw new RuntimeException("No playlist loaded.");
        }

        while (playStrategy.hasNext()) {
            Song nextSong = playStrategy.next();
            IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
            audioEngine.play(device, nextSong);
        }

        System.out.println("Completed playlist: " + loadedPlaylist.getPlaylistName());
    }

    public void playNextTrack() {
        if (loadedPlaylist == null) {
            throw new RuntimeException("No playlist loaded.");
        }
        if (playStrategy.hasNext()) {
            Song nextSong = playStrategy.next();
            IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
            audioEngine.play(device, nextSong);
        } else {
            System.out.println("Completed playlist: " + loadedPlaylist.getPlaylistName());
        }
    }

    public void playPreviousTrack() {
        if (loadedPlaylist == null) {
            throw new RuntimeException("No playlist loaded.");
        }
        if (playStrategy.hasPrevious()) {
            Song prevSong = playStrategy.previous();
            IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
            audioEngine.play(device, prevSong);
        } else {
            System.out.println("Completed playlist: " + loadedPlaylist.getPlaylistName());
        }
    }

    public void enqueueNext(Song song) {
        playStrategy.addToNext(song);
    }
}
