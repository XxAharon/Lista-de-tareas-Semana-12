package com.example.listadetareas_semana12.Data.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.listadetareas_semana12.Data.DataBaseHelper;
import com.example.listadetareas_semana12.Model.Tarea;

import java.util.ArrayList;
import java.util.List;

//1. Crear clase TareaDAO para interactuar con la base de datos generando los metodos CRUD.
public class TareaDAO {

    //2. Crear una instancia de la clase DataBaseHelper.
    private final DataBaseHelper dbHelper;

    //3. Crear un construtor que reciba la instancia de la clase DataBaseHelper.
    public TareaDAO(DataBaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    //4. Crear metodo CRUD para insertar una tarea (Create).
    public long insertarTareaDAO(Tarea tarea) {
        // Abrir la base de datos en modo de escritura.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Crear un objeto ContentValues para almacenar los valores a insertar en la base de datos.
        ContentValues values = new ContentValues();

        //Asignar los valores a los campos de la tabla.
        values.put(dbHelper.COL_Tarea, tarea.getTarea());
        values.put(dbHelper.COL_Fecha, tarea.getFecha());
        values.put(dbHelper.COL_Estado, tarea.getEstado() ? 1 : 0);
        values.put(dbHelper.COL_Prioridad, tarea.getPrioridad());

        //Crear una nueva tarea en la base de datos.
        long id = db.insert(dbHelper.TableName, null, values);
        //Retornar el long del id (Long para poder almacenar datos numericos mas grandes).
        db.close();
        return id;
    }

    //5. Crear metodo CRUD para leer las tareas (Read).
    public List<Tarea> obtenerTareasDAO() {
        //Crear lista que contendra los objetos obtenidos de la consulta.
        List<Tarea> tareas = new ArrayList<>();
        //Abrir uan base de datos en modo de lectura.
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Definir las columnas que se van a devolver de la consulta.
        String[] columnas = {
                dbHelper.COL_Id,
                dbHelper.COL_Tarea,
                dbHelper.COL_Fecha,
                dbHelper.COL_Estado,
                dbHelper.COL_Prioridad
        };

        //Crear un cursor que inspeccionara cada fila de la tabla con los datos en el orden solicitados.
        Cursor cursor = db.query(
                dbHelper.TableName,
                columnas,
                null,
                null,
                null,
                null,
                dbHelper.COL_Id + " ASC"
        );

        //Verificar si el cursor encuentra el primer dato de la tabla.
        if (cursor.moveToFirst()) {
            do {
                //Crear variables que contengan los datos obtenidos.
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(dbHelper.COL_Id));
                String tarea = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COL_Tarea));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COL_Fecha));
                int estado = cursor.getInt(cursor.getColumnIndexOrThrow(dbHelper.COL_Estado));
                String prioridad = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COL_Prioridad));

                //Tranformar a bool el dato de estado ya que este se guarda como (1 o 0) en la tabla.
                boolean estadoBoolean = (estado == 1);

                //Crear un objeto tarea que contenga los datos obtenidos.
                Tarea objetoTarea = new Tarea(id, tarea, fecha, estadoBoolean, prioridad);

                //Agregar el objeto a la lista de tareas.
                tareas.add(objetoTarea);

                //Verificar si el cursor encuentra el siguiente dato de la tabla.
            } while (cursor.moveToNext());
        }
        //Cerrar la db, el cursor y retornar la lista creada con los objetos guardados si el cursor ya no encuntra mas datos en la tabla.
        cursor.close();
        db.close();
        return tareas;
    }

    //6. Crear metodo CRUD para actualizar una tarea (Update).
    public void actualizarTareaDAO(Tarea tarea) {
        //Abrir la base de datos en modo de escritura.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Crear un objeto ContentValues para almacenar los valores a actualizar en la base de datos.
        ContentValues values = new ContentValues();

        //Asignar los valores a los campos de la tabla.
        values.put(dbHelper.COL_Id, tarea.getId());
        values.put(dbHelper.COL_Tarea, tarea.getTarea());
        values.put(dbHelper.COL_Fecha, tarea.getFecha());
        values.put(dbHelper.COL_Estado, tarea.getEstado() ? 1 : 0);
        values.put(dbHelper.COL_Prioridad, tarea.getPrioridad());

        //Asignar la condicion de actualizacion de la tarea.
        String seleccion = dbHelper.COL_Id + " = ?";
        String[] seleccionArgs = {String.valueOf(tarea.getId())};

        //Actualizar la tarea en la base de datos.
        db.update(
                dbHelper.TableName,
                values,
                seleccion,
                seleccionArgs
        );
        //Cerrar la db.
        db.close();
    }

    //7. Crear metodo CRUD para eliminar una tarea (Delete).
    public void eliminarTareaDAO(int tareaId) {
        //Abrir la base de datos en modo de escritura.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Asignar la condicion de eliminacion de la tarea.
        String seleccion = dbHelper.COL_Id + " = ?";
        String[] seleccionArgs = {String.valueOf(tareaId)};

        //Eliminar la tarea en la base de datos.
        db.delete(
                dbHelper.TableName,
                seleccion,
                seleccionArgs
        );
        //Cerrar la db.
        db.close();
    }
}
