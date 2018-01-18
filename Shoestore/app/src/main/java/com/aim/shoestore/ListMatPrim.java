package com.aim.shoestore;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

import java.util.ArrayList;

/**
 * Created by pedro on 08/12/17.
 */

public class ListMatPrim extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listmatprim);
        Tabla tabla = new Tabla(this, (TableLayout)findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tablaMatPrima);
        ArrayList<String> elementos = new ArrayList<String>();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "select * from materiaprima", null);
        if (fila.moveToFirst()) {
            do {
                elementos.add(fila.getString(0));
                elementos.add(fila.getString(1));
                elementos.add(fila.getString(2));
                elementos.add(fila.getString(3));
                elementos.add(fila.getString(4));
                elementos.add(fila.getString(5));

                tabla.agregarFilaTabla(elementos);
                elementos.clear();
            } while (fila.moveToNext());
        }
        bd.close();

    }

}