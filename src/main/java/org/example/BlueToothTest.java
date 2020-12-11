package org.example;

import javax.bluetooth.LocalDevice;
import java.io.IOException;

public class BlueToothTest {
    public static void main(String[]args) throws IOException {
        //34F15059234C - Tomka, 0015830CE42A - DESKTOP-PAAT8I3
        System.out.println("Local address: " + LocalDevice.getLocalDevice().getBluetoothAddress());
        LocalDevice device = LocalDevice.getLocalDevice();
        String friendlyName = device.getFriendlyName();
        System.out.println(friendlyName);
    }
}