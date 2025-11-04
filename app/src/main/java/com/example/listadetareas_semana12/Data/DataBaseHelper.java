package com.example.listadetareas_semana12.Data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//1. Crear clase DataBaseHelper que hereda de SQLiteOpenHelper la cual representa la creacion de la base de datos.
public class DataBaseHelper extends SQLiteOpenHelper {

    //2. Definir constantes para la base de datos.
    public static final String DataBaseName = "DbTareas";
    public static final int DataBaseVersion = 1;
    public static final String TableName = "TableTareas";
    public static final String COL_Id = "Id";
    public static final String COL_Tarea = "Tarea";
    public static final String COL_Fecha = "Fecha";
    public static final String COL_Estado = "Estado";
    public static final String COL_Prioridad = "Prioridad";

    //3. Definir la sentencia para crear la tabla.
    public static final String CreateTable = "CREATE TABLE " + TableName + "(" + COL_Id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COL_Tarea + " TEXT NOT NULL, "
            + COL_Fecha + " TEXT NOT NULL, "
            + COL_Estado + " INTEGER NOT NULL, "
            + COL_Prioridad + " TEXT NOT NULL)";

    //4. Constructor de la clase.
    public DataBaseHelper(android.content.Context context) {
        super(context, DataBaseName, null, DataBaseVersion);
    }

    //5. Metodos onCreate y onUpdate para crear y actualizar la base de datos.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }
}
