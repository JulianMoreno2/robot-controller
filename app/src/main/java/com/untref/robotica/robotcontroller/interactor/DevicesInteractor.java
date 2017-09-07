package com.untref.robotica.robotcontroller.interactor;

import android.bluetooth.BluetoothDevice;

import com.untref.robotica.robotcontroller.data.client.BluetoothClient;

import java.util.List;

public class DevicesInteractor {

    private BluetoothClient bluetoothClient;
    private DevicesRepository devicesRepository;

    public DevicesInteractor(BluetoothClient bluetoothClient, DevicesRepository devicesRepository) {
        this.bluetoothClient = bluetoothClient;
        this.devicesRepository = devicesRepository;
    }

    public void startDiscovery() {
        bluetoothClient.startDiscovery();
    }

    public void addDevice(BluetoothDevice device) {
        devicesRepository.addDevice(device);
    }

    public List<BluetoothDevice> getDevices() {
        return devicesRepository.getDevices();
    }

    public List<BluetoothDevice> getBoundedDevices() {
        return bluetoothClient.getBoundedDevices();
    }
}
