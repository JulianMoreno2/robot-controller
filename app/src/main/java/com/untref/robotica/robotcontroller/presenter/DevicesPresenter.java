package com.untref.robotica.robotcontroller.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.untref.robotica.robotcontroller.interactor.DevicesInteractor;
import com.untref.robotica.robotcontroller.view.adapter.DevicesAdapter;

import java.util.List;

public class DevicesPresenter extends Presenter<DevicesPresenter.View> {

    private static final String TAG = "DEVICES";
    private DevicesInteractor interactor;
    private final DevicesAdapter devicesAdapter;

    public DevicesPresenter(Context context, DevicesInteractor interactor) {
        this.interactor = interactor;

        devicesAdapter = new DevicesAdapter();

        registerBluetoothDiscoverReceiver(context);
        registerBluetoothPairReceiver(context);
    }

    private void registerBluetoothPairReceiver(Context context) {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        context.registerReceiver(pairReceiver, filter);
    }

    private void registerBluetoothDiscoverReceiver(Context context) {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(bluetoothReceiver, filter);
    }

    public void onStartDiscovery() {
        interactor.startDiscovery();
    }

    private final BroadcastReceiver pairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Log.d("DEVICE", "Device paired");
                    getView().pairDevice();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    Log.d("DEVICE", "Device paired");
                    getView().unPairDevice();
                }
            }
            devicesAdapter.notifyDataSetChanged();
        }
    };

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            getView().showLoading();
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.d(TAG, "Action: Discovery started");

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG, "Action: Discovery finished");
                List<BluetoothDevice> devices = interactor.getDevices();
                if (!devices.isEmpty()) {
                    getView().hideLoading();
                    devices.addAll(interactor.getBoundedDevices());
                    getView().renderDevices(devices);
                } else {
                    getView().showDevicesNotFoundMessage();
                }

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "Action: Device found - " + device.getName());
                interactor.addDevice(device);
            }
        }
    };

    public RecyclerView.Adapter getDevicesAdapter() {
        return devicesAdapter;
    }

    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(bluetoothReceiver);
        context.unregisterReceiver(pairReceiver);
    }

    public interface View extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showDevicesNotFoundMessage();

        void renderDevices(List<BluetoothDevice> devices);

        void pairDevice();

        void unPairDevice();
    }
}
