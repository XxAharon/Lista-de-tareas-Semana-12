package com.example.listadetareas_semana12.Model;

//1. Crear una clase Tarea pare representar a la entidad Tarea.
//Esta clase debe tener los siguientes atributos:
public class Tarea {

    private int id;
    private String tarea;
    private String fecha;
    private Boolean estado;
    private String prioridad;

    //2. Crear un constructor que inicialice los atributos.
    public Tarea(int id, String tarea, String fecha, Boolean estado, String prioridad) {
        this.id = id;
        this.tarea = tarea;
        this.fecha = fecha;
        this.estado = estado;
        this.prioridad = prioridad;
    }

    //3. Crear métodos get y set para cada atributo.
    public int getId() {
        return id;
    }

    public String getTarea() {
        return tarea;
    }

    public String getFecha() {
        return fecha;
    }

    public Boolean getEstado() {
        return estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
