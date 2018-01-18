package com.aim.shoestore;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by pedro on 02/12/17.
 */

public class Cuenta extends AppCompatActivity {
    AutoCompleteTextView passAnt,passAct,passRep;
    TextToSpeech t1;
    String username;
    private static final int PASS_ANTIQ=1, PASS_ACT=2, PASS_REP=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta);
        Bundle bundle = getIntent().getExtras();

        // Obtienes el texto
        username = bundle.getString("user");
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Locale loc = new Locale("es", "","");
                if(t1.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
                    t1.setLanguage(loc);

                }
            }
        });
        passAnt = (AutoCompleteTextView) findViewById(R.id.ePassAnt);
        passAct = (AutoCompleteTextView) findViewById(R.id.ePassNew);
        passRep = (AutoCompleteTextView) findViewById(R.id.epassNewRe);
        passAnt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingrese contraseña antigua", TextToSpeech.QUEUE_FLUSH, null);
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e) {}
                Intent intentActionRecognizeSpeech = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                // Configura el Lenguaje (Español-México)
                intentActionRecognizeSpeech.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-PE");
                try {
                    startActivityForResult(intentActionRecognizeSpeech,PASS_ANTIQ);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Tú dispositivo no soporta el reconocimiento por voz",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        passAct.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingrese contraseña nueva", TextToSpeech.QUEUE_FLUSH, null);
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e) {}
                Intent intentActionRecognizeSpeech = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                // Configura el Lenguaje (Español-México)
                intentActionRecognizeSpeech.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-PE");
                try {
                    startActivityForResult(intentActionRecognizeSpeech,PASS_ACT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Tú dispositivo no soporta el reconocimiento por voz",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        passRep.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Repita contraseña nueva", TextToSpeech.QUEUE_FLUSH, null);
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e) {}
                Intent intentActionRecognizeSpeech = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                // Configura el Lenguaje (Español-México)
                intentActionRecognizeSpeech.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-PE");
                try {
                    startActivityForResult(intentActionRecognizeSpeech,PASS_REP);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Tú dispositivo no soporta el reconocimiento por voz",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        Button recuperar = (Button)findViewById(R.id.change);
        recuperar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Cambiar contraseña", TextToSpeech.QUEUE_FLUSH, null);

                return false;
            }
        });
        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SQL
                if(passAct.getText().toString().equals(passRep.getText().toString()) && (passAct.getText().toString().length()>5)){
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getApplicationContext(),"administracion", null, 1);
                    SQLiteDatabase bd = admin.getWritableDatabase();
                    int valor=0;
                    String sentence = "update enpleado set pass="+passRep.getText().toString()+"  where username="+username;
                    bd.execSQL(sentence);
                }
                else{
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "Las contraseñas no coinciden", Toast.LENGTH_SHORT);

                    toast1.show();
                    t1.speak("Las Contraseñas no coinciden", TextToSpeech.QUEUE_FLUSH, null);
                    finish();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PASS_ANTIQ:

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.
                                    EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    speech.clear();
                    strSpeech2Text = strSpeech2Text.replace("ingrese contraseña","");
                    strSpeech2Text = strSpeech2Text.replace("contraseña antigua","");
                    strSpeech2Text = strSpeech2Text.replace("antigua","");
                    strSpeech2Text = strSpeech2Text.replace(" ","");

                    passAnt.setText(strSpeech2Text);
                }

                break;
            case PASS_ACT:

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.
                                    EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    speech.clear();
                    strSpeech2Text = strSpeech2Text.replace("ingrese contraseña","");
                    strSpeech2Text = strSpeech2Text.replace("contraseña nueva","");
                    strSpeech2Text = strSpeech2Text.replace("nueva","");
                    strSpeech2Text = strSpeech2Text.replace(" ","");
                    passAct.setText(strSpeech2Text);
                }

                break;
            case PASS_REP:

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.
                                    EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    speech.clear();
                    strSpeech2Text = strSpeech2Text.replace("repita contraseña nueva","");
                    strSpeech2Text = strSpeech2Text.replace("contraseña nueva","");
                    strSpeech2Text = strSpeech2Text.replace("nueva","");
                    strSpeech2Text = strSpeech2Text.replace(" ","");
                    passRep.setText(strSpeech2Text);
                }

                break;
            default:

                break;
        }
    }

}