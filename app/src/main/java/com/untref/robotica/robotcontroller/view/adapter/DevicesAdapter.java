package com.untref.robotica.robotcontroller.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.untref.robotica.robotcontroller.R;
import com.untref.robotica.robotcontroller.presenter.DevicesPresenter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder> {

    private List<BluetoothDevice> devices;
    private DevicesPresenter devicesPresenter;

    public DevicesAdapter(DevicesPresenter devicesPresenter) {
        this.devicesPresenter = devicesPresenter;
        devices = new ArrayList<>();
    }

    @Override
    public DevicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DevicesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DevicesViewHolder holder, int position) {
        BluetoothDevice device = devices.get(position);
        holder.device = device;
        holder.textViewDeviceName.setText(device.getName());
        holder.textViewDeviceAddress.setText(device.getAddress());

        holder.btnPair.setOnClickListener(v -> {
            Log.d("DEVICE", "Device state: " + device.getBondState());
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                //FIXME: El estado de device no cambia por lo que nunca se desvincula
                Log.d("DEVICE", "Unpairing device");
                holder.btnPair.setText("Pair");
                unpairDevice(device);
            } else {
                Log.d("DEVICE", "Pairing device");
                holder.btnPair.setText("Unpair");
                pairDevice(device);
                holder.btnConnect.setVisibility(View.VISIBLE);
                holder.btnConnect.setEnabled(true);
            }
        });

        holder.btnConnect.setOnClickListener(v -> {
                devicesPresenter.connectToPairDevice(holder.device);
                devicesPresenter.getView().renderNavigate();
        });
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void setDevices(List<BluetoothDevice> devices) {
        this.devices = devices;
    }

    public static class DevicesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_device_name)
        TextView textViewDeviceName;

        @BindView(R.id.txt_device_address)
        TextView textViewDeviceAddress;

        @BindView(R.id.btn_pair)
        Button btnPair;

        @BindView(R.id.btn_connect)
        Button btnConnect;

        BluetoothDevice device;

        public DevicesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
