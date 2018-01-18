package com.aim.shoestore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by pedro on 08/12/17.
 */

public class AddProducto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproduct);
        Button agregar = (Button)findViewById(R.id.agregar);

    }

}