package com.untref.robotica.robotcontroller.core.bluetoothclient;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.untref.robotica.robotcontroller.core.bluetoothclient.socket.BluetoothConnector;
import com.untref.robotica.robotcontroller.presentation.view.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class BluetoothClient {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothConnector bluetoothConnector;

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
        bluetoothConnector = BluetoothConnector.create(bluetoothAdapter, device);
        bluetoothConnector.connect();
    }

    public void sendToBluetoothSocket(String navigateMessage) {
        BluetoothConnector instance = BluetoothConnector.getInstance();
        if (instance.isConnected()) {
            Log.d("DEVICE", "Send -> " + navigateMessage);
            instance.send(navigateMessage);
        } else {
            Log.d("DEVICE", "Bluetooth is disconnected");
        }
    }

    public void disconnectBluetooth(String navigateMessage){
        BluetoothConnector instance = BluetoothConnector.getInstance();
        if (instance.isConnected()) {
            Log.d("DEVICE", "Send -> " + navigateMessage);
            instance.send(navigateMessage);
            instance.disconnect();
        }
    }
}
