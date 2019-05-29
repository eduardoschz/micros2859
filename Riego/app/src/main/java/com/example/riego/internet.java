package com.example.riego;

import android.content.Intent;
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


        Log.i("Valor: ", txtDatos.getText().toString());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Registros");
        /*
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("Valores","");
        hopperUpdates.put("Registros","0");
        myRef.updateChildren(hopperUpdates);
        */
        myRef.setValue(txtDatos.getText().toString());


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value = dataSnapshot.getValue(String.class);
                Log.d("Valor:", "" + value.toString());

                try {
                    flujoSalida.write(value.getBytes());

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

                }
            });

            btnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }


    }
}
