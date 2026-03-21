package P04_MusicPlayerApplication.factories;

import P04_MusicPlayerApplication.device.BluetoothSpeakerAdapter;
import P04_MusicPlayerApplication.device.HeadphoneAdapter;
import P04_MusicPlayerApplication.device.IAudioOutputDevice;
import P04_MusicPlayerApplication.device.WiredSpeakerAdapter;
import P04_MusicPlayerApplication.enums.DeviceType;
import P04_MusicPlayerApplication.external.BluetoothSpeakerAPI;
import P04_MusicPlayerApplication.external.HeadphoneAPI;
import P04_MusicPlayerApplication.external.WiredSpeakerAPI;

public class AudioOutputDeviceFactory {
    public static IAudioOutputDevice createDeviceAdapterObject(DeviceType deviceType) {
        switch (deviceType) {
            case BLUETOOTH:
                return new BluetoothSpeakerAdapter(new BluetoothSpeakerAPI());
            case WIRED:
                return new WiredSpeakerAdapter(new WiredSpeakerAPI());
            case HEADPHONE:
            default:
                return new HeadphoneAdapter(new HeadphoneAPI());
        }
    }
}
