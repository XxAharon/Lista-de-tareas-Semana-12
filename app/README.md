📋 Lista de Tareas Android
🌟 Descripción del Proyecto
Este proyecto es una aplicación de gestión de tareas móvil desarrollada en Android que implementa la arquitectura MVVM (Model-View-ViewModel). Utiliza SQLite para la persistencia local de los datos y LiveData para crear una interfaz de usuario completamente reactiva que se actualiza automáticamente ante cualquier cambio en la base de datos.

El objetivo principal es demostrar la separación de responsabilidades, la robustez del manejo de datos asíncronos y la gestión del ciclo de vida de Android.

🏗️ Arquitectura y Componentes Clave
El proyecto se divide en capas siguiendo el principio de separación de responsabilidades (MVVM):

1. View (MainActivity, TareaAdapter)
   Responsable de la interfaz de usuario.

Observa los datos (LiveData) expuestos por el ViewModel.

Maneja las interacciones del usuario (clics, diálogos).

Utiliza el RecyclerView con el TareaAdapter para mostrar la lista.

2. ViewModel (TareaViewModel, TareaViewModelFactory)
   Almacena y gestiona los datos relacionados con la UI en un formato que sobrevive a los cambios de configuración.

Expone los datos reactivos (LiveData).

Sirve como intermediario, delegando las operaciones de negocio al Repository.

TareaViewModelFactory: Es crucial para inyectar el TareaRepository al ViewModel de forma segura.

3. Repository (TareaRepository)
   Centraliza la lógica de negocio y la gestión de datos.

Decide si obtener datos de la caché, red, o, en este caso, la base de datos local (SQLite).

Utiliza un ExecutorService para asegurar que las operaciones de la base de datos se ejecuten en un hilo secundario, evitando bloquear la UI.

4. Data (Tarea, TareaDAO, DataBaseHelper)
   Tarea: La entidad modelo que define la estructura de los datos.

TareaDAO: Contiene los métodos de acceso directo a datos (CRUD: Create, Read, Update, Delete) para la base de datos SQLite.

DataBaseHelper: Clase que gestiona la creación y actualización del esquema de la base de datos.

💡 Flujo de Interacción (Ejemplo: Actualización)
El flujo reactivo garantiza que la UI siempre refleje el estado de la base de datos:

View (Click): El usuario pulsa una fila, se abre un diálogo de edición.

View (Llamada): Al pulsar "Actualizar", MainActivity llama a tareaViewModel.actualizarTarea(tareaActualizada).

Repository (Asíncrono): El Repository ejecuta tareaDAO.actualizarTareaDAO() en un hilo secundario.

Repository (Notificación): Inmediatamente después de la actualización, el Repository re-consulta la lista completa de la BD y publica los nuevos datos en el MutableLiveData.

View (Reacción): El Observer en MainActivity detecta el cambio, el TareaAdapter se actualiza y el RecyclerView se refresca en pantalla.

🛠️ Funcionalidades de Usuario
Función	Botón/Interacción	Implementación
Crear Tarea	Botón Agregar	Muestra un AlertDialog para capturar la descripción, fecha y prioridad.
Ver Tareas	Pantalla Principal	Lista de tareas renderizada por el RecyclerView y actualizada por LiveData.
Editar/Actualizar	Clic en una fila	Abre un AlertDialog precargado con los datos para modificar cualquier campo (incluido el estado y la prioridad).
Eliminar Tarea	Botón Eliminar dentro del diálogo de edición.	Elimina la tarea de la base de datos local.
Gestión de Errores	N/A	Errores de validación (campos vacíos, formato incorrecto) se muestran mediante un Toast desde un LiveData<String> dedicado.

Exportar a Hojas de cálculo

⚙️ Requisitos y Configuración
Lenguaje: Java

IDE: Android Studio

API Mínima: (Dependerá de las librerías, pero generalmente 21+)

Dependencias Clave:

androidx.lifecycle (ViewModel, LiveData)

androidx.recyclerview

androidx.constraintlayout