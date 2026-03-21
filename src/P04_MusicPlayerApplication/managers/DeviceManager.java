package P04_MusicPlayerApplication.managers;

import P04_MusicPlayerApplication.device.IAudioOutputDevice;
import P04_MusicPlayerApplication.enums.DeviceType;
import P04_MusicPlayerApplication.factories.AudioOutputDeviceFactory;

public class DeviceManager {
    private static DeviceManager instance = null;
    private IAudioOutputDevice currentOutputDevice;

    private DeviceManager() {
        this.currentOutputDevice = null;
    }

    public static DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    public void connect(DeviceType deviceType) {
        currentOutputDevice = AudioOutputDeviceFactory.createDeviceAdapterObject(deviceType);
        switch (deviceType) {
            case BLUETOOTH:
                System.out.println("Bluetooth Device Connected.");
                break;
            case WIRED:
                System.out.println("Bluetooth Device Connected.");
                break;
            case HEADPHONE:
                System.out.println("Bluetooth Device Connected.");
                break;
        }
    }

    public IAudioOutputDevice getOutputDevice() {
        if (currentOutputDevice == null) {
            throw new RuntimeException("No output device is connected.");
        }

        return currentOutputDevice;
    }

    public boolean hasOutputDevice() {
        return (currentOutputDevice != null);
    }
}
