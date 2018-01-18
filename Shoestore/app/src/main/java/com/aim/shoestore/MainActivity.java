package com.aim.shoestore;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech t1;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();

        // Obtienes el texto
        user = bundle.getString("user");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        insertMaterias();
        insertProductos();
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Locale loc = new Locale("es", "","");
                if(t1.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
                    t1.setLanguage(loc);

                }
            }
        });
        Button matPrim = (Button)findViewById(R.id.btnMatPrim);
        matPrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MateriaPrima.class);
                startActivity(i);
            }
        });
        matPrim.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingresar a la vista de Materia Prima", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        Button producto = (Button)findViewById(R.id.btnProducto);
        producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Producto.class);
                startActivity(i);
            }
        });
        producto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingresar a la vista de Producto", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        Button cuenta = (Button)findViewById(R.id.btnCuenta);
        cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accountsIntent = new Intent(getApplicationContext(), Cuenta.class);
                accountsIntent.putExtra("user", user);
                startActivity(accountsIntent);
            }
        });
        cuenta.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingresar a la vista de Configuración de Cuenta", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        Button cerrar = (Button)findViewById(R.id.btnCerrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cerrar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Cerrar Sesión", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
    }

    public boolean insertarMaterias(int codigo, String nombre, int cantidad, int puntor, String unidadm, String observacion) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("codigo",codigo);
        contentValues.put("nombre", nombre);
        contentValues.put("cantidad",cantidad);
        contentValues.put("puntor",puntor);
        contentValues.put("unidadm",unidadm);
        contentValues.put("observacion",observacion);
        bd.insert("materiaprima", null, contentValues);
        bd.close();
        return true;
    }
    public boolean insertarProductos(int codigo, String nombre, int cantidad, int puntor, float precio, int  talla, String caract, String marca, String color) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("codigo",codigo);
        contentValues.put("nombre",nombre);
        contentValues.put("cantidad",cantidad);
        contentValues.put("puntor",puntor);
        contentValues.put("precio",precio);
        contentValues.put("talla",talla);
        contentValues.put("caract",caract);
        contentValues.put("marca",marca);
        contentValues.put("color",color);
        bd.insert("producto", null, contentValues);
        bd.close();
        return true;
    }

    public void insertMaterias() {

        insertarMaterias(303,"Antitranspirante",405,300, "litros","se agota rapido");
        insertarMaterias(304,"Latex",333,250,"litros","bastante util");
        insertarMaterias(404,"Plantillas", 450, 400, "unidades","fundamental");
    }
    public void insertProductos() {

        insertarProductos(101,"Mocasines",70,50,75,42,"Sofisticados","Polo","azul");
        insertarProductos(102,"Botas",88,50,110,38,"Abrigadas","Vegeta","Blanco");
         insertarProductos(201,"Balerinas",65,40,55,39,"Elegantes","Karen","Rosado");
         insertarProductos(203,"Botines",55,35,88,38,"Moderno","Diana","Negro");
    }
}