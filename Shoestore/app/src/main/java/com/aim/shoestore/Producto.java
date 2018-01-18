package com.aim.shoestore;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by pedro on 02/12/17.
 */

public class Producto extends AppCompatActivity {
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producto);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Locale loc = new Locale("es", "","");
                if(t1.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
                    t1.setLanguage(loc);

                }
            }
        });
        Button badd = (Button)findViewById(R.id.btAdd);
        Button bsearch = (Button)findViewById(R.id.btSearch);
        Button blist = (Button)findViewById(R.id.btListar);
        badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddProducto.class);
                startActivity(i);

            }
        });
        badd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingresar a la vista de Añadir Producto", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SearchProducto.class);
                startActivity(i);

            }
        });
        bsearch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingresar a la vista de Buscar Producto", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        blist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListProducto.class);
                startActivity(i);

            }
        });
        blist.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingresar a la vista de Listar Producto", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
    }

}