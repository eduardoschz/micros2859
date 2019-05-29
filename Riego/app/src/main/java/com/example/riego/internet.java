package com.example.riego;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;

public class internet extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference dataRecipe;
    String MESSAGE_CHILD = "message";
    BluetoothSocket btInternoSocket = null;
    OutputStream flujoSalida = null;
    ImageView imgBack;
    TextView txtDatos;
    Button btnOn, btnOff;
    //EditText dataRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        final EditText txtDatos = (EditText) findViewById(R.id.txtDatos);
        btnOn = (Button) findViewById(R.id.btnInternetOn);
        btnOff = (Button) findViewById(R.id.btnInternetOff);


        Log.i("Valor: ", txtDatos.getText().toString());

        database = FirebaseDatabase.getInstance();
        final DatabaseReference messageReference = database.getReference().child(MESSAGE_CHILD);
        // myRef = database.getReference("regar");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("regar");
        // myRef.child("id0");
        //  DatabaseReference recipe = database.getReference("regHum");


        /*
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("Valores","");
        hopperUpdates.put("Registros","0");
        myRef.updateChildren(hopperUpdates);
        */
        //  myRef.setValue(1);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Integer value = dataSnapshot.getValue(Integer.class);
                txtDatos.setText(value.toString());
                Log.d("Valor:", "" + value.toString());
                if (value == 1) {
                    String encendido = "Encendido...";
                    txtDatos.setText(encendido);

            } else if (value ==  0){
                    String apagado = "Apagado...";
                    txtDatos.setText(apagado);

                }

                try {
                    flujoSalida.write(value.intValue());

                } catch (Exception ex) {

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
                //Log.e("Error", error.toException());
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        try {
            btnOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference messageReference = database.getReference().child(MESSAGE_CHILD);
                    DatabaseReference myRef = database.getReference("regar");
                    // dataRecipe = database.getReference("regHum");
                    myRef.setValue(1);
                }
            });

            btnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference myRef = database.getReference("regar");
                    myRef.setValue(0);

                }
            });

        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }


    }

}
