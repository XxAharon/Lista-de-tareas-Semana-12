package com.example.listadetareas_semana12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadetareas_semana12.Adapter.TareaAdapter;
import com.example.listadetareas_semana12.Model.Tarea;
import com.example.listadetareas_semana12.Repository.TareaRepository;
import com.example.listadetareas_semana12.ViewModel.TareaViewModel;
import com.example.listadetareas_semana12.ViewModelFactory.TareaViewModelFactory;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TareaAdapter.OnItemClickListener {

    private TareaViewModel tareaViewModel;
    private RecyclerView recyclerView;
    private TareaAdapter tareaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inicializar el Repository (Usando Application Context para evitar leaks)
        TareaRepository repository = new TareaRepository(getApplicationContext());

        // 2. Crear la Factory
        TareaViewModelFactory factory = new TareaViewModelFactory(repository);

        // 3. Inicializar el ViewModel usando la Factory
        tareaViewModel = new ViewModelProvider(this, factory).get(TareaViewModel.class);

        // 4. Configurar el RecyclerView y el Adapter
        recyclerView = findViewById(R.id.recycler_view);
        tareaAdapter = new TareaAdapter(new ArrayList<>(), this); // Pasar lista vacía inicial
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tareaAdapter);

        // 5. OBSERVAR el LiveData de la lista de tareas
        observarListaDeTareas();

        findViewById(R.id.button_agregar).setOnClickListener(v -> {
            mostrarDialogoAgregarTarea();
        });
    }

    @Override
    public void onItemClick(Tarea tarea) {
        // Cuando se toca una fila, mostramos el diálogo con los datos de esa tarea.
        mostrarDialogoEditarTarea(tarea);
    }

    private void mostrarDialogoAgregarTarea() {
        // 1. Inflar el layout personalizado
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_tarea, null);

        final EditText etTarea = dialogView.findViewById(R.id.edit_text_tarea);
        final EditText etFecha = dialogView.findViewById(R.id.edit_text_fecha);
        final Spinner spinnerPrioridad = dialogView.findViewById(R.id.spinner_prioridad);

        // 2. Crear y configurar el AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Agregar Nueva Tarea")
                .setView(dialogView)
                // Botón Aceptar
                .setPositiveButton("Guardar", (dialog, which) -> {
                    // Se ejecuta cuando el usuario presiona GUARDAR
                    String tareaDesc = etTarea.getText().toString().trim();
                    String fecha = etFecha.getText().toString().trim();
                    String prioridad = spinnerPrioridad.getSelectedItem().toString();

                    // 3. Crear el objeto Tarea (ID=0 para que la BD lo auto-incremente)
                    Tarea nuevaTarea = new Tarea(
                            0,                  // ID (la BD lo asignará)
                            tareaDesc,
                            fecha,
                            false,              // Estado inicial: no completada
                            prioridad
                    );

                    // 4. Llamar al ViewModel para guardar
                    // Si hay errores de validación (ej. campos vacíos), el VM/Repository
                    // los manejará y el Toast se mostrará a través del LiveData de error.
                    tareaViewModel.insertarTarea(nuevaTarea);

                    // El LiveData se encargará de actualizar el RecyclerView automáticamente.
                })
                // Botón Cancelar
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void observarListaDeTareas() {
        // 6. La clave: Suscribirse a la lista que gestiona el ViewModel
        tareaViewModel.obtenerTareas().observe(this, tareas -> {
            // Cuando el LiveData cambia (por INSERT, UPDATE, DELETE o Carga Inicial):

            // a) Actualizar los datos del Adapter
            tareaAdapter.setTareas(tareas);

            // b) Notificar al RecyclerView que los datos han cambiado
            tareaAdapter.notifyDataSetChanged();
        });

        // 7. OBSERVAR el LiveData de errores
        tareaViewModel.obtenerErrorMensaje().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                tareaViewModel.limpiarMensajeError();
            }
        });
    }

    private void mostrarDialogoEditarTarea(Tarea tareaOriginal) {
        // 1. Inflar el layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_eliminar_actualizar_tarea, null);

        final EditText etTarea = dialogView.findViewById(R.id.edit_text_tarea);
        final EditText etFecha = dialogView.findViewById(R.id.edit_text_fecha);
        final Spinner spinnerPrioridad = dialogView.findViewById(R.id.spinner_prioridad);

        // OBTENER REFERENCIA DEL NUEVO CHECKBOX
        final CheckBox cbEstado = dialogView.findViewById(R.id.checkbox_estado_dialog);

        // 2. Precargar los datos
        etTarea.setText(tareaOriginal.getTarea());
        etFecha.setText(tareaOriginal.getFecha());

        // ESTABLECER EL ESTADO ACTUAL EN EL CHECKBOX
        cbEstado.setChecked(tareaOriginal.getEstado());

        // Cargar la prioridad: se necesita encontrar la posición en el array
        String[] prioridades = getResources().getStringArray(R.array.prioridad_opciones);
        int prioridadIndex = 0;
        for (int i = 0; i < prioridades.length; i++) {
            if (prioridades[i].equals(tareaOriginal.getPrioridad())) {
                prioridadIndex = i;
                break;
            }
        }
        spinnerPrioridad.setSelection(prioridadIndex);


        // 3. Crear y configurar el AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Editar/Eliminar Tarea (ID: " + tareaOriginal.getId() + ")")
                .setView(dialogView)

                // Botón POSITIVO: ACTUALIZAR
                .setPositiveButton("Actualizar", (dialog, which) -> {
                    String tareaDesc = etTarea.getText().toString().trim();
                    String fecha = etFecha.getText().toString().trim();
                    String prioridad = spinnerPrioridad.getSelectedItem().toString();

                    // LEER EL NUEVO ESTADO DEL CHECKBOX
                    boolean nuevoEstado = cbEstado.isChecked();

                    // Crear un nuevo objeto Tarea con el ID original y los nuevos datos
                    Tarea tareaActualizada = new Tarea(
                            tareaOriginal.getId(), // ¡CRUCIAL: Mantener el ID original!
                            tareaDesc,
                            fecha,
                            nuevoEstado,           // USAMOS EL VALOR DEL CHECKBOX
                            prioridad
                    );

                    // Llamar al ViewModel para actualizar
                    tareaViewModel.actualizarTarea(tareaActualizada);
                })

                // Botón NEUTRO: ELIMINAR
                .setNeutralButton("Eliminar", (dialog, which) -> {
                    // Llamar al ViewModel para eliminar
                    tareaViewModel.eliminarTarea(tareaOriginal);
                })

                // Botón NEGATIVO: CANCELAR
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}