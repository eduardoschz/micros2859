package com.example.riego;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.OutputStream;
import java.util.UUID;

public class Blue extends AppCompatActivity {
    ImageView imgBack;
    Button btnOn, btnOff;
    BluetoothDevice btExterno = null;
    BluetoothAdapter btInterno = null;
    BluetoothSocket btInternoSocket = null;
    OutputStream flujoSalida = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnOn = (Button) findViewById(R.id.btnInternetOn);
        btnOff = (Button) findViewById(R.id.btnOff);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        try {
            btInterno = BluetoothAdapter.getDefaultAdapter();
            btExterno = btInterno.getRemoteDevice("");
            btInternoSocket = btExterno.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            btInternoSocket.connect();
            flujoSalida = btInternoSocket.getOutputStream();


            btnOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        flujoSalida.write("1".getBytes());
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                    }
                }
            });

            btnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        flujoSalida.write("Error".getBytes());
                    }catch (Exception   e){
                        Log.e("Error",e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }
}
