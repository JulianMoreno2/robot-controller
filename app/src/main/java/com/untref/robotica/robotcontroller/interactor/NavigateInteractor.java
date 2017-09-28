package com.untref.robotica.robotcontroller.interactor;

import com.untref.robotica.robotcontroller.data.client.BluetoothClient;


public class NavigateInteractor {

    private BluetoothClient bluetoothClient;

    public NavigateInteractor(BluetoothClient bluetoothClient) {
        this.bluetoothClient = bluetoothClient;
    }

    public void navigate() {
        String navigateMessage = "*1*";
        bluetoothClient.sendToBluetoothSocket(navigateMessage);
    }
}
