package com.untref.robotica.robotcontroller.data.client.socket;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class FallbackBluetoothSocket implements BluetoothSocketWrapper {

    private final BluetoothDevice device;
    private final UUID uuid;
    private BluetoothSocket fallbackSocket;

    public FallbackBluetoothSocket(BluetoothDevice device, UUID uuid) throws BluetoothConnector.FallbackException {
        this.device = device;
        this.uuid = uuid;
        try {
            fallbackSocket = createBluetoothSocketVersion();
        } catch (Exception e) {
            throw new BluetoothConnector.FallbackException(e);
        }
    }

    private BluetoothSocket createBluetoothSocketVersion() throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass()
                        .getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, uuid);
            } catch (Exception e) {
                Log.e("DEVICE", "Could not create Insecure RFComm Connection", e);
            }
        }
        return device.createRfcommSocketToServiceRecord(uuid);
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return fallbackSocket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return fallbackSocket.getOutputStream();
    }

    @Override
    public String getRemoteDeviceName() {
        return fallbackSocket.getRemoteDevice().getName();
    }

    @Override
    public void connect() throws IOException {
        fallbackSocket.connect();
    }

    @Override
    public String getRemoteDeviceAddress() {
        return fallbackSocket.getRemoteDevice().getAddress();
    }

    @Override
    public void close() throws IOException {
        fallbackSocket.close();
    }

    @Override
    public BluetoothSocket getUnderlyingSocket() {
        return fallbackSocket;
    }
}