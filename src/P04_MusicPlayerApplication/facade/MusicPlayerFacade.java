package P04_MusicPlayerApplication.facade;

import P04_MusicPlayerApplication.core.AudioEngine;
import P04_MusicPlayerApplication.device.IAudioOutputDevice;
import P04_MusicPlayerApplication.enums.DeviceType;
import P04_MusicPlayerApplication.managers.DeviceManager;
import P04_MusicPlayerApplication.models.Song;

public class MusicPlayerFacade {
    private static MusicPlayerFacade instance = null;
    private AudioEngine audioEngine;

    private MusicPlayerFacade() {
        this.audioEngine = new AudioEngine();
    }

    public MusicPlayerFacade getInstance() {
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

}
