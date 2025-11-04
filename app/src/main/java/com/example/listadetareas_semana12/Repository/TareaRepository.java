package com.example.listadetareas_semana12.Repository;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.listadetareas_semana12.Data.DAO.TareaDAO;
import com.example.listadetareas_semana12.Data.DataBaseHelper;
import com.example.listadetareas_semana12.Model.Tarea;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//1. Creacion de clase TareaRepository para manejar la logica de negocio de la tarea.
public class TareaRepository {
    //Variable de TareaDAO para acceder a los metodos de la clase TareaDAO.
    private TareaDAO tareaDAO;
    //Variable de ExecutorService para ejecutar tareas en segundo plano.
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    //Formato de fecha para validar.
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    //MutableLiveData para notificar cambios en los datos.
    private final MutableLiveData<List<Tarea>> tareasLiveData = new MutableLiveData<>();
    //MutableLiveData para notificar errores.
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    //Constructor con context para la creacion de instancia de DataBaseHelper.
    public TareaRepository(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        //Asignacion de instancia de TareaDAO a la variable tareaDAO.
        //DataBaseHelper ya que sin la base de datos no se puede acceder a los metodos de la clase TareaDAO.
        this.tareaDAO = new TareaDAO(dataBaseHelper);
        cargarTareasInicialesRepository();
    }

    //Metodo para obtener el error en LiveData.
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void postErrorValue(String message) {
        errorLiveData.setValue(message);
    }

    //Metodo para cargar datos al inicio.
    private void cargarTareasInicialesRepository() {
        executor.execute(() -> {
            try {
                List<Tarea> tareas = tareaDAO.obtenerTareasDAO();
                tareasLiveData.postValue(tareas);
            } catch (Exception e) {
                tareasLiveData.postValue(Collections.emptyList());
                errorLiveData.postValue("Error al cargar las tareas iniciales.");
                e.printStackTrace();
            }
        });
    }

    //Metodo para evitar codigo extenso en cada validacion de datos.
    private void validarTarea(Tarea tarea) {
        if (tarea == null) {
            throw new IllegalArgumentException("Tarea no encontrada.");
        }
        if (tarea.getTarea().trim().isEmpty() || tarea.getPrioridad().trim().isEmpty()) {
            throw new IllegalArgumentException("El título y la prioridad no pueden estar vacíos.");
        }
        if (!"ALTA".equals(tarea.getPrioridad()) && !"MEDIA".equals(tarea.getPrioridad()) && !"BAJA".equals(tarea.getPrioridad())) {
            throw new IllegalArgumentException("Prioridad no válida. Use ALTA, MEDIA o BAJA.");
        }
        if (tarea.getFecha() != null && !tarea.getFecha().isEmpty() && !isDateFormatValid(tarea.getFecha())) {
            throw new IllegalArgumentException("La fecha no tiene el formato " + DATE_FORMAT + " válido.");
        }
        if(tarea.getTarea().length() > 20){
            throw new IllegalArgumentException("El titulo no puede tener mas de 20 caracteres.");
        }
    }

    //2. Metodo que utiliza la DAO para insertar una nueva tarea.
    public void insertarTareaRepository(Tarea tarea) {

        validarTarea(tarea);

        //Ejecucion de insertar tarea en segundo plano.
        executor.execute(() -> {
            try {
                tareaDAO.insertarTareaDAO(tarea);
                List<Tarea> tareasActualizadas = tareaDAO.obtenerTareasDAO();
                tareasLiveData.postValue(tareasActualizadas);
            } catch (Exception e) {
                errorLiveData.postValue("Error al insertar la tarea en BD.");
                e.printStackTrace();
            }
        });
    }

    //3. Metodo que valida el formato de la fecha.
    private boolean isDateFormatValid(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        sdf.setLenient(false);

        try {
            sdf.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    //4. Metodo que utiliza la DAO para obtener todas las tareas.
    public LiveData<List<Tarea>> obtenerTareasRepository() {
        return tareasLiveData;
    }

    //5. Metodo que utiliza la DAO para actualizar una tarea.
    public void actualizarTareaRepository(Tarea tarea) {
        //Validacion de datos.
        validarTarea(tarea);

        //Ejecucion de actualizar tarea en segundo plano.
        executor.execute(() -> {
            try {
                tareaDAO.actualizarTareaDAO(tarea);
                List<Tarea> tareasActualizadas = tareaDAO.obtenerTareasDAO();
                tareasLiveData.postValue(tareasActualizadas);
            } catch (Exception e) {
                errorLiveData.postValue("Error al actualizar la tarea en BD.");
                e.printStackTrace();
            }
        });
    }

    //6. Metodo que utiliza la DAO para eliminar una tarea.
    public void eliminarTareaRepository(Tarea tarea) {
        //Validacion de datos.
        if (tarea == null) {
            throw new IllegalArgumentException("Tarea no encontrada.");
        }
        //Ejecucion de eliminar tarea en segundo plano.
        executor.execute(() -> {
            try {

                //Obtencion de id de la tarea a eliminar.
                int id = tarea.getId();
                tareaDAO.eliminarTareaDAO(id);
                List<Tarea> tareasActualizadas = tareaDAO.obtenerTareasDAO();
                tareasLiveData.postValue(tareasActualizadas);

            } catch (Exception e) {
                errorLiveData.postValue("Error al eliminar la tarea en BD.");
                e.printStackTrace();
            }
        });
    }
}
