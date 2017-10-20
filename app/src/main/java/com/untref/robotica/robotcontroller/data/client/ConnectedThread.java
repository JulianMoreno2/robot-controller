package com.untref.robotica.robotcontroller.data.client;

import com.untref.robotica.robotcontroller.data.client.socket.BluetoothSocketWrapper;

import java.io.IOException;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    private final OutputStream outputStream;

    public ConnectedThread(BluetoothSocketWrapper socket) {

        OutputStream tmpOut = null;

        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException ignored) {
        }

        outputStream = tmpOut;
    }

    public void write(String navigateMessage) {
        try {
            outputStream.write(navigateMessage.getBytes());
        } catch (IOException ignored) {
        }
    }
}