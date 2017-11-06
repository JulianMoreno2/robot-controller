package com.untref.robotica.robotcontroller.core.provider;

import android.bluetooth.BluetoothAdapter;

import com.untref.robotica.robotcontroller.core.bluetoothclient.BluetoothClient;

import io.reactivex.subjects.PublishSubject;

public class Provider {

    static PublishSubject<String> publishSubject;
    static BluetoothClient bluetoothClient;

    public static PublishSubject<String> provideBluetoothReaderPublishSubject() {
        if(publishSubject != null) {
            return publishSubject;
        }
        publishSubject = PublishSubject.create();
        return publishSubject;
    }

    public static BluetoothClient provideBluetoothClient() {
        if(bluetoothClient != null) {
            return bluetoothClient;
        }
        bluetoothClient = new BluetoothClient(BluetoothAdapter.getDefaultAdapter(), provideBluetoothReaderPublishSubject());
        return bluetoothClient;
    }
}
