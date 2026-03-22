package P04_MusicPlayerApplication.device;

import P04_MusicPlayerApplication.external.WiredSpeakerAPI;
import P04_MusicPlayerApplication.models.Song;

public class WiredSpeakerAdapter implements IAudioOutputDevice {
    private final WiredSpeakerAPI wiredSpeaker;

    public WiredSpeakerAdapter(WiredSpeakerAPI api) {
        this.wiredSpeaker = api;
    }

    @Override
    public void playAudio(Song song) {
        wiredSpeaker.playSoundViaCable(
                String.format(
                        "%s by %s @ %s",
                        song.getTitle(),
                        song.getArtist(),
                        song.getFilePath()));
    }

}
