package com.untref.robotica.robotcontroller.data.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class BluetoothConnector {

    private UUID uuid;
    private BluetoothSocketWrapper bluetoothSocket;
    private final BluetoothDevice device;
    private final BluetoothAdapter bluetoothAdapter;

    public BluetoothConnector(BluetoothDevice device, BluetoothAdapter bluetoothAdapter) {
        this.device = device;
        this.bluetoothAdapter = bluetoothAdapter;
        this.uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }

    public BluetoothSocketWrapper connect() throws IOException {
        BluetoothSocket tmp = createBluetoothSocketVersion();
        bluetoothSocket = new NativeBluetoothSocket(tmp);
        bluetoothAdapter.cancelDiscovery();

        try {
            Log.d("DEVICE", "Connect Native Bluetooth Socket");
            bluetoothSocket.connect();
        } catch (IOException e) {
            try {
                bluetoothSocket = new FallbackBluetoothSocket(bluetoothSocket.getUnderlyingSocket());
                Thread.sleep(500);
                Log.d("DEVICE", "Fallback Bluetooth Socket");
                bluetoothSocket.connect();
            } catch (FallbackException e1) {
                Log.w("DEVICE", "Could not initialize FallbackBluetoothSocket classes.", e);
            } catch (InterruptedException e1) {
                Log.w("DEVICE", e1.getMessage(), e1);
            } catch (IOException e1) {
                Log.w("DEVICE", "Fallback failed. Cancelling.", e1);
            }
        }

        return bluetoothSocket;
    }

    private BluetoothSocket createBluetoothSocketVersion() throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass()
                        .getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, uuid);
            } catch (Exception e) {
                Log.e("DEVICE", "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(uuid);
    }

    public static interface BluetoothSocketWrapper {

        InputStream getInputStream() throws IOException;

        OutputStream getOutputStream() throws IOException;

        String getRemoteDeviceName();

        void connect() throws IOException;

        String getRemoteDeviceAddress();

        void close() throws IOException;

        BluetoothSocket getUnderlyingSocket();

    }


    public static class NativeBluetoothSocket implements BluetoothSocketWrapper {

        private BluetoothSocket socket;

        public NativeBluetoothSocket(BluetoothSocket tmp) {
            this.socket = tmp;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return socket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return socket.getOutputStream();
        }

        @Override
        public String getRemoteDeviceName() {
            return socket.getRemoteDevice().getName();
        }

        @Override
        public void connect() throws IOException {
            socket.connect();
        }

        @Override
        public String getRemoteDeviceAddress() {
            return socket.getRemoteDevice().getAddress();
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }

        @Override
        public BluetoothSocket getUnderlyingSocket() {
            return socket;
        }

    }

    public class FallbackBluetoothSocket extends NativeBluetoothSocket {

        private BluetoothSocket fallbackSocket;

        public FallbackBluetoothSocket(BluetoothSocket tmp) throws FallbackException {
            super(tmp);
            try {
                fallbackSocket =(BluetoothSocket) device.getClass()
                        .getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                //Class<?> clazz = tmp.getRemoteDevice().getClass();
                //Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
                //Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                //fallbackSocket = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), 1);
            } catch (Exception e) {
                throw new FallbackException(e);
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return fallbackSocket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return fallbackSocket.getOutputStream();
        }

        @Override
        public void connect() throws IOException {
            fallbackSocket.connect();
        }

        @Override
        public void close() throws IOException {
            fallbackSocket.close();
        }
    }

    public static class FallbackException extends Exception {
        private static final long serialVersionUID = 1L;

        public FallbackException(Exception e) {
            super(e);
        }
    }
}