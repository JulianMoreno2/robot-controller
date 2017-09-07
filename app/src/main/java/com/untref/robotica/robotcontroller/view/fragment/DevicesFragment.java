package com.untref.robotica.robotcontroller.view.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.untref.robotica.robotcontroller.R;
import com.untref.robotica.robotcontroller.data.client.BluetoothClient;
import com.untref.robotica.robotcontroller.interactor.DevicesInteractor;
import com.untref.robotica.robotcontroller.interactor.DevicesRepository;
import com.untref.robotica.robotcontroller.presenter.DevicesPresenter;
import com.untref.robotica.robotcontroller.view.adapter.DevicesAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevicesFragment extends Fragment implements DevicesPresenter.View {

    @BindView(R.id.rv_device)
    RecyclerView rv_device;
    @BindView(R.id.pv_devices)
    ProgressBar pv_devices;
    @BindView(R.id.txt_subline_devices)
    TextView txt_sub_line_devices;

    private DevicesPresenter devicesPresenter;

    public DevicesFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        devicesPresenter = new DevicesPresenter(getContext(), new DevicesInteractor(
                new BluetoothClient(BluetoothAdapter.getDefaultAdapter()), new DevicesRepository()));
        devicesPresenter.setView(this);
        devicesPresenter.onStartDiscovery();
    }

    @Override
    public void onPause() {
        try {
            devicesPresenter.unregisterReceiver(getContext());
        } catch (IllegalArgumentException ex) {
            // If Receiver not registered
        }
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        rv_device.setAdapter(devicesPresenter.getDevicesAdapter());
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void showLoading() {
        pv_devices.setVisibility(View.VISIBLE);
        txt_sub_line_devices.setVisibility(View.GONE);
        rv_device.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        pv_devices.setVisibility(View.GONE);
        rv_device.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDevicesNotFoundMessage() {
        pv_devices.setVisibility(View.GONE);
        txt_sub_line_devices.setVisibility(View.VISIBLE);
        txt_sub_line_devices.setText("Cannot found devices");
    }

    @Override
    public void renderDevices(List<BluetoothDevice> devices) {
        DevicesAdapter devicesAdapter = (DevicesAdapter) rv_device.getAdapter();
        Log.d("DEVICE", "Amount of devices: " + devices.size());
        devicesAdapter.setDevices(devices);
        devicesAdapter.notifyDataSetChanged();
    }

    @Override
    public void pairDevice() {
        //TODO: cambiar el texto del boton a Pair
    }

    @Override
    public void unPairDevice() {
        //TODO: cambiar el texto del boton a unPair
    }

}
