package com.example.listadetareas_semana12.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.listadetareas_semana12.Model.Tarea;
import com.example.listadetareas_semana12.Repository.TareaRepository;

import java.util.List;

public class TareaViewModel extends ViewModel {
    private final TareaRepository tareaRepository;
    private final LiveData<List<Tarea>> Listatareas;
    private final LiveData<String> errorMensaje;

    public TareaViewModel(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
        this.Listatareas = tareaRepository.obtenerTareasRepository();
        this.errorMensaje = tareaRepository.getErrorLiveData();
    }

    public LiveData<List<Tarea>> obtenerTareas() {
        return Listatareas;
    }

    public LiveData<String> obtenerErrorMensaje() {
        return errorMensaje;
    }

    public void insertarTarea(Tarea tarea) {
        try{
            tareaRepository.insertarTareaRepository(tarea);
        }catch (IllegalArgumentException e){
            tareaRepository.postErrorValue(e.getMessage());
        }
    }

    public void actualizarTarea(Tarea tarea) {
        try{
            tareaRepository.actualizarTareaRepository(tarea);
        }catch (IllegalArgumentException e){
            tareaRepository.postErrorValue(e.getMessage());
        }
    }

    public void eliminarTarea(Tarea tarea) {
        try{
            tareaRepository.eliminarTareaRepository(tarea);
        }catch (IllegalArgumentException e){
            tareaRepository.postErrorValue(e.getMessage());
        }
    }

    public void limpiarMensajeError() {
        // Establecer el valor a null para evitar que el Toast se muestre de nuevo
        tareaRepository.postErrorValue(null);
    }
}
