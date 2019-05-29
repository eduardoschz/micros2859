package com.example.riego;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    Button btnInternet, btnBlue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInternet = (Button) findViewById(R.id.btnInternet);
       // btnBlue = (Button) findViewById(R.id.btnBluetooth);

        btnInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), internet.class);
                startActivity(intent);
            }
        });

        /*
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Blue.class);
                startActivity(intent);

            }
        });

        */

    }
}
