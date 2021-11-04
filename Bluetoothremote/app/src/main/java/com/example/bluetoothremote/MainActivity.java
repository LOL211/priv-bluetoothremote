package com.example.bluetoothremote;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    util utility = new util();
    boolean bluetooth;
    BluetoothAdapter ba;
    LinearLayout layout;
    TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ba = utility.checkbluetooth(getApplicationContext());
        status = findViewById(R.id.status_txt);
        layout = (LinearLayout) findViewById(R.id.bt_layout);

    }

    public void scan(View v)
    {
        bluetooth = ba.isEnabled();
        if(ba!=null && !bluetooth)
        {
            Intent enablebt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int worked = 1;
            startActivityForResult(enablebt, worked);
            if(worked == RESULT_OK)
                bluetooth = ba.isEnabled();
            else
                Toast.makeText(this, "Turn on bluetooth!", Toast.LENGTH_SHORT).show();
        }
        else{
            layout.removeAllViews();
            final Set<BluetoothDevice> pairedDevices = ba.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    final BluetoothDevice dev = device;
                    Button newboi = new Button(getApplicationContext());
                    newboi.setPadding(30, 30, 30, 30);
                    newboi.setText(device.getName());

                    newboi.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Button but = findViewById(v.getId());
                            status.setText("Connecting");
                               connect(dev,"");


                        }
                    });
                    layout.addView(newboi);
                }
            }


        }
    }

    public void connect(BluetoothDevice devices, String name)
    {

       boolean d =utility.connect(getApplicationContext(), devices);
       if(d)
       {
           layout.removeAllViews();
           Intent open = new Intent(this, Selection_screen.class);
            status.setText("Click on Scanned to get paired devices!");
           startActivity(open);
       }
       else
           status.setText("Connection Failed, Make sure device is in range and is not connected to another socket/device");
    }



}