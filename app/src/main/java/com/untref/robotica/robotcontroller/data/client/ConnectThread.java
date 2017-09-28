package com.untref.robotica.robotcontroller.data.client;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {

    private BluetoothSocket bluetoothSocket;

    public ConnectThread(BluetoothDevice device) {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
        }
    }

    public BluetoothSocket connect() {
        try {
            bluetoothSocket.connect();
            return bluetoothSocket;
        } catch (IOException connectException) {
            try {
                bluetoothSocket.close();
            } catch (IOException ignored) {

            }
        }
        return null;
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
        }
    }
}