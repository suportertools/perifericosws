package br.com.rtools.perifericosws.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class Mac {

    public static String getInstance() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface netInter = NetworkInterface.getByInetAddress(localHost);
            byte[] macAddressBytes = netInter.getHardwareAddress();

            String macAddress = String.format("%1$02x-%2$02x-%3$02x-%4$02x-%5$02x-%6$02x",
                    macAddressBytes[0], macAddressBytes[1],
                    macAddressBytes[2], macAddressBytes[3],
                    macAddressBytes[4], macAddressBytes[5]).toUpperCase();
            return macAddress;
        } catch (Exception e) {
            return "";
        }
    }

}
