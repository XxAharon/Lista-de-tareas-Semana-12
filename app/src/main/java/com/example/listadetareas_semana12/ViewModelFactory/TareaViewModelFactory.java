package com.example.listadetareas_semana12.ViewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.listadetareas_semana12.Repository.TareaRepository;
import com.example.listadetareas_semana12.ViewModel.TareaViewModel;

public class TareaViewModelFactory implements ViewModelProvider.Factory {
    private final TareaRepository repository;
    // Constructor que recibe el Repository como argumento.
    public TareaViewModelFactory(TareaRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TareaViewModel.class)) {
            // Retorna una nueva instancia de TareaViewModel con el Repository
            return (T) new TareaViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
