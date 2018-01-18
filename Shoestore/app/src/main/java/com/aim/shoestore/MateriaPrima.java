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

public class MateriaPrima extends AppCompatActivity {
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materiaprima);
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
        Button blist = (Button)findViewById(R.id.btList);
        badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddMatPrim.class);
                startActivity(i);

            }
        });
        badd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingresar a la vista de Añadir Materia Prima", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SearchMatPrim.class);
                startActivity(i);

            }
        });
        bsearch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingresar a la vista de Buscar Materia Prima", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        blist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListMatPrim.class);
                startActivity(i);

            }
        });
        blist.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingresar a la vista de Listar Materia Prima", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });

    }

}