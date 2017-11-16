package com.untref.robotica.robotcontroller.presentation.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;

import com.untref.robotica.robotcontroller.core.interactor.HomeInteractor;
import com.untref.robotica.robotcontroller.presentation.view.activity.HomeActivity;

import java.util.function.Consumer;

import io.reactivex.subjects.PublishSubject;

public class HomePresenter extends Presenter<HomePresenter.View> {

    private HomeInteractor interactor;
    private PublishSubject<BluetoothDevice> bluetoothDevicePublishSubject;

    public HomePresenter(HomeInteractor interactor, PublishSubject<BluetoothDevice> bluetoothDevicePublishSubject) {
        this.interactor = interactor;
        this.bluetoothDevicePublishSubject = bluetoothDevicePublishSubject;
    }

    public boolean onEnableBluetooth(Context context, HomeActivity homeActivity) {
        boolean enableBluetooth = interactor.enableBluetooth(context, homeActivity);
        if(enableBluetooth) {
            getView().enableBluetooth();
        } else {
            getView().disableBluetooth();
        }

        return enableBluetooth;
    }

    public void onReceiveConnectionBluetoothSocket(Consumer<BluetoothDevice> bluetoothDeviceSupplier) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bluetoothDevicePublishSubject.subscribe(bluetoothDeviceSupplier::accept);
        }
    }

    public interface View extends Presenter.View {

        void enableBluetooth();

        void disableBluetooth();
    }
}