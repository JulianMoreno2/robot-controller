package com.untref.robotica.robotcontroller.data.client;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.untref.robotica.robotcontroller.view.activity.HomeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BluetoothClient {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothConnector.BluetoothSocketWrapper bluetoothSocket;

    public BluetoothClient(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public boolean isEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public boolean enable() {
        return bluetoothAdapter.enable();
    }

    public boolean disable() {
        return bluetoothAdapter.disable();
    }

    public void setPermissions(Context context, HomeActivity activity) {
        // Android 6.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String accessCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
            switch (ContextCompat.checkSelfPermission(context,
                    accessCoarseLocation)) {
                case PackageManager.PERMISSION_DENIED:
                    if (ContextCompat.checkSelfPermission(context,
                            accessCoarseLocation) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{accessCoarseLocation}, 1);
                    }
                    break;
                case PackageManager.PERMISSION_GRANTED:
                    break;
            }
        }
    }

    public void startDiscovery() {
        bluetoothAdapter.startDiscovery();
    }

    public List<BluetoothDevice> getBoundedDevices() {
        List<BluetoothDevice> devices = new ArrayList<>();
        devices.addAll(bluetoothAdapter.getBondedDevices());
        return devices;
    }

    public void connectToPairDevice(BluetoothDevice device) {
        try {
            bluetoothSocket = new BluetoothConnector(device, bluetoothAdapter).connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToBluetoothSocket(String navigateMessage) {
        ConnectedThread connectedThread = new ConnectedThread(bluetoothSocket);
        connectedThread.write(navigateMessage);
    }
}
