package com.example.bluetoothremote;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

public class Globalvar extends Application {
    BluetoothSocket sock;

    public BluetoothSocket getSock() {
        return sock;
    }

    public void setSock(BluetoothSocket sock) {
        this.sock = sock;
    }



}
