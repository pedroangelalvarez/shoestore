package com.aim.shoestore;

/**
 * Created by pedro on 15/12/17.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String crearUsuario="create table empleado(dni int primary key ,nombre text,apellido text, correo text, username text,  pass text)";
        db.execSQL(crearUsuario);
        String crearProducto="create table producto(codigo int primary key ,nombre text,cantidad int, puntor int, precio float, talla int, caract text, marca text, color text)";
        db.execSQL(crearProducto);
        String crearMateriaP = "create table materiaprima(codigo int primary key,nombre text, cantidad int, puntor int, unidadm text,observacion text)";
        db.execSQL(crearMateriaP);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}