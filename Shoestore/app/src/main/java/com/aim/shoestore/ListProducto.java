package com.aim.shoestore;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by pedro on 08/12/17.
 */

public class ListProducto  extends AppCompatActivity {
    int cantProd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listprod);
        cantProd=cantidadProductos();
        Tabla tabla = new Tabla(this, (TableLayout)findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tablaProd);
        ArrayList<String> elementos = new ArrayList<String>();

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            Cursor fila = bd.rawQuery(
                    "select * from producto", null);
            if (fila.moveToFirst()) {
                do {
                elementos.add(fila.getString(0));
                elementos.add(fila.getString(1));
                elementos.add(fila.getString(2));
                elementos.add(fila.getString(3));
                elementos.add(fila.getString(4));
                elementos.add(fila.getString(5));
                elementos.add(fila.getString(6));
                elementos.add(fila.getString(7));
                elementos.add(fila.getString(8));

                tabla.agregarFilaTabla(elementos);
                elementos.clear();
                } while (fila.moveToNext());
            }
        bd.close();


    }

    public int cantidadProductos() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        int valor=0;
        Cursor fila = bd.rawQuery(
                "select count(*) from producto", null);
        if (fila.moveToFirst()) {
            valor = Integer.parseInt(fila.getString(0));
            bd.close();

        } else{
            Toast.makeText(this, "Usuario no registrado",
                    Toast.LENGTH_SHORT).show();
            }
        return valor;

    }
}