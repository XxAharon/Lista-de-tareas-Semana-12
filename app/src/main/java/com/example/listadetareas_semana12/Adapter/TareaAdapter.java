package com.example.listadetareas_semana12.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadetareas_semana12.Model.Tarea;
import com.example.listadetareas_semana12.R;

import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    // Interfaz para comunicar eventos de clic a la Activity/Fragment
    public interface OnItemClickListener {
        void onItemClick(Tarea tarea);
    }

    private final OnItemClickListener listener;
    private List<Tarea> tareas;

    // Constructor que recibe la lista inicial de datos.
    public TareaAdapter(List<Tarea> tareas, OnItemClickListener listener) {
        this.tareas = tareas;
        this.listener = listener;
    }

    // -----------------------------------------------------------
    // 1. ViewHolder: Almacena las referencias de los Views de cada fila.
    // -----------------------------------------------------------
    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTarea;
        public TextView textViewFecha;
        public CheckBox checkBoxEstado;
        public TextView textViewPrioridad;

        public TareaViewHolder(View itemView) {
            super(itemView);
            // Referencias a los IDs del layout de fila (item_tarea.xml)
            textViewTarea = itemView.findViewById(R.id.text_view_tarea);
            textViewFecha = itemView.findViewById(R.id.text_view_fecha);
            checkBoxEstado = itemView.findViewById(R.id.checkbox_view_estado);
            textViewPrioridad = itemView.findViewById(R.id.text_view_prioridad);
        }
    }

    // -----------------------------------------------------------
    // 2. Método onCreateViewHolder: Crea el nuevo ViewHolder (infla el layout).
    // -----------------------------------------------------------
    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout de la fila (el segundo XML que proporcionaste)
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycle_view, parent, false);
        return new TareaViewHolder(itemView);
    }

    // -----------------------------------------------------------
    // 3. Método onBindViewHolder: Rellena los datos en el ViewHolder reciclado/creado.
    // -----------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea currentTarea = tareas.get(position);

        // Mapear los datos del objeto Tarea a los Views
        holder.textViewTarea.setText(currentTarea.getTarea());
        holder.textViewFecha.setText(currentTarea.getFecha());
        holder.checkBoxEstado.setChecked(currentTarea.getEstado());
        holder.textViewPrioridad.setText(currentTarea.getPrioridad());

        // Desactivar la interacción del CheckBox para que solo muestre el estado
        // (La lógica de actualización debe ir en otro lugar, como un listener,
        // pero por ahora lo dejamos estático)
        holder.checkBoxEstado.setEnabled(false);
        // Asignamos el listener a toda la fila (itemView)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // NOTIFICAR a la Activity qué objeto Tarea fue clickeado
                listener.onItemClick(currentTarea);
            }
        });
    }

    // -----------------------------------------------------------
    // 4. Método getItemCount: Retorna el tamaño de la lista.
    // -----------------------------------------------------------
    @Override
    public int getItemCount() {
        return tareas.size();
    }

    // -----------------------------------------------------------
    // 5. Método de Actualización (Usado por el LiveData Observer en MainActivity)
    // -----------------------------------------------------------
    /**
     * Reemplaza la lista actual del Adapter con la nueva lista del LiveData.
     * @param nuevaLista La lista actualizada de tareas.
     */
    public void setTareas(List<Tarea> nuevaLista) {
        this.tareas = nuevaLista;
        // La MainActivity llamará a notifyDataSetChanged() después de llamar a este método.
    }
}
