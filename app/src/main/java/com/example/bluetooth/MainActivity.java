package com.example.bluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button status, turn_on, turn_off, paired, discover, discoverable;
    BluetoothAdapter bluetoothAdapter;
    ConnectivityManager connectivityManager;

    BroadcastReceiver broadcastReceiver1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.status);
        turn_on = findViewById(R.id.on);
        turn_off = findViewById(R.id.off);
        paired = findViewById(R.id.pair);
        discover = findViewById(R.id.discovery);
        discoverable = findViewById(R.id.discoverable);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Toast.makeText(this, ""+bluetoothAdapter, Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(this, new String[]
        {
       Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver1, filter1);


        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected())
                {
                    if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                        Toast.makeText(MainActivity.this, "WI FI", Toast.LENGTH_SHORT).show();

                    if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
                    Toast.makeText(MainActivity.this, "Mobile", Toast.LENGTH_SHORT).show();
                }
            }
        });

        turn_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bluetoothAdapter == null)
                {
                    Toast.makeText(MainActivity.this, "blutooth not supported", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    if(!bluetoothAdapter.isEnabled())
                    {
                        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(i,1);
                    }

                    if(bluetoothAdapter.isEnabled())
                    {
                        Toast.makeText(MainActivity.this, "enabled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bluetoothAdapter != null)
                {
                    bluetoothAdapter.disable();
                    Toast.makeText(MainActivity.this, "Turned off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        paired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bluetoothAdapter != null)
                {
                    Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

                    if(pairedDevice.size() >0)
                    {
                        for (BluetoothDevice device : pairedDevice)
                        {
                            String deviceNME = device.getName();
                            String  deviceAdd = device.getAddress();

                            Toast.makeText(MainActivity.this, "Paired "+deviceNME, Toast.LENGTH_SHORT).show();
                        }
                    }


                }

            }
        });

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bluetoothAdapter != null)
                {
                    bluetoothAdapter.startDiscovery();
                    Toast.makeText(MainActivity.this, "start Discovery "+bluetoothAdapter.startDiscovery(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        discoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bluetoothAdapter != null)
                {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                    startActivityForResult(intent,2);
                }

                else
                {
                    Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private final BroadcastReceiver mReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String acttion = intent.getAction();
            Toast.makeText(context, "Inside on recive of reciver1", Toast.LENGTH_SHORT).show();

            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(acttion)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state)
                {
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, "Blutooth off", Toast.LENGTH_SHORT).show();
                        break;

                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(context, "Bluetooth on", Toast.LENGTH_SHORT).show();
                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Toast.makeText(context, "Turning off", Toast.LENGTH_SHORT).show();
                        break;

                    case BluetoothAdapter.STATE_TURNING_ON:
                        Toast.makeText(context, "Turning on", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mReceiver1);

        bluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
                Toast.makeText(this, "turnned on", Toast.LENGTH_SHORT).show();

            if(resultCode == RESULT_CANCELED)
                Toast.makeText(this, "turn on failed", Toast.LENGTH_SHORT).show();
        }

        else if(requestCode == 2)
        {
            if(resultCode != RESULT_CANCELED)
            {
                Toast.makeText(this, "Device Discoveribility start", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Discoveribility cancled", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
