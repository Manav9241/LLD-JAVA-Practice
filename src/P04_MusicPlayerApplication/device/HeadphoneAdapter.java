package P04_MusicPlayerApplication.device;

import P04_MusicPlayerApplication.external.HeadphoneAPI;
import P04_MusicPlayerApplication.models.Song;

public class HeadphoneAdapter implements IAudioOutputDevice{
    private final HeadphoneAPI headphone;

    public HeadphoneAdapter(HeadphoneAPI api) {
        this.headphone = api;
    }
    
    @Override
    public void playAudio(Song song) {
        headphone.playSoundViaJack(
            String.format(
                "%s by %s @ %s", 
                song.getTitle(), 
                song.getArtist(), 
                song.getFilePath()
            )
        );
    }
}
