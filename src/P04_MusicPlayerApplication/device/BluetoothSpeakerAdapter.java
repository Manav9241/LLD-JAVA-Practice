package P04_MusicPlayerApplication.device;

import P04_MusicPlayerApplication.external.BluetoothSpeakerAPI;
import P04_MusicPlayerApplication.models.Song;

public class BluetoothSpeakerAdapter implements IAudioOutputDevice{
    private final BluetoothSpeakerAPI bluetoothSpeaker;
    
    public BluetoothSpeakerAdapter(BluetoothSpeakerAPI api) {
        this.bluetoothSpeaker = api;
    }

    @Override
    public void playAudio(Song song) {
        bluetoothSpeaker.playSoundViaBluetooth(
            String.format(
                "%s by %s @ %s", 
                song.getTitle(), 
                song.getArtist(), 
                song.getFilePath()
            )
        );
    }
}
