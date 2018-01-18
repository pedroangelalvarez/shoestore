package com.aim.shoestore;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by pedro on 02/12/17.
 */

public class Recovery extends AppCompatActivity {
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery);
        Button recuperar = (Button)findViewById(R.id.send);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Locale loc = new Locale("es", "","");
                if(t1.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
                    t1.setLanguage(loc);

                }
            }
        });
        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "Codigo de recuperacion enviado a su correo", Toast.LENGTH_SHORT);

                toast1.show();
                finish();
            }
        });
        recuperar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Enviar código de recuperación a su correo", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
    }

}