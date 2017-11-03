package com.untref.robotica.robotcontroller.core.bluetoothclient.socket;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnector {

    private static BluetoothConnector instance;
    private final BluetoothAdapter bluetoothAdapter;
    private final BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    private boolean connected = false;

    public BluetoothConnector(BluetoothAdapter bluetoothAdapter, BluetoothDevice bluetoothDevice) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.device = bluetoothDevice;
    }

    /**
     * Connect to bluetooth device through a candidate uuid.
     *
     * @return
     */
    public void connect() {

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {

                Log.d("DEVICE", "Bluetooth habilitado. Conectando...");
                bluetoothAdapter.cancelDiscovery();

                try {
                    ParcelUuid[] deviceUuids = device.getUuids();
                    int counter = 0;
                    do {
                        UUID uuid = deviceUuids[counter].getUuid();
                        Log.d("DEVICE", "UUID connect?: " + uuid.toString());
                        socket = device.createRfcommSocketToServiceRecord(uuid);
                        try {
                            socket.connect();
                        } catch (IOException e) {
                            Log.d("DEVICE", "Retry bluetooth connection");
                        }
                        counter++;
                    } while (!socket.isConnected() && counter < deviceUuids.length);
                    connected = true;

                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();

                    Log.d("DEVICE", "Connection success");

                } catch (IOException e) {
                    connected = false;
                    Log.d("DEVICE", "Connection Timeout");
                    e.printStackTrace();
                }

            } else {
                Log.d("DEVICE", "Cannot connection to bluetooth device.");
            }
        }
    }

    public boolean isConnected() {
        return connected && bluetoothAdapter.enable();
    }

    public void disconnect() {

        if (isConnected()) {
            try {
                if (inputStream != null) {
                    this.inputStream.close();
                }

                if (outputStream != null) {
                    this.outputStream.close();
                }

                if (this.socket != null) {
                    this.socket.close();
                }

                this.bluetoothAdapter.disable();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String message) {
        try {
            outputStream.write(message.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive() throws IOException {

        byte[] message = new byte[5];

        for (int i = 0; i < 5; i++) {
            byte[] bytes = new byte[1];
            inputStream.read(bytes);
            message[i] = bytes[0];
        }

        return new String(message);
    }

    public static BluetoothConnector create(BluetoothAdapter bluetoothAdapter, BluetoothDevice device) {
        if (instance == null) {
            instance = new BluetoothConnector(bluetoothAdapter, device);;
        }
        return instance;
    }

    public static BluetoothConnector getInstance() {
        return instance;
    }
}