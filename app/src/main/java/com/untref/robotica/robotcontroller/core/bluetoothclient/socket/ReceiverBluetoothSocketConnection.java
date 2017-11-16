package com.untref.robotica.robotcontroller.core.bluetoothclient.socket;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import io.reactivex.subjects.PublishSubject;

/**
 * This thread runs while listening for incoming connections. It behaves
 * like a server-side client. It runs until a connection is accepted
 * (or until cancelled).
 */
public class ReceiverBluetoothSocketConnection extends Thread {

    // Debugging
    private static final String TAG = "BluetoothChatService";

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final String NAME_INSECURE = "BluetoothChatInsecure";

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    // Member fields
    private int mState;
    private int mNewState;

    // The local server socket
    private final BluetoothServerSocket mmServerSocket;
    private String mSocketType;
    private final BluetoothAdapter mAdapter;
    private final PublishSubject<BluetoothDevice> devicePublishSubject;

    public ReceiverBluetoothSocketConnection(BluetoothAdapter mAdapter, PublishSubject<BluetoothDevice> devicePublishSubject) {
        this.mAdapter = mAdapter;
        this.devicePublishSubject = devicePublishSubject;

        BluetoothServerSocket tmp = null;

        // Create a new listening server socket
        try {
            tmp = this.mAdapter.listenUsingInsecureRfcommWithServiceRecord(
                    NAME_INSECURE, MY_UUID_INSECURE);
        } catch (IOException e) {
            Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
        }
        mmServerSocket = tmp;
        mState = STATE_LISTEN;
    }

    public void run() {
        Log.d(TAG, "Socket Type: " + mSocketType +
                "BEGIN mAcceptThread" + this);
        setName("AcceptThread" + mSocketType);

        BluetoothSocket socket = null;

        // Listen to the server socket if we're not connected
        while (mState != STATE_CONNECTED) {
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
                break;
            }

            // If a connection was accepted
            if (socket != null) {
                synchronized (ReceiverBluetoothSocketConnection.this) {
                    switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            devicePublishSubject.onNext(socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                    }
                }
            }
        }
        Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);

    }

    public void cancel() {
        Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
        }
    }

}
