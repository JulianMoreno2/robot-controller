package com.untref.robotica.robotcontroller.interactor;

import android.content.Context;

import com.untref.robotica.robotcontroller.data.client.BluetoothClient;
import com.untref.robotica.robotcontroller.view.activity.HomeActivity;

public class HomeInteractor {

    private BluetoothClient bluetoothClient;

    public HomeInteractor(BluetoothClient bluetoothClient) {

        this.bluetoothClient = bluetoothClient;
    }

    public boolean enableBluetooth(Context context, HomeActivity homeActivity) {
        if (!bluetoothClient.isEnabled()) {
            bluetoothClient.setPermissions(context, homeActivity);
            return bluetoothClient.enable();
        }
        return !bluetoothClient.disable();
    }

}
