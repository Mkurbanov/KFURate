package com.mkurbanov.kfurate.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mkurbanov.kfurate.data.repository.MainRepository;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private MainRepository mainRepository;

    public HomeViewModelFactory(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeViewModel(mainRepository);
    }
}
