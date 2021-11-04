package com.example.bluetoothremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class util {
File save;
OutputStream output;
BluetoothSocket sock;

    public BluetoothAdapter checkbluetooth(Context c) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(c, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return null;
        } else
            return bluetoothAdapter;


    }


    public boolean wrte(Context d, String c)
    {
        try{
            Globalvar t = (Globalvar) d;
            sock = t.getSock();

            output = sock.getOutputStream();
            output.write('C');
            output.flush();
            Thread.sleep(1000);
            output.write(c.getBytes());
            output.flush();

            Toast.makeText(d,"Message sent!", Toast.LENGTH_LONG).show();
            return true;
        }
        catch(Exception e)
        {
            Toast.makeText(d,"Message did not send! Please reconnect", Toast.LENGTH_LONG).show();
            return false;
        }

    }


    public boolean wrtespec(Context d, String c)
    {
        try{
            Globalvar t = (Globalvar) d;
            sock = t.getSock();

            output = sock.getOutputStream();


            output.write(c.getBytes());
            output.flush();

            Toast.makeText(d,"Message sent!", Toast.LENGTH_LONG).show();
            return true;
        }
        catch(Exception e)
        {
            Toast.makeText(d,"Message did not send! Please reconnect", Toast.LENGTH_LONG).show();
            return false;
        }

    }



    public boolean connect(Context c, BluetoothDevice s)
    {

        final UUID MY_UUID_SPP=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try{
            Globalvar t = (Globalvar) c;
            sock = t.getSock();
            sock= s.createRfcommSocketToServiceRecord(MY_UUID_SPP);
            sock.connect();
            t.setSock(sock);
           return true;
        }
        catch(Exception e)
        {
            Log.d("msg", "connection failed");
            return false;

        }
    }

    public void quit(Context c)
    {
        Globalvar t = (Globalvar) c;
        BluetoothSocket s = t.getSock();
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void intialize(Context c)
    {
        save = new File(c.getFilesDir(), "save.txt");
    }
    public String read(Context c)
    {
        intialize(c);
        try {
            FileInputStream read = new FileInputStream(save);
            InputStreamReader reader = new InputStreamReader(read);
            BufferedReader readerest = new BufferedReader(reader);
            String line=readerest.readLine();
            readerest.close();
            reader.close();
            read.close();
            return line;

        }
        catch (IOException e) {
            return null;
        }
    }
        public boolean write(Context c, String write) {

        intialize(c);
            FileOutputStream writer;
            try {
                writer = c.openFileOutput("save.txt",Context.MODE_PRIVATE);
                writer.write(write.getBytes());

                writer.close();
                return true;
            }catch(Exception e) {

                return false;
            }

    }
    }





