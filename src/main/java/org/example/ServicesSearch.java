package org.example;

import javax.bluetooth.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Minimal Services Search example.
 */
public class ServicesSearch {

    @SuppressWarnings("unused")
    static final UUID OBEX_FILE_TRANSFER = new UUID(0x1106);

    public static final CopyOnWriteArrayList<String> serviceFound = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {

        // First run RemoteDeviceDiscovery and use discoved device
        RemoteDeviceDiscovery.main(null);

        serviceFound.clear();

        UUID serviceUUID = new UUID(0x20201211L);
        if ((args != null) && (args.length > 0)) {
            serviceUUID = new UUID(args[0], false);
        }

        //34F15059234C - Tomka, 0015830CE42A - DESKTOP-PAAT8I3


        final Object serviceSearchCompletedEvent = new Object();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            }

            public void inquiryCompleted(int discType) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                for (ServiceRecord serviceRecord : servRecord) {
                    String url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    if (url == null) {
                        continue;
                    }
                    serviceFound.add(url);
                    DataElement serviceName = serviceRecord.getAttributeValue(0x0100);
                    if (serviceName != null) {
                        System.out.println("service " + serviceName.getValue() + " found " + url);
                    } else {
                        System.out.println("service found " + url);
                    }
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
                System.out.println("service search completed!");
                synchronized (serviceSearchCompletedEvent) {
                    serviceSearchCompletedEvent.notifyAll();
                }
            }

        };

        UUID[] searchUuidSet = new UUID[]{serviceUUID};
        int[] attrIDs = new int[]{
                0x0100 // Service name
        };

        for (RemoteDevice btDevice : RemoteDeviceDiscovery.devicesDiscovered) {
            synchronized (serviceSearchCompletedEvent) {
                System.out.println("search services on " + btDevice.getBluetoothAddress() + " " + btDevice.getFriendlyName(false));
                LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet, btDevice, listener);
                serviceSearchCompletedEvent.wait();
            }
        }

    }

}