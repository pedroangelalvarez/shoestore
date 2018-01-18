package com.aim.shoestore;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by pedro on 08/12/17.
 */

public class SearchMatPrim extends AppCompatActivity {
    int request_code = 1;
    int codMat=0;
    TextToSpeech t1;
    Tabla tabla;
    AutoCompleteTextView codigo;
    AutoCompleteTextView cantidad;
    Button b,botmas,botmenos,guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchmatprim);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Locale loc = new Locale("es", "","");
                if(t1.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
                    t1.setLanguage(loc);

                }
            }
        });
        codigo = (AutoCompleteTextView)findViewById(R.id.codemat);
        tabla = new Tabla(this, (TableLayout)findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tablaMatPrima);
        b = (Button)findViewById(R.id.buttonM);
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Escanear codigo qr", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ScannerMat.class);
                startActivityForResult(i, request_code);
            }
        });

        cantidad = (AutoCompleteTextView) findViewById(R.id.cantModPod);

        botmas = (Button)findViewById(R.id.buttonMasM);
        botmas.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Aumentar una unidad la cantidad", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        botmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cantidad.getText().toString()!=""){
                    cantidad.setText((Integer.parseInt(cantidad.getText().toString())+1)+"");}

            }
        });

        botmenos = (Button)findViewById(R.id.buttonMenosM);
        botmenos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Disminuir una unidad la cantidad", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        botmenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cantidad.getText().toString()!=""){
                    cantidad.setText((Integer.parseInt(cantidad.getText().toString())-1)+"");}

            }
        });

        guardar= (Button)findViewById(R.id.buttonGuardarM);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getApplicationContext(),"administracion", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                int valor=0;
                String sentence = "update materiaprima set cantidad="+cantidad.getText().toString()+"  where codigo="+codMat;
                bd.execSQL(sentence);
            }
        });
        guardar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Guardar cantidad modificada", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        codigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    ((EditText) v).addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //

                        }

                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            //

                        }

                        public void afterTextChanged(Editable s) {
                            // affect EditText2
                            tabla.eliminarFila(1);
                            cantidad.setText("");
                            codMat=0;
                            ArrayList<String> elementos = new ArrayList<String>();
                            if (codigo.getText().toString().length() == 3) {
                                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getApplicationContext(), "administracion", null, 1);
                                SQLiteDatabase bd = admin.getWritableDatabase();
                                Cursor fila = bd.rawQuery(
                                        "select * from materiaprima where codigo=" + codigo.getText(), null);
                                if (fila.moveToFirst()) {
                                    do {
                                        elementos.add(fila.getString(0));
                                        codMat = Integer.parseInt(fila.getString(0).toString());
                                        elementos.add(fila.getString(1));
                                        elementos.add(fila.getString(2));
                                        elementos.add(fila.getString(3));
                                        elementos.add(fila.getString(4));
                                        elementos.add(fila.getString(5));
                                        cantidad.setText(fila.getString(2));
                                        tabla.agregarFilaTabla(elementos);
                                        elementos.clear();
                                    } while (fila.moveToNext());
                                }
                                bd.close();
                            }
                        }
                    });

                }

            }
        });


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if ((requestCode == request_code) && (resultCode == RESULT_OK)){
            codigo.setText(data.getDataString());
        }
    }

}